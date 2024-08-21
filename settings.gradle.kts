pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "PKO Challenge"
include(":app")
include(":modules:core:network")
include(":modules:core:theme")
include(":modules:movie:domain")
include(":modules:movie:data")
include(":modules:movie:presentation")
include(":modules:core:persistence")
