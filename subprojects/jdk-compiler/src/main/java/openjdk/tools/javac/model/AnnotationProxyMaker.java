/*
 * Copyright (c) 2005, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package openjdk.tools.javac.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import jdkx.lang.model.type.*;
import openjdk.tools.javac.code.*;
import openjdk.tools.javac.code.Symbol.*;
import openjdk.tools.javac.code.Type.ArrayType;
import openjdk.tools.javac.util.*;

import static openjdk.tools.javac.code.Scope.LookupKind.NON_RECURSIVE;
import static openjdk.tools.javac.code.Kinds.Kind.*;

public class AnnotationProxyMaker {

private final Attribute.Compound anno;
private final Class<? extends Annotation> annoType;

private AnnotationProxyMaker(Attribute.Compound anno,
                             Class<? extends Annotation> annoType) {
    this.anno = anno;
    this.annoType = annoType;
}

public static <A extends Annotation> A generateAnnotation(
        Attribute.Compound anno, Class<A> annoType) {
    AnnotationProxyMaker apm = new AnnotationProxyMaker(anno, annoType);
    return annoType.cast(apm.generateAnnotation());
}

private Annotation generateAnnotation() {
    return (Annotation) Proxy.newProxyInstance(
            annoType.getClassLoader(),
            new Class[]{annoType},
            new AnnotationInvocationHandler(annoType, getAllReflectedValues())
    );
}

private Map<String, Object> getAllReflectedValues() {
    Map<String, Object> res = new LinkedHashMap<>();

    for (Map.Entry<MethodSymbol, Attribute> entry :
            getAllValues().entrySet()) {

        MethodSymbol meth = entry.getKey();
        Object value = generateValue(meth, entry.getValue());

        if (value != null) {
            res.put(meth.name.toString(), value);
        }
    }
    return res;
}

private Map<MethodSymbol, Attribute> getAllValues() {
    Map<MethodSymbol, Attribute> res = new LinkedHashMap<>();

    ClassSymbol sym = (ClassSymbol) anno.type.tsym;

    for (Symbol s : sym.members().getSymbols(NON_RECURSIVE)) {
        if (s.kind == MTH) {
            MethodSymbol m = (MethodSymbol) s;
            Attribute def = m.getDefaultValue();
            if (def != null)
                res.put(m, def);
        }
    }

    for (Pair<MethodSymbol, Attribute> p : anno.values)
        res.put(p.fst, p.snd);

    return res;
}

private Object generateValue(MethodSymbol meth, Attribute attr) {
    ValueVisitor vv = new ValueVisitor(meth);
    return vv.getValue(attr);
}

// =========================
// INTERNAL REPLACEMENT LAYER
// =========================

private static abstract class ExceptionProxy implements java.io.Serializable {
    protected abstract RuntimeException generateException();
}

private static class AnnotationInvocationHandler implements InvocationHandler {
    private final Class<? extends Annotation> type;
    private final Map<String, Object> values;

    AnnotationInvocationHandler(Class<? extends Annotation> type,
                                Map<String, Object> values) {
        this.type = type;
        this.values = values;
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        String name = method.getName();

        if (name.equals("annotationType")) return type;

        Object value = values.get(name);
        if (value instanceof ExceptionProxy)
            throw ((ExceptionProxy) value).generateException();

        if (value != null) return value;

        return method.getDefaultValue();
    }
}

// =========================
// ORIGINAL LOGIC PRESERVED
// =========================

private class ValueVisitor implements Attribute.Visitor {

    private MethodSymbol meth;
    private Class<?> returnClass;
    private Object value;

    ValueVisitor(MethodSymbol meth) {
        this.meth = meth;
    }

    Object getValue(Attribute attr) {
        Method method;
        try {
            method = annoType.getMethod(meth.name.toString());
        } catch (NoSuchMethodException e) {
            return null;
        }
        returnClass = method.getReturnType();
        attr.accept(this);
        return value;
    }

    public void visitConstant(Attribute.Constant c) {
        value = c.getValue();
    }

    public void visitClass(Attribute.Class c) {
        value = new MirroredTypeExceptionProxy(c.classType);
    }

    public void visitArray(Attribute.Array a) {
        int len = a.values.length;
        Object res = Array.newInstance(returnClass.getComponentType(), len);

        for (int i = 0; i < len; i++) {
            a.values[i].accept(this);
            Array.set(res, i, value);
        }
        value = res;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public void visitEnum(Attribute.Enum e) {
        try {
            value = Enum.valueOf((Class)returnClass, e.value.toString());
        } catch (Exception ex) {
            value = new RuntimeException(e.value.toString());
        }
    }

    public void visitCompound(Attribute.Compound c) {
        value = generateAnnotation(c, returnClass.asSubclass(Annotation.class));
    }

    public void visitError(Attribute.Error e) {
        value = null;
    }
}

// =========================
// EXCEPTION PROXIES (UNCHANGED LOGIC)
// =========================

private static final class MirroredTypeExceptionProxy extends ExceptionProxy {
    static final long serialVersionUID = 269;
    private transient TypeMirror type;
    private final String typeString;

    MirroredTypeExceptionProxy(TypeMirror t) {
        type = t;
        typeString = t.toString();
    }

    protected RuntimeException generateException() {
        return new MirroredTypeException(type);
    }
}

private static final class MirroredTypesExceptionProxy extends ExceptionProxy {
    static final long serialVersionUID = 269;

    private transient java.util.List<TypeMirror> types;
    private final String typeStrings;

    MirroredTypesExceptionProxy(java.util.List<TypeMirror> ts) {
        types = ts;
        typeStrings = ts.toString();
    }

    protected RuntimeException generateException() {
        return new MirroredTypesException(types);
    }
}

}
