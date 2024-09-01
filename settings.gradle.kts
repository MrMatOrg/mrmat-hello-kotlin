pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/MrMatAP/mrmat-gradle-plugins")
            credentials {
                username = System.getProperty("gpr.user", "UNKNOWN")
                password = System.getProperty("gpr.token", "UNKNOWN")
            }
        }
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.9.23"

        val mrmatPluginVersion = "0.0.24"
        id("org.mrmat.plugins.gradle.version") version mrmatPluginVersion
        id("org.mrmat.plugins.gradle.ansible") version mrmatPluginVersion
        id("org.mrmat.plugins.gradle.container") version mrmatPluginVersion
        id("org.mrmat.plugins.gradle.helm") version mrmatPluginVersion
        id("org.mrmat.plugins.gradle.rpm") version mrmatPluginVersion
        id("mrmat.kotlin-conventions") version mrmatPluginVersion
    }
}

rootProject.name = "mrmat-hello-kotlin"

include(":versioning")
project(":versioning").projectDir = file("apps/versioning")

include(":helloworld-spring")
project(":helloworld-spring").projectDir = file("apps/helloworld-spring")

include(":deployment:helm")
project(":deployment:helm").projectDir = file("deployment/helm")

include(":deployment:rpm")
project(":deployment:rpm").projectDir = file("deployment/rpm")

include(":deployment:ansible")
project(":deployment:ansible").projectDir = file("deployment/ansible")
