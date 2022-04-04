rootProject.name = "composable-workflows"

enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

plugins {
    id("com.gradle.enterprise") version "3.8.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

include(
    ":workflow:workflow-composable",

    // We included a bunch of code from @square/workflow-kotlin repo to play with the upcoming UI
    // overhaul from ray/ui-update branch (https://github.com/square/workflow-kotlin/pull/591)
    ":workflow:workflow-ui:core-common",
    ":workflow:workflow-ui:core-android",
    ":workflow:workflow-ui:container-common",
    ":workflow:workflow-ui:container-android",
    ":workflow:workflow-ui:compose",

    ":samples:workflow1-samples-containers:android",
    ":samples:workflow1-samples-containers:common",

    ":samples:tictactoe:app",
    ":samples:tictactoe:common",
)
