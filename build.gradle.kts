// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("composeVersion", "1.5.0")
    }
}

plugins {
    val androidVersion = "8.1.2"
    val kotlinVersion = "1.9.0"
    id("com.android.application") version androidVersion apply false
    id("com.android.library") version androidVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("common-publish") apply false
}

val publishArtifact = listOf(
    "common-dialog",
    "compose-ui",
    "config_edit",
    "filter-ui",
    "recycleview_ui_extra", "sort-ui"
)

val publishJavaArtifact = listOf(
    "sort-core", "filter-core", "config-core"
)

subprojects {
    if (publishArtifact.contains(name)) {
        plugins.apply("common-publish")
    }
    if (publishJavaArtifact.contains(name)) {
        plugins.apply("common-publish")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}