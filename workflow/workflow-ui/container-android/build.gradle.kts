plugins {
    id("com.android.library")
    kotlin("android")
//    id("org.jetbrains.dokka")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

//apply(from = rootProject.file(".buildscript/configure-maven-publish.gradle"))
//apply(from = rootProject.file(".buildscript/configure-android-defaults.gradle"))
//apply(from = rootProject.file(".buildscript/android-ui-tests.gradle"))

dependencies {
//  api(project(":workflow-core"))
    api(libs.squareup.workflow.core)
//    api(project(":workflow-ui:core-android"))
    api(project(":workflow:workflow-ui:core-android"))
//    api(project(":workflow-ui:container-common"))
    api(project(":workflow:workflow-ui:container-common"))

    api(libs.androidx.transition)
    api(libs.kotlin.jdk6)

    implementation(libs.squareup.workflow.runtime)
//    implementation(project(":workflow-runtime"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.core)
    implementation(libs.androidx.fragment.core)
    implementation(libs.androidx.savedstate)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlin.test.jdk)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.truth)
}
