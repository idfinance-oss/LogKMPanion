import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.multiplatform)
    alias(libs.plugins.org.jetbrains.cocoapods)
    alias(libs.plugins.maven.publish)
}

group = "io.github.idfinance-oss"
version = System.getenv("LIBRARY_VERSION") ?: libs.versions.pluginVersion.get()

kotlin {
    androidTarget { publishLibraryVariants("release", "debug") }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    applyDefaultHierarchyTemplate()

    jvmToolchain(17)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
        }
    }

    cocoapods {
        summary = "LogKMPanion No Impl"
        ios.deploymentTarget = "12.0"
        homepage = "https://idfinance.com/"

        framework {
            baseName = "DebugViewNoImpl"
        }
    }
}

android {
    namespace = "com.idfinance.logkmpanion"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("LogKMPanion-no-impl")
            description.set("This Kotlin Multiplatform library is designed to help you observe and manage application logs across Android and iOS platforms. This is module with empty implementation")
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
