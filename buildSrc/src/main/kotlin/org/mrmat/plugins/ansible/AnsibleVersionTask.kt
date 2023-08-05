package org.mrmat.plugins.ansible

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class AnsibleVersionTask: DefaultTask() {

    @get:Input
    abstract val version: Property<String>

    @get:Input
    abstract val appName: Property<String>

    @get:OutputFile
    abstract val ansibleAppVarsFile: RegularFileProperty

    init {
        val ansibleExtension = project.extensions.getByType<AnsibleExtension>(AnsibleExtension::class.java)
        version.convention("${project.version}")
        appName.convention("${project.rootProject.name}")
        ansibleAppVarsFile.convention(ansibleExtension.ansibleAppVarsFile.get())
    }

    @TaskAction
    fun execute() {
        var ansibleApp = AnsibleAppModel(appName = appName.get(), appVersion = version.get() )
        val mapper = YAMLMapper().registerKotlinModule()
        mapper.writeValue(ansibleAppVarsFile.get().asFile, ansibleApp)
    }
}
