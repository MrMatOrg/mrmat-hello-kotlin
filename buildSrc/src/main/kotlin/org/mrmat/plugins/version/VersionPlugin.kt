package org.mrmat.plugins.version

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetContainer

abstract class VersionPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(VersionBasePlugin::class.java)
        val versionExtension = project.extensions.getByType(VersionExtension::class.java)

        project.version =
            System.getenv(versionExtension.environmentVersion.get()) ?: versionExtension.localVersion.get()

        if (project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
//            val generatedVersionSourceSet = project.container(KotlinSourceSet::class.java).create("generatedVersion")
//            generatedVersionSourceSet.kotlin.srcDirs(project.layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
//            val sourceSetExtension = project.extensions.getByType(KotlinSourceSetContainer::class.java)
//            sourceSetExtension.sourceSets.add(generatedVersionSourceSet)

              //val generatedVersionSourceSet = project.container(SourceSet::class.java).create("generatedVersion")
//            generatedVersionSourceSet.java.srcDir(project.layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
//            val sourceSetExtension = project.extensions.getByType(SourceSetContainer::class.java)
//            sourceSetExtension.add(generatedVersionSourceSet)

            val sourceSetExtension = project.extensions.getByType(KotlinSourceSetContainer::class.java)
            sourceSetExtension.sourceSets.create("generatedVersion") {
                kotlin {
                    srcDirs(project.layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
                }
            }



//            val generatedKotlinVersionSourceDirectorySet = project.container(SourceDirectorySet::class.java).create("kotlin") {
//                srcDirs(project.layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
//            }
            //sourceSetExtension.add(project.container(KotlinSourceSet::class.java) as SourceSet)
//            sourceSetExtension.register("generatedKotlinSources") {
//                project.container(KotlinSourceSet::class.java).create("kotlinVersionSources") {
//                    kotlin {
//                        srcDirs(project.layout.buildDirectory.dir("generated/kotlinpoet/main/kotlin"))
//                        apiConfigurationName = "foo"
//                    }
//                }
            }
        }
}
