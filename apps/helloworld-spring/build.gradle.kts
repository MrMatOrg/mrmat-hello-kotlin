plugins {
    kotlin("jvm")
    id("org.mrmat.plugins.version")
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.7.22"

    id("mrmat.kotlin-conventions")
}

group = "org.mrmat.hello.kotlin.app.helloworld.spring"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
    mainClass.set("org.mrmat.hello.kotlin.app.helloworld.spring.AppKt")
}

tasks.create<Exec>("containerBuild") {
    group = "container"
    description = "Build a container for the Spring Boot Jar"
    dependsOn(tasks.withType<Jar>())
    workingDir(layout.projectDirectory)
    commandLine(
        "docker",
        "build",
        "--build-arg", "JAR=build/libs/${project.name}-${project.version}.jar",
        "--label", "org.mrmat.version=${project.version}",
        "--file=src/main/container/Dockerfile",
        "--tag", "mrmat/${project.name}:${project.version}",
        "."
    )
}
