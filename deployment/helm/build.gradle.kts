plugins {
    distribution
}

description = "The Helm chart deploying the mrmat-hello-kotlin containers"
version = "1.0.27"

val helmSrcPath = layout.projectDirectory.dir("src/helm")
val helmBuildPath = layout.buildDirectory.dir("helm")

dependencies {
    project(":apps:autoversioning")
}

tasks.create<Copy>("buildHelm") {
    from(helmSrcPath) {
        exclude("**/values.yaml", "**/Chart.yaml")
    }
    from(helmSrcPath) {
        include("**/values.yaml", "**/Chart.yaml")
        expand(project.properties)
    }
    into(helmBuildPath)
}

tasks.withType<Tar> {
    compression = Compression.GZIP
    archiveExtension.set("tar.gz")
}

distributions {
    main {
        distributionBaseName.set(rootProject.name)
        contents {
            from(tasks.named<Copy>("buildHelm"))
        }
    }
}

tasks.create<Exec>("helmLint") {
    dependsOn(tasks.named("assembleDist"))
    workingDir(helmBuildPath)
    commandLine("helm", "lint", ".")
}

tasks.create<Exec>("helmDeployLocally") {
    dependsOn(tasks.named("assembleDist"))
    workingDir(helmBuildPath)
    commandLine("helm", "upgrade", "--install", "--create-namespace", rootProject.name, ".")
}

tasks.create<Exec>("helmUndeployLocally") {
    commandLine("helm", "uninstall", rootProject.name)
}

tasks.named("build") {
    dependsOn(tasks.named("assembleDist"))
}