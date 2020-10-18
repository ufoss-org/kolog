rootProject.name = "kolog-build"

include("kolog")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
    val kotlinVersion: String by settings
    val dokkaPluginVersion: String by settings
    val releasePluginVersion: String by settings
    val androidLibraryPluginVersion: String by settings
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.library" -> useModule("com.android.tools.build:gradle:${androidLibraryPluginVersion}")
            }
        }
    }
    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("net.researchgate.release") version releasePluginVersion
    }
}
