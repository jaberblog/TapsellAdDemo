plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ir.jaber.tapselladdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "ir.jaber.tapselladdemo"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

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
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Tapsell Plus SDK
    implementation("ir.tapsell.plus:tapsell-plus-sdk-android:2.3.2")
    // Required by AdMobMediaView / NativeAdView used in the native ad layout
    implementation("com.google.android.gms:play-services-ads:22.6.0")
}
