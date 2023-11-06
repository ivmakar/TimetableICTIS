pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Расписание ИКТИБ"
include(":app")
include(":sources:core")
include(":sources:scheduler")
include(":sources:search")
include(":sources:timetable")
