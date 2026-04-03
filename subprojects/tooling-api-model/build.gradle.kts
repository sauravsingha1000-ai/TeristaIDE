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
  implementation(projects.logger)

  api(projects.subprojects.xmlDom)
  api(projects.subprojects.builderModelImpl)
  api(libs.common.jsonrpc)

  implementation(libs.common.jkotlin)
}

tasks.register<Copy>("copyToTestDir") {
  from(project.layout.buildDirectory.file("libs/tooling-api-model.jar"))
  into(project.rootProject.mkdir("tests/test-home/.androidide/init"))
  rename { "model.jar" }

  outputs.upToDateWhen { false }
}

tasks.named<Jar>("jar") {
  finalizedBy("copyToTestDir")
  outputs.upToDateWhen { false }
}
