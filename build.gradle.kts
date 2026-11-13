plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "uz.jarvis.assistant"
    compileSdk = 35

    defaultConfig {
        applicationId = "uz.jarvis.assistant"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
