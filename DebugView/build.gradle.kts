plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("io.realm.kotlin") version "1.11.0"
    id("maven-publish")
}

group = "com.idfinance.kmm"
version = "0.0.9"

kotlin {
    androidTarget { publishLibraryVariants("release", "debug") }
    ios()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.realm)
                implementation(libs.decomposeCompose)
                implementation(libs.decompose)
                implementation(libs.datetime)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.activity.compose)
            }
        }
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }

    cocoapods {
        ios.deploymentTarget = "12.0"
        summary = "Debug View"
        homepage = "https://idfinance.com/"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "DebugView"
        }
    }
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
    namespace = "com.idfinance.kmm.debugview"
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
            url = uri("https://maven.pkg.github.com/sergei-mikhailovskii-idf/kmm-debug-view")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_API_KEY")
            }
        }
    }
}
