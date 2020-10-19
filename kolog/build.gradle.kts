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
                jvmTarget = "1.8"
            }
        }

        val test by compilations.getting {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjvm-default=all")
                jvmTarget = "1.8"
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
            val kotlinxCoroutinesVersion: String by project
            val atriumVersion: String by project
            dependencies {
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("reflect"))

                implementation("ch.tutteli.atrium:atrium-fluent-en_GB:$atriumVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
            }
        }

        val jvmMain by getting {
            val slf4jVersion: String by project
            dependencies {
                api("org.slf4j:slf4j-api:$slf4jVersion")
            }
        }

        val jvmTest by getting {
            val junitVersion: String by project
            val logbackVersion: String by project
            dependencies {
                implementation(kotlin("test-junit5"))

                implementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
                runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }

        val androidMain by getting

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("androidx.test:runner:1.3.0")
                implementation("org.robolectric:robolectric:4.4")
            }
        }

        val iosMain by getting
    }

    configure(targets) {
        mavenPublication {
            pom {
                description.set("kolog duty is to be the idiomatic way to log in Kotlin")
                url.set("https://github.com/ufoss-org/kolog")
                licenses {
                    license {
                        name.set("The Unlicence")
                        url.set("https://unlicense.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ufoss-org/kolog.git")
                    url.set("https://github.com/ufoss-org/kolog.git")
                }
            }
        }
    }

    sourceSets.all {
        languageSettings.apply {
            languageVersion = "1.4"
            apiVersion = "1.4"
            enableLanguageFeature("InlineClasses")
            useExperimentalAnnotation("kotlin.contracts.ExperimentalContracts")
            useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
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

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("jvmMain") {
            configureEach {
                jdkVersion.set(8)
            }
        }
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
