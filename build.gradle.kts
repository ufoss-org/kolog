import net.researchgate.release.GitAdapter

val ossrhUsername = if (project.hasProperty("ossrhUsername")) {
    project.property("ossrhUsername") as String?
} else {
    System.getenv("OSSRH_USERNAME")
}
val ossrhPassword = if (project.hasProperty("ossrhPassword")) {
    project.property("bintray_api_key") as String?
} else {
    System.getenv("OSSRH_PASSWORD")
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    id("org.jetbrains.dokka") apply false
    id("com.android.library") apply false
    `maven-publish`
    signing
    id("net.researchgate.release")
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        google()
        mavenCentral()
        jcenter() // todo remove me when kotlinx-html is on maven central
    }

    publishing {
        repositories {
            maven {
                if (project.version.toString().endsWith("SNAPSHOT")) {
                    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots")
                } else {
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
                }

                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    signing {
        // Require signing.keyId, signing.password and signing.secretKeyRingFile
        //useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}

fun CopySpec.setExecutablePermissions() {
    filesMatching("gradlew") { mode = 0b111101101 }
    filesMatching("gradlew.bat") { mode = 0b110100100 }
}

/*tasks.withType<GradleBuild> {
    buildFile = file("build.gradle.kts")
    buildName = "kolog-build"
}*/

// Workaround for project with modules https://github.com/researchgate/gradle-release/issues/144
tasks.register("releaseBuild") {
    dependsOn(subprojects.map { it.tasks.findByName("build") }.toTypedArray())
}

release {
    buildTasks = listOf("releaseBuild")
    val git: GitAdapter.GitConfig = getProperty("git") as GitAdapter.GitConfig
    git.requireBranch = "main"
}

// when version changes :
// -> execute ./gradlew wrapper, then delete .gradle directory, then execute ./gradlew wrapper again
tasks.wrapper {
    gradleVersion = "7.0"
    distributionType = Wrapper.DistributionType.ALL
}
