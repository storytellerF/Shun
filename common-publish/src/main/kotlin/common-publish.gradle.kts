plugins {
    `maven-publish`
}

version = "1.2.0"

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                val component = components.find {
                    it.name == "java" || it.name == "release"
                }
                from(component)
            }
        }
    }
}