plugins {
    kotlin("jvm")
    id("org.mrmat.plugins.version")
    id("org.mrmat.plugins.container")
    id("mrmat.kotlin-conventions")

    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.spring") version "1.9.23"

    id("org.jetbrains.dokka") version "1.9.20"
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

ktlint {
    ignoreFailures.set(true)
    ktlint.debug.set(true)
}

mrmatContainer {
    imageName.set("helloworld-spring")
    runCommandArgs.set(listOf("run", "-i", "-p", "8080:8080", "--rm"))
}

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.buildDirectory.dir("dokka"))
}

tasks.runKtlintCheckOverGeneratedVersionSourceSet.configure {
    dependsOn(tasks.generateKotlinVersion)
    mustRunAfter(tasks.generateKotlinVersion)
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
