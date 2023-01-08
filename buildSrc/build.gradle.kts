plugins {
    `kotlin-dsl`
}

group = "org.mrmat.conventions"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")

    //
    // Plugins we like to add by convention

    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.0.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.22.0")

    //
    // To generate code holding the version

    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("com.squareup:javapoet:1.13.0")

    //
    // For parsing YAML
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
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
