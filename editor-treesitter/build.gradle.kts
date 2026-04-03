/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2023  Rosemoe
 *
 *     This library is free software...
 ******************************************************************************/

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "io.github.rosemoe.sora.ts"

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
    api(libs.common.editor)
    api(libs.common.kotlin.coroutines.android)
    api(libs.androidide.ts)
    api(libs.androidx.collection)

    api(projects.common)
    api(projects.editorApi)
    api(projects.logger)
}
