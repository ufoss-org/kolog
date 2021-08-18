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
    val androidGradlePlugin: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        id("com.android.library") version androidGradlePlugin
        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("net.researchgate.release") version releasePluginVersion
    }
}
