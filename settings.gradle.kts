enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")



pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "ToDoYa"
include(":app")
include(":feature")
include(":core")
include(":resources")
