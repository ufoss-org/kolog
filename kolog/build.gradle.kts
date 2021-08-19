import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
}


println("Using Gradle version: ${gradle.gradleVersion}")
println("Using Kotlin compiler version: ${KotlinCompilerVersion.VERSION}")

kotlin {
    explicitApi()

    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }

        val main by compilations.getting {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjvm-default=all")
            }
        }

        val test by compilations.getting {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjvm-default=all")
            }
        }

        // Generate and add Javadoc jar in kolog-jvm
        val dokkaJar = tasks.create<Jar>("dokkaJar") {
            dependsOn("dokkaHtml")
            archiveClassifier.set("javadoc")
            from(buildDir.resolve("dokka/html"))
        }

        mavenPublication {
            artifact(dokkaJar)
        }
    }

    android {
        publishAllLibraryVariants()
    }

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "kolog"
            }
        }
    }

    sourceSets {
        val commonMain by getting

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${findProperty("kotlinx.coroutines.version")}")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.slf4j:slf4j-api:${findProperty("slf4j.version")}")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:${findProperty("junit.version")}")

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:${findProperty("junit.version")}")
                runtimeOnly("ch.qos.logback:logback-classic:${findProperty("logback.version")}")
            }
        }

        val androidMain by getting

        val androidTest by getting {
            dependencies {
                implementation("androidx.test:runner:1.4.0")
                implementation("org.robolectric:robolectric:4.4")
            }
        }

        val iosMain by getting
    }

    sourceSets.all {
        languageSettings.apply {
            languageVersion = "1.5"
            apiVersion = "1.5"
            useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            progressiveMode = true
        }
    }
}

tasks.withType<Test> {
    testLogging {
        events = setOf(TestLogEvent.STARTED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("jvmMain") {
            configureEach {
                jdkVersion.set(8)
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

val packForXcode by tasks.creating(Sync::class) {
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    val targetDir = File(buildDir, "xcode-frameworks")

    group = "build"
    dependsOn(framework.linkTask)
    inputs.property("mode", mode)

    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
