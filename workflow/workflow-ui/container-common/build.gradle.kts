plugins {
    `java-library`
    kotlin("jvm")
//  id("org.jetbrains.dokka")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

//apply(from = rootProject.file(".buildscript/configure-maven-publish.gradle"))

dependencies {
    api(project(":workflow:workflow-ui:core-common"))
    api(libs.kotlin.jdk6)
    api(libs.squareup.okio)

    testImplementation(libs.kotlin.test.jdk)
    testImplementation(libs.truth)
}
