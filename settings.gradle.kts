@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("common-publish")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        maven { setUrl("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Shun"
include(":sort-core")
include(":config-core")
include(":filter-core")
include(":recycleview_ui_extra")
include(":config_edit")
include(":sort-ui")
include(":filter-ui")
include(":compose-ui")
if (System.getenv()["JITPACK"] == null) {
    include(":common-config")
    include(":app")
    include(":testcompose")
}
include(":common-dialog")
