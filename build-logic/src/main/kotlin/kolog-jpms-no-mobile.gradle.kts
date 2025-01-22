import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.jvm.optionals.getOrNull

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka-javadoc")
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
    }

    jvmToolchain(javaVersion)

    jvm {
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
                exceptionFormat = TestExceptionFormat.FULL
                showStandardStreams = true
            }
        }

        // Generate javadoc jar for Java and Kotlin code in jvm artefacts.
        val dokkaJavadocJar by tasks.registering(Jar::class) {
            description = "A Javadoc JAR containing Dokka Javadoc for Java and Kotlin"
            from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
            archiveClassifier.set("javadoc")
        }

        mavenPublication {
            artifact(dokkaJavadocJar)
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
    }
}
