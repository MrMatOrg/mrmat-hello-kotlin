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
    //
    // Plugins we like to add by convention

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.1.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.6")
}
