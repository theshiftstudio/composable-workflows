plugins {
    id("com.android.library")
    kotlin("android")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

//apply(from = rootProject.file(".buildscript/configure-android-defaults.gradle"))

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
//    api(project(":workflow-core"))
    api(libs.squareup.workflow.core)
    api(project(":workflow:workflow-ui:container-android"))
//    api(project(":samples:containers:common"))
    api(project(":samples:workflow1-samples-containers:common"))


    api(libs.androidx.transition)
    api(libs.kotlin.jdk6)

    implementation(libs.squareup.workflow.runtime)
//    implementation(project(":workflow-runtime"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.savedstate)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    androidTestImplementation(libs.androidx.activity.core)
    androidTestImplementation(libs.androidx.compose.ui)
    androidTestImplementation(libs.kotlin.test.jdk)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.truth)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
