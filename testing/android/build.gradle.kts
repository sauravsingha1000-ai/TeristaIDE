/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 */

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("com.android.library")
  kotlin("android")
}

android {
  namespace = "${BuildConfig.packageName}.testing.android"

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
  api(libs.tests.androidx.test.core)
  api(libs.tests.androidx.test.runner)
  api(libs.tests.androidx.test.rules)
  api(libs.tests.androidx.junit)
  api(libs.tests.androidx.espresso.core)
  api(libs.tests.androidx.espresso.contrib)
  api(libs.tests.androidx.uiautomator)
  api(libs.tests.junit)
  api(libs.tests.google.truth)
  api(libs.tests.barista) {
    exclude("org.jetbrains.kotlin")
  }

  api(projects.buildInfo)
  api(projects.common)
  api(projects.shared)
}
