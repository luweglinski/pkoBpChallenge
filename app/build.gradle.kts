plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs.kotlin)
    kotlin("kapt")
}

android {
    namespace = "lw.pko.pkochallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "lw.pko.pkochallenge"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "TMDB_ACCESS_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1YTM1YjlkZjNiMTI3YjQ1YTc0ZGI3M2IyYzczYjg3ZSIsIm5iZiI6MTcyNDE2MTkwMS43NjU3MTksInN1YiI6IjY2YzNiMTg3M2E2NDllZjE5YjFhYjE0OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.odxQi1Anod0jJJCWQv-XOHrklwe4Ct1uX8yt2d72jiw\"")
        buildConfigField("String", "TMDB_URL", "\"https://api.themoviedb.org/3/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":modules:core:theme"))
    implementation(project(":modules:movie:presentation"))
    implementation(project(":modules:movie:data"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt)

    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.mockitoAndroid)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}