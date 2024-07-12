plugins {
    `kotlin-dsl`
}


gradlePlugin {
    plugins.register("myPlugin") {
        id = "my-plugin"
        implementationClass = "ru.yandex.shmr24.plugins.MyPlugin"
    }
}


dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.kotlinx.coroutines.core.v171)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}