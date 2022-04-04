plugins {
    `java-library`
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

//apply(from = rootProject.file(".buildscript/configure-maven-publish.gradle"))

dependencies {
    api(libs.kotlin.jdk6)
    api(libs.squareup.okio)
    api(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlin.test.jdk)
    testImplementation(libs.truth)
}
