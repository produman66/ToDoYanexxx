package com.example.todoya.practice

import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.util.Calendar
import java.util.zip.ZipInputStream
import javax.inject.Inject

abstract class TelegramReporterTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:Input
    abstract val variantName: Property<String>

    @get:Input
    abstract val versionCode: Property<String>

    @get:Input
    abstract val sendAdditionalFile: Property<Boolean>


    @TaskAction
    fun report() {
        println("Files: " + apkDir.get().asFileTree.asPath)
        val token = token.get()
        val chatId = chatId.get()
        val variantName = variantName.get()
        val versionCode = versionCode.get()

        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                val renamedFile = renameApkFile(it, variantName, versionCode)
                val caption = unzip(renamedFile)
                val reportFile = createHtmlReportFile(caption)

                runBlocking {
                    telegramApi.sendMessage("Build finished", token, chatId).apply {
                        println(bodyAsText())
                    }
                    if (sendAdditionalFile.get()) {
                        telegramApi.upload(reportFile, token = token, chatId = chatId).apply {
                            println(bodyAsText())
                            reportFile.delete()
                        }
                    }
                    telegramApi.upload(renamedFile, renamedFile.length().toFileSize(), token, chatId).apply {
                        println("Status = $status")
                    }
                }
            }

    }

    private fun renameApkFile(file: File, variantName: String, versionCode: String): File {
        val safeVariantName = variantName.replace("[^a-zA-Z0-9\\-_]".toRegex(), "")
        val safeVersionCode = versionCode.replace("[^a-zA-Z0-9\\-_]".toRegex(), "")
        val newName = "${file.parent}/todolist-$safeVariantName-$safeVersionCode.apk"
        val renamedFile = File(newName)

        if (file.renameTo(renamedFile)) {
            return renamedFile
        } else {
            throw IllegalStateException("Failed to rename ${file.name} to ${renamedFile.name}")
        }
    }

    private fun createHtmlReportFile(files: List<UnzippedFile>): File {
        val reportFile = File.createTempFile("report-${Calendar.getInstance().timeInMillis}", ".html")
        PrintWriter(reportFile, "UTF-8").use { writer ->
            writer.println("<html><body>")
            writer.println("<h1>Build Report</h1>")
            writer.println("<ul>")
            files.forEach { file ->
                writer.println("<li>${file.filename} - ${file.size.toFileSize()}</li>")
            }
            writer.println("</ul>")
            writer.println("</body></html>")
        }
        return reportFile
    }

    data class UnzippedFile(val filename: String, val size: Long, val path: String)

    private fun unzip(file: File): List<UnzippedFile> = ZipInputStream(FileInputStream(file))
        .use { zipInputStream ->
            generateSequence { zipInputStream.nextEntry }
                .filterNot { it.isDirectory }
                .map {
                    val fn = it.name.split('/').last()
                    UnzippedFile(
                        filename = fn,
                        size = it.size,
                        path = it.name.removeSuffix(fn)
                    )
                }.toList()
        }

    private fun Long.toFileSize(): String {
        return when {
            this < 1024L -> "${String.format("%.2f", this.toDouble()).removeSuffix(".00").removeSuffix("0")}B"
            this < 1024L * 1024L -> "${String.format("%.2f", this.toDouble() / 1024L).removeSuffix(".00").removeSuffix("0")}KB"
            else -> "${String.format("%.2f", this.toDouble() / 1024L / 1024L).removeSuffix(".00").removeSuffix("0")}MB"
        }
    }
}