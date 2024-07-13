package com.example.todoya.practice

import Versions
import Versions.versionCode
import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create

class TelegramReporterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.findByType(AndroidComponentsExtension::class.java)
            ?: throw GradleException("Android not found")

        val extension = project.extensions.create("tgReporter", TelegramExtension::class)
        val telegramApi = TelegramApi(HttpClient(OkHttp))

        androidComponents.onVariants { variant ->
            val variantName = variant.name.capitalize()
            val artifacts = variant.artifacts.get(SingleArtifact.APK)
            val validateTaskName = "ValidateApkSizeFor$variantName"
            project.tasks.register("reportTelegramApkFor${variant.name.capitalize()}", TelegramReporterTask::class.java, telegramApi).configure {
                if (extension.checkSize.get()) {
                    dependsOn(validateTaskName)
                }
                apkDir.set(artifacts)
                token.set(extension.token)
                chatId.set(extension.chatId)
                this.variantName.set(variantName)
                versionCode.set(Versions.versionCode.toString())
            }
        }
    }
}

interface TelegramExtension {
    val chatId: Property<String>
    val token: Property<String>
    val checkSize: Property<Boolean>
}