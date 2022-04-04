plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

//    id("app.cash.molecule")
}

android {
    defaultConfig {
        applicationId = "com.shiftstudio.workflow.sample.tictactoe"
        versionCode = 1
        versionName = "1.0"

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
        viewBinding = true
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

dependencies {

    implementation(project(":samples:tictactoe:common"))

    implementation(project(":samples:workflow1-samples-containers:android"))

    implementation(project(":workflow:workflow-ui:core-android"))

    implementation(project(":workflow:workflow-ui:compose"))

    // implementation(libs.squareup.workflow.ui.compose) {
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-core-android")
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-core-common-jvm")
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-container-android")
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-container-common-jvm")
    // }
    // implementation(libs.squareup.workflow.ui.compose.tooling) {
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-core-android")
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-core-common-jvm")
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-container-android")
    //     exclude(group = "com.squareup.workflow1", module = "workflow-ui-container-common-jvm")
    // }

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.rxjava2.rxandroid)
    implementation(libs.timber)

    // implementation(libs.molecule.runtime.android)

    implementation(libs.androidx.core)

    implementation(libs.google.android.material)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.savedstate.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.lifecycle.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    implementation(libs.androidx.test.espresso.idlingResource)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}