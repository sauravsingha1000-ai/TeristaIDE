/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 */

plugins {
    id("com.android.library")
}

android {
    namespace = "com.google.googlejavaformat"

    compileSdk = 35

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.google.guava)
    implementation(libs.google.auto.value.annotations)
    implementation(libs.google.auto.service.annotations)
    implementation(projects.subprojects.javac)

    annotationProcessor(libs.google.auto.value.ap)
    annotationProcessor(libs.google.auto.service)
}
