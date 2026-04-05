/**
 * Copyright (c) 2018 Angelo ZERR All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.uriresolver;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;

/** URI resolver manager. */
public class URIResolverExtensionManager
implements URIResolverExtension, IExternalGrammarLocationProvider {

private final List<URIResolverExtension> resolvers;

private final URIResolverExtension defaultURIResolverExtension;

public URIResolverExtensionManager() {
resolvers = new ArrayList<>();
this.defaultURIResolverExtension = new DefaultURIResolverExtension();
}

static class DefaultURIResolverExtension implements URIResolverExtension {

@Override
public String getName() {
  return URIResolverExtension.DEFAULT;
}

@Override
public String resolve(String baseLocation, String publicId, String systemId) {
  try {
    if (systemId == null) {
      return null;
    }
    if (baseLocation == null) {
      return systemId;
    }
    URI base = new URI(baseLocation);
    return base.resolve(systemId).toString();
  } catch (Exception e) {
    return systemId;
  }
}

@Override
public XMLInputSource resolveEntity(XMLResourceIdentifier rid)
    throws XNIException, IOException {

  String id = rid.getPublicId();
  if (id == null) {
    id = rid.getNamespace();
  }

  String location = null;
  if (id != null || rid.getLiteralSystemId() != null) {
    location = this.resolve(rid.getBaseSystemId(), id, rid.getLiteralSystemId());
  }

  if (location != null) {
    return new XMLInputSource(rid.getPublicId(), location, location);
  }

  return null;
}

}

public void registerResolver(URIResolverExtension resolver) {
resolvers.add(resolver);
}

public void unregisterResolver(URIResolverExtension resolver) {
resolvers.remove(resolver); // ✅ FIXED BUG
}

@Override
public String resolve(String baseLocation, String publicId, String systemId) {
for (URIResolverExtension resolver : resolvers) {
String resolved = resolver.resolve(baseLocation, publicId, systemId);
if (resolved != null && !resolved.isEmpty()) {
return resolved;
}
}
return defaultURIResolverExtension.resolve(baseLocation, publicId, systemId);
}

public ResolvedURIInfo resolveInfo(String baseLocation, String publicId, String systemId) {
for (URIResolverExtension resolver : resolvers) {
String resolvedURI = resolver.resolve(baseLocation, publicId, systemId);
if (resolvedURI != null && !resolvedURI.isEmpty()) {
return new ResolvedURIInfo(resolvedURI, resolver);
}
}

String resolvedURI = defaultURIResolverExtension.resolve(baseLocation, publicId, systemId);
if (resolvedURI != null && !resolvedURI.isEmpty()) {
  return new ResolvedURIInfo(resolvedURI, defaultURIResolverExtension);
}

return null;

}

@Override
public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
throws XNIException, IOException {

for (URIResolverExtension resolver : resolvers) {
  XMLInputSource is = resolver.resolveEntity(resourceIdentifier);
  if (is != null) {
    return is;
  }
}

return defaultURIResolverExtension.resolveEntity(resourceIdentifier);

}

@Override
public Map<String, String> getExternalGrammarLocation(URI fileURI) {
for (URIResolverExtension resolver : resolvers) {
if (resolver instanceof IExternalGrammarLocationProvider) {
Map<String, String> result =
((IExternalGrammarLocationProvider) resolver)
.getExternalGrammarLocation(fileURI);

    if (result != null) {
      return result;
    }
  }
}
return null;

}
}
