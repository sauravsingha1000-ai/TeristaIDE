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
    api(projects.buildInfo)
    api(projects.logger)
    api(projects.shared)
    api(projects.subprojects.toolingApiModel)
    api(projects.subprojects.toolingApiEvents)

    api(libs.google.gson)
    api(libs.common.jsonrpc)
    implementation(libs.common.jkotlin)
}
