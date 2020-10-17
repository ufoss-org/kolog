rootProject.name = "kolog-build"

include("kolog")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        //jcenter()
    }

    val kotlinVersion: String by settings
    val dokkaPluginVersion: String by settings
    val releasePluginVersion: String by settings
    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("net.researchgate.release") version releasePluginVersion
        id("com.android.library") version "3.6.1"
    }
}
