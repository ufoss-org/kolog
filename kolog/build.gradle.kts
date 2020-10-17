import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    //id("com.android.library")
    //id("kotlin-android-extensions")
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

    sourceSets {
        val commonMain by getting

        val commonTest by getting {
            val kotlinxCoroutinesVersion: String by project
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("reflect"))

                implementation("ch.tutteli.atrium:atrium-fluent-en_GB:0.13.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
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

        /*val androidMain by getting

        val androidTest by getting {
            dependencies {
                implementation("androidx.test:runner:1.2.0")
                implementation("org.robolectric:robolectric:4.3.1")
            }
        }*/
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

/*
android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}*/
