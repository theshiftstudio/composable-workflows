plugins {
    id("com.android.library")

    kotlin("android")
    id("kotlin-parcelize")
}

android {

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    // <editor-fold desc="Composable Workflow">
    api(project(":workflow:workflow-composable"))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.saveable)
    // </editor-fold>

    // <editor-fold desc="Workflow">
    // api(libs.squareup.workflow.core)
    // </editor-fold>

    // <editor-fold desc="AndroidX core">
    coreLibraryDesugaring(libs.android.tools.desugar)
    // </editor-fold>

    // <editor-fold desc="Hilt">
    api(libs.google.dagger.hilt.core)
    // </editor-fold>

    // <editor-fold desc="Coroutines">
    api(libs.kotlinx.coroutines.core)
    // </editor-fold>

    implementation(libs.timber)

    // <editor-fold desc="Testing">
    testImplementation(libs.junit)

    testImplementation(libs.kotlin.test.jdk)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.molecule.testing)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    // </editor-fold>
}
