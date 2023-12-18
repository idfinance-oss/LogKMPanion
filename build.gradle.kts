plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}

tasks.register("buildAndPublish", DefaultTask::class) {
    dependsOn(":debug-view:build")
    dependsOn(":debug-view:publish")
    dependsOn(":debug-view-no-impl:build")
    dependsOn(":debug-view-no-impl:publish")
    tasks.findByPath(":debug-view:publish")?.mustRunAfter(":debug-view:build")
    tasks.findByPath(":debug-view-no-impl:publish")?.mustRunAfter(":debug-view-no-impl:build")
}
