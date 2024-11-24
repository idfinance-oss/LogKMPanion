import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.multiplatform)
    alias(libs.plugins.org.jetbrains.cocoapods)
    alias(libs.plugins.org.jetbrains.serialization)
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.org.jetbrains.compose.compiler)
    alias(libs.plugins.io.realm.kotlin)
    alias(libs.plugins.maven.publish)
}

group = "io.github.idfinance-oss"
version = System.getenv("LIBRARY_VERSION") ?: libs.versions.pluginVersion.get()

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree = KotlinSourceSetTree.test
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.realm)
            implementation(libs.decomposeCompose)
            implementation(libs.decompose)
            implementation(libs.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.uuid)
        }
        androidMain.dependencies {
            implementation(libs.activity.compose)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        androidInstrumentedTest.dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.ui.test.junit4.android)
        }
    }

    cocoapods {
        ios.deploymentTarget = "12.0"
        summary = "LogKMPanion"
        homepage = "https://idfinance.com/"
        name = "LogKMPanion"

        framework {
            baseName = "LogKMPanion"
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
    namespace = "com.idfinance.logkmpanion"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    dependencies {
        debugImplementation(libs.ui.test.manifest)
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("LogKMPanion")
            description.set("This Kotlin Multiplatform library is designed to help you observe and manage application logs across Android and iOS platforms")
            url.set("https://github.com/idfinance-oss/LogKMPanion")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    id.set("idfinance-oss")
                    name.set("idfinance-oss")
                    email.set("github-idfinance-oss@idfinance.com")
                }
            }

            scm {
                url.set("https://github.com/idfinance-oss/LogKMPanion")
                connection.set("scm:git:git://github.com/idfinance-oss/LogKMPanion.git")
                developerConnection.set("scm:git:git://github.com/idfinance-oss/LogKMPanion.git")
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral(
        host = SonatypeHost.CENTRAL_PORTAL,
        automaticRelease = System.getenv("AUTO_PUBLISH") == "true"
    )
    signAllPublications()
}
