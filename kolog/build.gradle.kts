import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

println("Using Gradle version: ${gradle.gradleVersion}")
println("Using Kotlin compiler version: ${KotlinCompilerVersion.VERSION}")

val slf4jVersion: String by project
val assertjVersion: String by project
val junitVersion: String by project
val kotlinxCoroutinesVersion: String by project
val logbackVersion: String by project

dependencies {
    api("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf(
        "-Xjvm-default=all",
        "-Xexplicit-api=strict",
        "-Xinline-classes",
        "-Xuse-experimental=kotlin.contracts.ExperimentalContracts"
    )
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xjvm-default=all")
    jvmTarget = "1.8"
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.STARTED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}

// --------------- Source & Javadoc artefacts + publishing ---------------

// generate xxx-sources.jar
java {
    withSourcesJar()
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            configureEach {
                jdkVersion.set(8)
            }
        }
    }
}

val dokkaJar = tasks.create<Jar>("dokkaJar") {
    dependsOn("dokkaHtml")
    archiveClassifier.set("javadoc")
    from(buildDir.resolve("dokka/html"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name
            from(components["java"])
            artifact(dokkaJar)
            pom {
                name.set(project.name)
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
}
