import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm") apply false
    id("org.jetbrains.dokka") apply false
    id("net.researchgate.release")
    id("java-library")
    id("maven-publish")
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.dokka")
        plugin("maven-publish")
    }

    java.sourceCompatibility = JavaVersion.VERSION_1_8

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

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjvm-default=enable", "-Xexplicit-api=strict", "-Xinline-classes", "-Xuse-experimental=kotlin.contracts.ExperimentalContracts")
            jvmTarget = "1.8"
        }
    }

    val compileTestKotlin: KotlinCompile by tasks

    compileTestKotlin.kotlinOptions {
        freeCompilerArgs = listOf("-Xjvm-default=enable")
        jvmTarget = "1.8"
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
        testLogging {
            //events 'passed', 'failed', 'skipped'
            showStandardStreams = true
        }
    }

// --------------- Source & Javadoc artefacts + publishing ---------------

    // generate xxx-sources.jar
    java {
        withSourcesJar()
    }

    val dokkaJar by tasks.creating(Jar::class) {
        dependsOn("dokkaHtml")
        archiveClassifier.set("javadoc")
        from(buildDir.resolve("dokka/html"))
    }

    tasks.withType<DokkaTask>().configureEach {
        //outputDirectory.set(file("$buildDir/javadoc"))

        dokkaSourceSets {
            named("main") {
                configureEach {
                    jdkVersion.set(8)
                }
            }
        }
    }

    publishing {
        repositories {
            maven {
                val user = "ufoss"
                val repo = "ufoss"
                val name = "kotlin-slf4j"
                url = uri("https://api.bintray.com/maven/$user/$repo/$name/;publish=0")

                credentials {
                    username = if (project.hasProperty("bintray_user")) project.property("bintray_user") as String? else System.getenv("BINTRAY_USER")
                    password = if (project.hasProperty("bintray_api_key")) project.property("bintray_api_key") as String? else System.getenv("BINTRAY_API_KEY")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                artifactId = "project.name"
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
    gradleVersion = "6.6.1"
    distributionType = Wrapper.DistributionType.ALL
}
