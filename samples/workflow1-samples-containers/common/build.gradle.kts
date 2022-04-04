plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
//  implementation(project(":workflow-ui:container-common"))
    implementation(project(":workflow:workflow-ui:container-common"))
//  implementation(project(":workflow-ui:core-android"))
    implementation(project(":workflow:workflow-ui:core-common"))
//  implementation(project(":workflow-core"))
    implementation(libs.squareup.workflow.core)

    implementation(libs.kotlin.jdk6)

    testImplementation(libs.kotlin.test.jdk)
    testImplementation(libs.hamcrest)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
//    testImplementation(project(":workflow-testing"))
    testImplementation(libs.squareup.workflow.testing)
}
