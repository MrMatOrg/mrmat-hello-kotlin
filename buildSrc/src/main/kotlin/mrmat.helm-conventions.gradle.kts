plugins {
    distribution
    id("mrmat.versioning-conventions")
}

val helmSrcPath : Directory? = layout.projectDirectory.dir("src/helm")
val helmBuildPath : Provider<Directory> = layout.buildDirectory.dir("helm")

tasks.create<Copy>("buildHelm") {
    from(helmSrcPath!!.asFile) {
        exclude("**/values.yaml", "**/Chart.yaml")
    }
    from(helmSrcPath) {
        include("**/values.yaml", "**/Chart.yaml")
        expand(project.properties)
    }
    into(helmBuildPath)
}

tasks.withType<Tar>().configureEach {
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
