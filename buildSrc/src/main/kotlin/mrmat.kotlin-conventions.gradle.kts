import org.mrmat.plugins.version.GenerateKotlinVersionTask

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    // TODO: We have a circular dependency within buildSrc setting this. Conventions should be separated from plugins
    // id("org.mrmat.plugins.version")
}

repositories {
    mavenCentral()
}

dependencies {
    kotlinCompilerPluginClasspath("com.squareup:kotlinpoet:1.11.0")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    sourceSets.main {
        kotlin.srcDir("${project.buildDir}/generated/kotlinpoet/main/kotlin")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<GenerateKotlinVersionTask>("generateVersion") {
    description = "Transfers the build-time version to a generated runtime code file"
    group = "mrmat.kotlin"
}

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn(tasks.named<GenerateKotlinVersionTask>("generateVersion"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.named<GenerateKotlinVersionTask>("generateVersion"))
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}