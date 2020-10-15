pluginManagement {
    val kotlinVersion: String by settings
    val dokkaPluginVersion: String by settings
    val releasePluginVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("net.researchgate.release") version releasePluginVersion
    }
}

rootProject.name = "kolog-build"

include("kolog")
