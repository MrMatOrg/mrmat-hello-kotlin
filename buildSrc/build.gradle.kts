plugins {
    `kotlin-dsl`
}

group = "org.mrmat.conventions"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")

    //
    // Plugins we like to add by convention

    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.5.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.1")

    //
    // To generate code holding the version

    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("com.squareup:javapoet:1.13.0")

    //
    // For parsing YAML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

gradlePlugin {
    plugins {
        create("versionBasePlugin") {
            id = "org.mrmat.plugins.version.base"
            implementationClass = "org.mrmat.plugins.version.VersionBasePlugin"
        }
        create("versionPlugin") {
            id = "org.mrmat.plugins.version"
            implementationClass = "org.mrmat.plugins.version.VersionPlugin"
        }
        create("containerBasePlugin") {
            id = "org.mrmat.plugins.container.base"
            implementationClass = "org.mrmat.plugins.container.ContainerBasePlugin"
        }
        create("containerPlugin") {
            id = "org.mrmat.plugins.container"
            implementationClass = "org.mrmat.plugins.container.ContainerPlugin"
        }
        create("rpmBasePlugin") {
            id = "org.mrmat.plugins.rpm.base"
            implementationClass = "org.mrmat.plugins.rpm.RPMBasePlugin"
        }
        create("rpmPlugin") {
            id = "org.mrmat.plugins.rpm"
            implementationClass = "org.mrmat.plugins.rpm.RPMPlugin"
        }
        create("helmBasePlugin") {
            id = "org.mrmat.plugins.helm.base"
            implementationClass = "org.mrmat.plugins.helm.HelmBasePlugin"
        }
        create("helmPlugin") {
            id = "org.mrmat.plugins.helm"
            implementationClass = "org.mrmat.plugins.helm.HelmPlugin"
        }
        create("ansibleBasePlugin") {
            id = "org.mrmat.plugins.ansible.base"
            implementationClass = "org.mrmat.plugins.ansible.AnsibleBasePlugin"
        }
        create("ansiblePlugin") {
            id = "org.mrmat.plugins.ansible"
            implementationClass = "org.mrmat.plugins.ansible.AnsiblePlugin"
        }
    }
}
