plugins {
    distribution
    id("mrmat.versioning-conventions")
}

val ansibleSrcPath : Directory? = layout.projectDirectory.dir("src/main/ansible")
val ansibleBuildPath : Provider<Directory> = layout.buildDirectory.dir("ansible")

tasks.create<Copy>("buildAnsible") {
    from(ansibleSrcPath!!.asFile) {
        exclude("**/values.yaml", "**/Chart.yaml")
    }
    from(ansibleSrcPath) {
        include("**/values.yaml", "**/Chart.yaml")
        expand(project.properties)
    }
    into(ansibleBuildPath)
}

tasks.withType<Tar>().configureEach {
    compression = Compression.GZIP
    archiveExtension.set("tar.gz")
}

distributions {
    main {
        distributionBaseName.set(rootProject.name)
        contents {
            from(tasks.named<Copy>("buildAnsible"))
        }
    }
}

tasks.create<Exec>("ansibleLint") {
    dependsOn(tasks.named("assembleDist"))
    workingDir(ansibleBuildPath)
    commandLine("helm", "lint", ".")
}

tasks.named("build") {
    dependsOn(tasks.named("assembleDist"))
}
