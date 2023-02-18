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

//val signingKey = if (project.hasProperty("signingKey")) {
//    project.property("signingKey") as String?
//} else {
//    System.getenv("GPG_SIGNING_KEY")
//}
//val signingPassword = if (project.hasProperty("signingPassword")) {
//    project.property("signingPassword") as String?
//} else {
//    System.getenv("GPG_SIGNING_PASSWORD")
//}

val jpmsAsString: String? = System.getProperty("jpms")
var isJpms: Boolean? = null
if (jpmsAsString != null) {
    isJpms = jpmsAsString.toBoolean()
}

plugins {
    `maven-publish`
    signing
    id("net.researchgate.release")
}

repositories {
    mavenCentral()
}

subprojects {
    // uncomment for editing module-info.java
//    apply(plugin = "kolog.jpms-no-mobile-conventions")
    when (isJpms) {
        true -> apply(plugin = "kolog.jpms-no-mobile-conventions")
        false -> apply(plugin = "kolog.client-only-conventions")
        null -> apply(plugin = "kolog.dev-conventions")
    }
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    // --------------- publishing ---------------

    publishing {
        repositories {
            maven {
                if (project.version.toString().endsWith("SNAPSHOT")) {
                    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }

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
        // Require signing.keyId, signing.password and signing.secretKeyRingFile
        sign(publishing.publications)
//        useInMemoryPgpKeys(signingKey, signingPassword)
//        sign(publishing.publications)
    }
}

//fun CopySpec.setExecutablePermissions() {
//    filesMatching("gradlew") { mode = 0b111101101 }
//    filesMatching("gradlew.bat") { mode = 0b110100100 }
//}

// Workaround for project with modules https://github.com/researchgate/gradle-release/issues/144
tasks.register("releaseBuild") {
    dependsOn(subprojects.map { it.tasks.findByName("build") }.toTypedArray())
}

release {
    buildTasks.set(listOf("releaseBuild"))
}

// when version changes :
// -> execute ./gradlew wrapper, then delete .gradle directory, then execute ./gradlew wrapper again
tasks.wrapper {
    gradleVersion = "8.0"
    distributionType = Wrapper.DistributionType.ALL
}
