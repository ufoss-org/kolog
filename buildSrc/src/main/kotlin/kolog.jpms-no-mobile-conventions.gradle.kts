import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        // For JPMS module-info.java
        withJava()
        
        val compileKotlinJvm: KotlinCompile by tasks
        val compileJava: JavaCompile by tasks
        // replace '-' with '.' to match JPMS jigsaw module name
        val jpmsName = project.name.replace('-', '.')
        // this is needed because we have a separate compile step in this example with the 'module-info.java' is in
        // 'main/java' and the Kotlin code is in 'main/kotlin'
        compileJava.options.compilerArgs =
            listOf(
//            "--module-path",
//            compileJava.classpath.asPath,
            "--patch-module",
            "$jpmsName=${compileKotlinJvm.destinationDirectory.get().asFile.path}"
        )

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
            testLogging {
                events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                showStandardStreams = true
            }
        }

        // Generate and add Javadoc jar in jvm artefacts
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
        all {
            languageSettings.apply {
                languageVersion = "1.8"
                apiVersion = "1.8"
                optIn("kotlin.contracts.ExperimentalContracts")
                optIn("kotlin.time.ExperimentalTime")
                progressiveMode = true
            }
        }

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
                // import BOM
                implementation(platform("org.junit:junit-bom:${findProperty("junitVersion")}"))

                implementation("org.junit.jupiter:junit-jupiter-api")

                runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
                runtimeOnly("ch.qos.logback:logback-classic:${findProperty("logback.version")}")
            }
        }
    }
}
