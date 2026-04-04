plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "${BuildConfig.packageName}.javac.services"

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
    implementation(libs.common.utilcode)
    implementation(libs.google.guava)
    implementation(projects.common)
    implementation(projects.logger)

    api(projects.subprojects.javac)

    testImplementation(libs.tests.junit)
    testImplementation(libs.tests.google.truth)
    testImplementation(libs.tests.robolectric)
}
