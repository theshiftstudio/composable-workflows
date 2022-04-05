plugins {
    id("com.android.application")

    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")

    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId = "com.shiftstudio.workflow.sample.hello"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    // <editor-fold desc="Common module with business logic">
    implementation(project(":samples:hello:common"))
    // </editor-fold>

    // <editor-fold desc="Coroutines">
    implementation(libs.kotlinx.coroutines.android)
    // </editor-fold>

    // <editor-fold desc="Hilt">
    api(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    // </editor-fold>

    // <editor-fold desc="AndroidX core">
    implementation(libs.androidx.core)
    coreLibraryDesugaring(libs.android.tools.desugar)

    // implementation(Dependencies.AndroidX.Lifecycle.ktx)
    // implementation(Dependencies.AndroidX.Lifecycle.viewModelKtx)
    // implementation(Dependencies.AndroidX.Lifecycle.viewModelCompose)
    // implementation(Dependencies.AndroidX.Lifecycle.viewModelSavedState)
    // </editor-fold>

    // <editor-fold desc="Compose">
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // implementation(Dependencies.AndroidX.Navigation.compose)
    // </editor-fold>

    // <editor-fold desc="Accompanist & Coil">
    // implementation(Dependencies.Accompanist.insets)
    // implementation(Dependencies.Accompanist.insetsUi)
    // implementation(Dependencies.Accompanist.systemUiController)
    // implementation(Dependencies.Accompanist.placeholder)
    // implementation(Dependencies.Accompanist.swipeRefresh)

    // implementation(Dependencies.Coil.coilCompose)
    // </editor-fold>

    // <editor-fold desc="Workflow">
    implementation(libs.squareup.workflow.ui.compose)
    implementation(libs.squareup.workflow.ui.compose.tooling)
    // </editor-fold>

    implementation(libs.timber)

    // <editor-fold desc="Testing">
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // </editor-fold>
}
