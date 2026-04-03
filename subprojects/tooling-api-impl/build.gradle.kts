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
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

tasks.withType<Jar> {
  manifest {
    attributes("Main-Class" to "${BuildConfig.packageName}.tooling.impl.Main")
  }
}

tasks.register("deleteExistingJarFiles") {
  delete(project.layout.buildDirectory.dir("libs"))
}

tasks.register("copyJar") {
  doLast {
    val libsDir = project.layout.buildDirectory.dir("libs")

    copy {
      from(libsDir)
      into(libsDir)
      include("*-all.jar")
      rename { "tooling-api-all.jar" }
    }
  }
}

tasks.named("jar") {
  dependsOn("deleteExistingJarFiles")
  finalizedBy("shadowJar")
}

tasks.named("shadowJar") {
  finalizedBy("copyJar")
}

dependencies {
  api(projects.subprojects.toolingApi)

  implementation(projects.buildInfo)
  implementation(projects.shared)

  implementation(libs.common.jkotlin)
  implementation(libs.xml.xercesImpl)
  implementation(libs.xml.apis)
  implementation(libs.tooling.gradleApi)

  testImplementation(projects.testing.tooling)

  runtimeOnly(libs.tooling.slf4j)
}
