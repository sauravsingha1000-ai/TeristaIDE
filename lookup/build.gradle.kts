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
    id("kotlin-kapt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    kapt(libs.google.auto.service)

    implementation(projects.logger)
    implementation(projects.shared)

    implementation(libs.common.jkotlin)
    implementation(libs.google.auto.service.annotations)
    implementation(libs.google.guava)

    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
}
