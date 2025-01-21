import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import kotlin.jvm.optionals.getOrNull

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    `maven-publish`
}

val versionCatalog: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun catalogVersion(lib: String) =
    versionCatalog.findVersion(lib).getOrNull()?.requiredVersion
        ?: throw GradleException("Version '$lib' is not specified in the toml version catalog")

val javaVersion = catalogVersion("java").toInt()

kotlin {
    compilerOptions {
        languageVersion = KOTLIN_2_1
        apiVersion = KOTLIN_2_1
        allWarningsAsErrors = true
        explicitApi = Strict
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-opt-in=kotlin.contracts.ExperimentalContracts",
        )
        progressiveMode = true
    }

    jvmToolchain(javaVersion)

    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
            testLogging {
                events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                showStandardStreams = true
            }
        }
    }

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "kolog"
        }
    }

    sourceSets {
        val commonMain by getting

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${catalogVersion("kotlinx-coroutines")}")
            }
        }

        val jvmMain by getting
        
        val jvmTest by getting {
            dependencies {
                // import BOM
                implementation(platform("org.junit:junit-bom:${catalogVersion("junit")}"))

                implementation("org.junit.jupiter:junit-jupiter-api")

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
                runtimeOnly("org.slf4j:slf4j-jdk-platform-logging:${catalogVersion("slf4j")}")
                runtimeOnly("ch.qos.logback:logback-classic:${catalogVersion("logback")}")
            }
        }

        val androidMain by getting

        val androidUnitTest by getting {
            dependencies {
                implementation("androidx.test:runner:${catalogVersion("androidx")}")
                implementation("org.robolectric:robolectric:${catalogVersion("robolectric")}")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "org.ufoss." + project.name

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    
    compileSdk = (catalogVersion("android-compile-sdk")).toInt()

    defaultConfig {
        minSdk = (catalogVersion("android-min-sdk")).toInt()
        targetSdk = (catalogVersion("android-target-sdk")).toInt()
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }
}
