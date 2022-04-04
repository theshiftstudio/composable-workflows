plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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

    api(project(":workflow:workflow-composable"))

    // <editor-fold desc="Coroutines">
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.rx2)
    // </editor-fold>

    // <editor-fold desc="Compose">
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.compose.runtime.rx2)
    // </editor-fold>

    implementation(project(":samples:workflow1-samples-containers:common"))
    implementation(project(":workflow:workflow-ui:container-common"))
    implementation(libs.squareup.workflow.core)
    implementation(libs.squareup.workflow.rx2)

    // <editor-fold desc="Testing">
    testImplementation(libs.junit)

    testImplementation(libs.kotlin.test.jdk)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.molecule.testing)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    // </editor-fold>
}