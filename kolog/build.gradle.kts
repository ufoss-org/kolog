import org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION as KOTLIN_VERSION

val jpmsAsString: String? = System.getProperty("jpms")
var isJpms: Boolean? = null
if (jpmsAsString != null) {
    isJpms = jpmsAsString.toBoolean()
}

println("Using Gradle version: ${gradle.gradleVersion}")
println("Using Kotlin compiler version: $KOTLIN_VERSION")
println("Using Java compiler version: ${JavaVersion.current()}")
println("isJpms = $isJpms")

plugins {
    id("kolog-jpms-no-native") apply false
    id("kolog-dev") apply false
}

when (isJpms) {
    true -> apply(plugin = "kolog-jpms-no-native")
    else -> apply(plugin = "kolog-dev")
}
// uncomment for editing module-info.java
//apply(plugin = "kolog-jpms-no-native")
