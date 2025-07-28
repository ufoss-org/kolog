import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2
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
        languageVersion = KOTLIN_2_2
        apiVersion = KOTLIN_2_2
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
        iosSimulatorArm64(),
        macosArm64(),
        macosX64()
    ).forEach {
        it.binaries.framework {
            baseName = "kolog"
        }
    }

    listOf(
        linuxArm64(),
        linuxX64()
    ).forEach {
        it.binaries.sharedLib {
            baseName = "kolog"
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${catalogVersion("kotlinx-coroutines")}")
            }
        }

        val jvmTest by getting {
            dependencies {
                // import BOM
                implementation(project.dependencies.platform("org.junit:junit-bom:${catalogVersion("junit")}"))

                implementation("org.junit.jupiter:junit-jupiter-api")

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
                runtimeOnly("org.slf4j:slf4j-jdk-platform-logging:${catalogVersion("slf4j")}")
                runtimeOnly("ch.qos.logback:logback-classic:${catalogVersion("logback")}")
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation("androidx.test:runner:${catalogVersion("androidx")}")
                implementation("org.robolectric:robolectric:${catalogVersion("robolectric")}")
            }
        }

        val apple by creating {
            dependsOn(commonMain.get())
        }
        iosArm64Main.get().dependsOn(apple)
        iosX64Main.get().dependsOn(apple)
        iosSimulatorArm64Main.get().dependsOn(apple)
        macosArm64Main.get().dependsOn(apple)
        macosX64Main.get().dependsOn(apple)

        val linux by creating {
            dependsOn(commonMain.get())
        }
        linuxArm64Main.get().dependsOn(linux)
        linuxX64Main.get().dependsOn(linux)
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
