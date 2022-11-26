import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.squareup:kotlinpoet:1.11.0")
    }
}

plugins {
    kotlin("jvm") version "1.7.20"
    application

    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
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
    sourceSets.test {
        kotlin.srcDir("${project.buildDir}/generated/kotlinpoet/test/kotlin")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register("generateVersion") {
    description = "Transfers the build-time version to runtime code"
    inputs.property("project.version", project.version)
    outputs.file("${project.buildDir}/generated/kotlinpoet/main/kotlin/${project.group.toString().replace(".", "/")}/Version.kt")
    doLast {
        val versionProperty = com.squareup.kotlinpoet.PropertySpec
            .builder("VERSION", String::class)
            .initializer("%S", project.version)
            .build()
        val versionCompanion = com.squareup.kotlinpoet.TypeSpec
            .companionObjectBuilder()
            .addProperty(versionProperty)
            .build()
        val versionClass = com.squareup.kotlinpoet.TypeSpec
            .classBuilder("Version")
            .addType(versionCompanion)
            .build()
        val versionClassFile = com.squareup.kotlinpoet.FileSpec
            .builder(project.group.toString(), "Version")
            .addType(versionClass)
            .build()
        versionClassFile.writeTo(file("${project.buildDir}/generated/kotlinpoet/main/kotlin"))
    }
}

tasks.named("compileKotlin") { dependsOn("generateVersion") }
