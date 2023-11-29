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

rootProject.name = "TheRetosVirtuales"
include(":app")
include(":app:model")
include(":Android_LIB_CommonsAds")
include(":Android_APP_2DGravity")
include(":appgravity")
include(":Android_LIB_Commons2D")
include(":Android_LIB_Commons")
include(":app100Bubbles")
include("::appBolas")

include(":Vistas")
include(":helpers")
