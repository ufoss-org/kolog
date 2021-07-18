import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}


println("Using Gradle version: ${gradle.gradleVersion}")
println("Using Kotlin compiler version: ${KotlinCompilerVersion.VERSION}")

kotlin {
    explicitApi()

    jvm {
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

    ios {
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
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("reflect"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${findProperty("kotlinx.coroutines.version")}")
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.slf4j:slf4j-api:${findProperty("slf4j.version")}")
            }
        }

        val jvmTest by getting {
            val junitVersion: String by project
            val logbackVersion: String by project
            dependencies {
                implementation(kotlin("test-junit5"))

                implementation("org.junit.jupiter:junit-jupiter-api:${findProperty("junit.version")}")

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:${findProperty("junit.version")}")
                runtimeOnly("ch.qos.logback:logback-classic:${findProperty("logback.version")}")
            }
        }

        val androidMain by getting

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
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


tasks.getByName<Test>("jvmTest") {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.STARTED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(15)
    }

    val main by sourceSets.getting {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
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
