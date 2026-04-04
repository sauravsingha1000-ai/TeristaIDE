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
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
}

android {
  namespace = "${BuildConfig.packageName}.templates.impl"

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
  kapt(libs.google.auto.service)

  api(projects.templatesApi)

  implementation(projects.shared)
  implementation(projects.common)
  implementation(projects.preferences)
  implementation(projects.subprojects.projects)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.auto.service.annotations)

  testImplementation(projects.templatesApi)
  testImplementation(projects.lsp.api)
  testImplementation(projects.preferences)
  testImplementation(projects.testing.unit)
  testImplementation(projects.testing.tooling)
}
