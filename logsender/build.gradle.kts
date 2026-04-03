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
  id("com.vanniktech.maven.publish.base")
}

description = "LogSender is used to read logs from applications built with AndroidIDE"

android {
  namespace = "${BuildConfig.packageName}.logsender"

  compileSdk = 35

  defaultConfig {
    minSdk = 16

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  @Suppress("UnstableApiUsage")
  buildFeatures.apply {
    aidl = true
    viewBinding = false
  }
}

dependencies {
  api(projects.logger)

  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.robolectric)
  testImplementation(libs.tests.google.truth)
}
