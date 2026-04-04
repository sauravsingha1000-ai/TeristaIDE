/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 */

@Suppress("JavaPluginLanguageLevel")
plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    api(libs.common.jkotlin)
    api(libs.tests.robolectric)
    api(libs.tests.junit)
    api(libs.tests.google.truth)
    api(libs.tests.mockk)

    api(projects.buildInfo)
    api(projects.logger)
    api(projects.shared)
    api(projects.subprojects.toolingApi)

    // build tooling API before tests
    compileOnly(projects.subprojects.toolingApiImpl)
}
