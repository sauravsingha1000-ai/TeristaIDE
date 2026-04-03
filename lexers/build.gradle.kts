/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 */

import com.itsaky.androidide.plugins.LexerGeneratorPlugin

plugins {
  id("java-library")
  kotlin("jvm")
}

apply {
  plugin(LexerGeneratorPlugin::class.java)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }

  sourceCompatibility = BuildConfig.javaVersion
  targetCompatibility = BuildConfig.javaVersion
}

dependencies {
  api(libs.common.antlr4.runtime)

  implementation(libs.common.jkotlin)

  testImplementation(libs.tests.junit)
  testImplementation(libs.tests.google.truth)
}
