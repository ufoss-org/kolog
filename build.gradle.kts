import net.researchgate.release.GitAdapter

plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    id("org.jetbrains.dokka") apply false
    id("com.android.library") apply false
    id("net.researchgate.release")
    id("maven-publish")
}

subprojects {
    apply(plugin = "maven-publish")

    publishing {
        repositories {
            maven {
                val user = "ufoss"
                val repo = "ufoss"
                val name = "kolog"
                url = uri("https://api.bintray.com/maven/$user/$repo/$name/;publish=0")

                credentials {
                    username =
                            if (project.hasProperty("bintray_user")) project.property("bintray_user") as String? else System.getenv(
                                    "BINTRAY_USER"
                            )
                    password =
                            if (project.hasProperty("bintray_api_key")) project.property("bintray_api_key") as String? else System.getenv(
                                    "BINTRAY_API_KEY"
                            )
                }
            }
        }
    }

    repositories {
        google()
        jcenter()
    }
}

fun CopySpec.setExecutablePermissions() {
    filesMatching("gradlew") { mode = 0b111101101 }
    filesMatching("gradlew.bat") { mode = 0b110100100 }
}

// Workaround for project with modules https://github.com/researchgate/gradle-release/issues/144
tasks.withType<GradleBuild> {
    buildFile = file("build.gradle.kts")
    buildName = "kolog-build"
}

tasks.register("build") {
    dependsOn(subprojects.map { it.tasks.findByName("build") }.toTypedArray())
}

release {
    val git: GitAdapter.GitConfig = getProperty("git") as GitAdapter.GitConfig
    git.requireBranch = "main"
}

// when version changes :
// -> execute ./gradlew wrapper, then delete .gradle directory, then execute ./gradlew wrapper again
tasks.wrapper {
    gradleVersion = "6.7"
    distributionType = Wrapper.DistributionType.ALL
}
