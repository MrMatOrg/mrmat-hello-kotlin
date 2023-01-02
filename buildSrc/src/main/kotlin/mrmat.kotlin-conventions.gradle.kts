plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    id("mrmat.base-conventions")
    id("mrmat.versioning-conventions")
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

tasks.register<org.mrmat.GenerateVersionTask>("generateVersion") {
    description = "Transfers the build-time version to a generated runtime code file"
    group = "mrmat.kotlin"
}

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn(tasks.named<org.mrmat.GenerateVersionTask>("generateVersion"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.named<org.mrmat.GenerateVersionTask>("generateVersion"))
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}