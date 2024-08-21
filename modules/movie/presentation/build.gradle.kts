plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs.kotlin)
    kotlin("kapt")
}

android {
    namespace = "lw.pko.pkochallenge.movie.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":modules:core:theme"))
    implementation(project(":modules:movie:domain"))
    implementation(project(":modules:core:network"))
    implementation(libs.paging.runtime)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.room.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.paging.compose.android)
    implementation(libs.material.icons.extended)

    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}