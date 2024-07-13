plugins {
    `kotlin-dsl`
}


gradlePlugin {
    plugins.register("myPlugin") {
        id = "my-plugin"
        implementationClass = "com.example.todoya.plugins.MyPlugin"
    }
    plugins.register("telegram-reporter") {
        id = "telegram-reporter"
        implementationClass = "com.example.todoya.practice.TelegramReporterPlugin"
    }
    plugins.register("validator") {
        id = "validator"
        implementationClass = "com.example.todoya.practice.ValidateApkSizePlugin"
    }
}


dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.kotlinx.coroutines.core.v171)
    implementation(libs.ktor.client)
    implementation(libs.ktor.client.okhttp)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}