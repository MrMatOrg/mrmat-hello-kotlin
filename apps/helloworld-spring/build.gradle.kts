plugins {
    kotlin("jvm")
    id("org.mrmat.plugins.version")
    id("org.mrmat.plugins.container")
    id("mrmat.kotlin-conventions")

    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.7.22"
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

mrmatContainer {
    imageName.set("helloworld-spring")
}

//
// Let's assemble everything that's needed in the container directory

tasks.register<Copy>("containerDependencies") {
    from(tasks.named("bootJar")) {
        rename(".*", "helloworld.jar")
    }
    into(mrmatContainer.buildPath)
}

tasks.named<Copy>("containerAssemble") {
    dependsOn(tasks.named<Copy>("containerDependencies"))
}
