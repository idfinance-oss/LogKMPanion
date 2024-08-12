plugins {
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.compose) apply false
    alias(libs.plugins.org.jetbrains.compose.compiler) apply false
    alias(libs.plugins.org.jetbrains.multiplatform) apply false
    alias(libs.plugins.org.jetbrains.cocoapods) apply false
    alias(libs.plugins.org.jetbrains.serialization) apply false
    alias(libs.plugins.io.realm.kotlin) apply false
    alias(libs.plugins.maven.publish) apply false
}

tasks.register("buildAndPublish", DefaultTask::class) {
    dependsOn(":logkmpanion:build")
    dependsOn(":logkmpanion:publish")
    dependsOn(":logkmpanion-no-impl:build")
    dependsOn(":logkmpanion-no-impl:publish")
    tasks.findByPath(":logkmpanion:publish")?.mustRunAfter(":logkmpanion:build")
    tasks.findByPath(":logkmpanion-no-impl:publish")?.mustRunAfter(":logkmpanion-no-impl:build")
}
