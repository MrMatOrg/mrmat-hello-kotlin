buildscript {
    dependencies {
        classpath("com.squareup:kotlinpoet:1.11.0")
    }
}

plugins {
    kotlin("jvm") version "1.7.20"
    application

    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

group = "org.mrmat.hello.kotlin"
version = System.getenv("MRMAT_VERSION") ?: "0.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("org.mrmat.hello.kotlin.MainKt")
}

kotlin {
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

tasks.register("generateVersion") {
    description = "Transfers the build-time version to a generated runtime code file"
    group = "other"
    inputs.property("project.version", project.version)
    outputs.file("${project.buildDir}/generated/kotlinpoet/main/kotlin/${project.group.toString().replace(".", "/")}/Version.kt")
    doLast {
        val versionProperty = com.squareup.kotlinpoet.PropertySpec
            .builder("VERSION", String::class, com.squareup.kotlinpoet.KModifier.CONST)
            .initializer("%S", project.version)
            .build()
        val versionCompanionObject = com.squareup.kotlinpoet.TypeSpec
            .companionObjectBuilder()
            .addProperty(versionProperty)
            .build()
        val versionClass = com.squareup.kotlinpoet.TypeSpec
            .classBuilder("Version")
            .addType(versionCompanionObject)
            .build()
        val versionClassFile = com.squareup.kotlinpoet.FileSpec
            .builder(project.group.toString(), "Version")
            .addType(versionClass)
            .indent("    ")
            .build()
        versionClassFile.writeTo(file("${project.buildDir}/generated/kotlinpoet/main/kotlin"))
    }
}

tasks.named("compileKotlin") {
    dependsOn("generateVersion")
    mustRunAfter("generateVersion")
}
