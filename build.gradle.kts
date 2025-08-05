import org.gradle.tooling.GradleConnector

val ossrhUsername = if (project.hasProperty("ossrhUsername")) {
    project.property("ossrhUsername") as String?
} else {
    System.getenv("OSSRH_USERNAME")
}
val ossrhPassword = if (project.hasProperty("ossrhPassword")) {
    project.property("ossrhPassword") as String?
} else {
    System.getenv("OSSRH_PASSWORD")
}

val signingKey = if (project.hasProperty("signingKey")) {
    project.property("signingKey") as String?
} else {
    System.getenv("GPG_SIGNING_KEY")
}
val signingPassword = if (project.hasProperty("signingPassword")) {
    project.property("signingPassword") as String?
} else {
    System.getenv("GPG_SIGNING_PASSWORD")
}

plugins {
    `maven-publish`
    signing
    alias(libs.plugins.release)

    alias(libs.plugins.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    // --------------- publishing ---------------

    publishing {
        repositories {
            maven {
                name = "ossrh-staging-api"
                setUrl("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")

                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }


                publications.withType<MavenPublication> {
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

                        developers {
                            developer {
                                name.set("pull-vert")
                                url.set("https://github.com/pull-vert")
                            }
                        }

                        scm {
                            connection.set("scm:git:https://github.com/ufoss-org/kolog")
                            developerConnection.set("scm:git:git@github.com:ufoss-org/kolog.git")
                            url.set("https://github.com/ufoss-org/kolog")
                        }
                    }
                }
    }

    signing {
        // To publish to maven local, comment next line. Then it requires signing.keyId, signing.password and
        // signing.secretKeyRingFile
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

// workaround : https://github.com/researchgate/gradle-release/issues/304#issuecomment-1083692649
configure(listOf(tasks.release, tasks.runBuildTasks)) {
    configure {
        actions.clear()
        doLast {
            GradleConnector
                .newConnector()
                .forProjectDirectory(layout.projectDirectory.asFile)
                .connect()
                .use { projectConnection ->
                    val buildLauncher = projectConnection
                        .newBuild()
                        .forTasks(*tasks.toTypedArray())
                        .setStandardInput(System.`in`)
                        .setStandardOutput(System.out)
                        .setStandardError(System.err)
                    gradle.startParameter.excludedTaskNames.forEach {
                        buildLauncher.addArguments("-x", it)
                    }
                    buildLauncher.run()
                }
        }
    }
}

// when the Gradle version changes:
// -> execute ./gradlew wrapper, then remove .gradle directory, then execute ./gradlew wrapper again
tasks.wrapper {
    gradleVersion = "8.14.3"
    distributionType = Wrapper.DistributionType.ALL
}
