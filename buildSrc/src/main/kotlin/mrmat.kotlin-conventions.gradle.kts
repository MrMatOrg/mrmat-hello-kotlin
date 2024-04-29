plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    // You would technically add our other plugins here but since they come out of buildSrc as well that would
    // cause a circular dependency
}

repositories {
    mavenCentral()
}

dependencies {
    kotlinCompilerPluginClasspath("com.squareup:kotlinpoet:1.16.0")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    sourceSets.main {
        kotlin.srcDir(layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
}

tasks.test {
    useJUnitPlatform()
}

tasks.named("runKtlintCheckOverMainSourceSet") {
    dependsOn(tasks.named("generateKotlinVersion"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(tasks.named("generateKotlinVersion"))
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
