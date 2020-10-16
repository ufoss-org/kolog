import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    id("org.jetbrains.dokka") apply false
    id("net.researchgate.release")
    `java-library`
    `maven-publish`
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    repositories {
        jcenter()
    }

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

    val compileKotlin: KotlinCompile by tasks
    compileKotlin.kotlinOptions {
        freeCompilerArgs = listOf("-Xjvm-default=enable", "-Xexplicit-api=strict", "-Xinline-classes", "-Xuse-experimental=kotlin.contracts.ExperimentalContracts")
        jvmTarget = "1.8"
    }

    val compileTestKotlin: KotlinCompile by tasks
    compileTestKotlin.kotlinOptions {
        freeCompilerArgs = listOf("-Xjvm-default=enable")
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
        repositories {
            maven {
                val user = "ufoss"
                val repo = "ufoss"
                val name = "kolog"
                url = uri("https://api.bintray.com/maven/$user/$repo/$name/;publish=0")

                credentials {
                    username = if (project.hasProperty("bintray_user")) project.property("bintray_user") as String? else System.getenv("BINTRAY_USER")
                    password = if (project.hasProperty("bintray_api_key")) project.property("bintray_api_key") as String? else System.getenv("BINTRAY_API_KEY")
                }
            }
        }

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
}

// Workaround for project with modules https://github.com/researchgate/gradle-release/issues/144
tasks.replace("build").dependsOn(subprojects.map { it.tasks.findByName("build") }/*.toTypedArray()*/)

// when version changes :
// -> execute ./gradlew wrapper, then delete .gradle directory, then execute ./gradlew wrapper again
tasks.wrapper {
    gradleVersion = "6.7"
    distributionType = Wrapper.DistributionType.ALL
}
