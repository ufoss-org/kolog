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
    id("kolog-jvm") apply false
    id("kolog-no-jvm") apply false
}

when (isJpms) {
    true -> apply(plugin = "kolog-jvm")
    else -> apply(plugin = "kolog-no-jvm")
}
