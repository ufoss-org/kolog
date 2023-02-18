pluginManagement {
    val releasePluginVersion: String by settings
    plugins {
        id("net.researchgate.release") version releasePluginVersion
    }
}

rootProject.name = "kolog-build"

val jpmsAsString: String? = System.getProperty("jpms")
var isJpms: Boolean? = null
if (jpmsAsString != null) {
    isJpms = jpmsAsString.toBoolean()
}
println("isJpms = $isJpms")

include("kolog")
