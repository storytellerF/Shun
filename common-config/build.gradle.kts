plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

val javaVersion = JavaVersion.VERSION_1_8
java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion.majorVersion))
    }
}

dependencies {
    implementation(project(":config-core"))
    implementation(project(":filter-core"))
    implementation(project(":sort-core"))
}