/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 */

@file:Suppress("UnstableApiUsage")

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-parcelize")
  id("com.google.devtools.ksp") version libs.versions.ksp
}

android {
  namespace = "${BuildConfig.packageName}.inflater"

  compileSdk = 35

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {
  ksp(projects.annotationProcessorsKsp)

  implementation(libs.androidx.appcompat)
  implementation(libs.common.kotlin)
  implementation(libs.common.utilcode)

  implementation(projects.annotations)
  implementation(projects.common)
  implementation(projects.subprojects.aaptcompiler)
  implementation(projects.subprojects.projects)
  implementation(projects.subprojects.xmlUtils)
  implementation(projects.resources)

  testImplementation(projects.subprojects.projects)
  testImplementation(projects.testing.tooling)
}
