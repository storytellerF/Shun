plugins {
    alias(libs.plugins.android) apply false
    alias(libs.plugins.kotlin) apply false
    id("common-publish") apply false
}

val publishArtifact = listOf(
    "common-dialog",
    "config_edit",
    "recycleview_ui_extra",
    "compose-ui",
    "filter-ui",
    "sort-ui"
)

val publishJavaArtifact = listOf(
    "sort-core", "filter-core", "config-core"
)

subprojects {
//    if (publishArtifact.contains(name)) {
//        plugins.apply("common-publish")
//    }
    if (publishJavaArtifact.contains(name)) {
        plugins.apply("common-publish")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}