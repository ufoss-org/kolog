import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2
import kotlin.jvm.optionals.getOrNull

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    `maven-publish`
}

val versionCatalog: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun catalogVersion(lib: String) =
    versionCatalog.findVersion(lib).getOrNull()?.requiredVersion
        ?: throw GradleException("Version '$lib' is not specified in the toml version catalog")

val javaVersion = catalogVersion("java").toInt()

kotlin {
    compilerOptions {
        languageVersion = KOTLIN_2_2
        apiVersion = KOTLIN_2_2
        allWarningsAsErrors = true
        explicitApi = Strict
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-opt-in=kotlin.contracts.ExperimentalContracts",
        )
        progressiveMode = true
    }

    jvmToolchain(javaVersion)

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosArm64(),
        macosX64()
    ).forEach {
        it.binaries.framework {
            baseName = "kolog"
        }
    }

    listOf(
        linuxArm64(),
        linuxX64()
    ).forEach {
        it.binaries.sharedLib {
            baseName = "kolog"
        }
    }

    sourceSets {
        val commonMain by getting

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${catalogVersion("kotlinx-coroutines")}")
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation("androidx.test:runner:${catalogVersion("androidx")}")
                implementation("org.robolectric:robolectric:${catalogVersion("robolectric")}")
            }
        }

        val appleMain by creating {
            dependsOn(commonMain)
        }
        iosArm64Main.get().dependsOn(appleMain)
        iosX64Main.get().dependsOn(appleMain)
        iosSimulatorArm64Main.get().dependsOn(appleMain)
        macosArm64Main.get().dependsOn(appleMain)
        macosX64Main.get().dependsOn(appleMain)

        val linuxMain by creating {
            dependsOn(commonMain)
        }
        linuxArm64Main.get().dependsOn(linuxMain)
        linuxX64Main.get().dependsOn(linuxMain)

        val linuxTest by creating {
            dependsOn(commonTest)
        }
        linuxArm64Test.get().dependsOn(linuxTest)
        linuxX64Test.get().dependsOn(linuxTest)
    }
}

android {
    namespace = "org.ufoss." + project.name

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }

    compileSdk = (catalogVersion("android-compile-sdk")).toInt()

    defaultConfig {
        minSdk = (catalogVersion("android-min-sdk")).toInt()
        targetSdk = (catalogVersion("android-target-sdk")).toInt()
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    publishing {
        multipleVariants {
            // Publishes all build variants with "default" component, see afterEvaluate publishing below
            allVariants()
            withSourcesJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("allVariants") {
                artifactId = project.name + "-android"
                from(components["default"])
            }
        }
    }
}
