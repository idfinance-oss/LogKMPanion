plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}

tasks.register("buildAndPublish", DefaultTask::class) {
    dependsOn(":debug-view:build")
    dependsOn(":debug-view:publish")
    tasks.findByPath(":debug-view:publish")?.mustRunAfter(":debug-view:build")
}
