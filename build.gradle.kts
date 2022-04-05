import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlin.serialization.gradle.plugin)
        classpath(libs.ktlint.gradle)
        classpath(libs.google.ksp)
        classpath(libs.google.dagger.hilt.plugin)
        classpath(libs.molecule.plugin)
    }
}

subprojects {

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    afterEvaluate {
        configurations.configureEach {
            // There could be transitive dependencies in tests with a lower version. This could cause
            // problems with a newer Kotlin version that we use.
            resolutionStrategy.force(libs.kotlin.reflect)
        }
    }

    // Configuration documentation: https://github.com/JLLeitschuh/ktlint-gradle#configuration
    configure<KtlintExtension> {
        // Prints the name of failed rules.
        verbose.set(true)
        reporters {
            // Default "plain" reporter is actually harder to read.
            reporter(ReporterType.JSON)
        }
    }

    extensions.findByType<JavaPluginExtension>()?.run {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }

    plugins.withType<com.android.build.gradle.BasePlugin>().configureEach {
        (project.extensions.getByName("android") as com.android.build.gradle.BaseExtension).run {
            compileSdkVersion(32)

            defaultConfig {
                minSdk = 25
                targetSdk = 32
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = false

            jvmTarget = "11"

            freeCompilerArgs += listOf(
                "-progressive",
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlin.ExperimentalStdlibApi",
            )
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
