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
    id("kotlin-android")
}

android {
    namespace = "${BuildConfig.packageName}.aaptcompiler"

    compileSdk = 35

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.common.kotlin)
    implementation(libs.androidx.collection)
    implementation(projects.logger)
    implementation(projects.subprojects.jaxp)

    api(libs.aapt2.annotations)
    api(libs.aapt2.common)
    api(libs.aapt2.proto)
    api(libs.google.protobuf)
    api(projects.subprojects.layoutlibApi)

    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.robolectric)
    testImplementation(libs.tests.google.truth)
    testImplementation(projects.shared)
}
