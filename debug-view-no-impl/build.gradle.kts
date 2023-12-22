plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("maven-publish")
}

group = "com.idfinance.kmm"
version = System.getenv("LIBRARY_VERSION") ?: libs.versions.pluginVersion.get()

kotlin {
    androidTarget { publishLibraryVariants("release", "debug") }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    cocoapods {
        summary = "Debug View No Impl"
        ios.deploymentTarget = "12.0"
        homepage = "https://idfinance.com/"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "DebugViewNoImpl"
        }
    }
}

android {
    namespace = "com.idfinance.kmm.debug.view"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

publishing {
    publications {
        matching {
            return@matching it.name in listOf("iosArm64", "iosX64", "kotlinMultiplatform")
        }.all {
            val targetPublication = this@all
            tasks.withType<AbstractPublishToMaven>()
                .matching { it.publication == targetPublication }
                .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
        }
    }
    repositories {
        maven {
            name = "kmm-debug-view"
            url = uri("https://maven.pkg.github.com/idfmoneyman-mx/kmm-debug-view")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_API_KEY")
            }
        }
    }
}
