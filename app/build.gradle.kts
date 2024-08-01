plugins {
    id("android-app-convention")
    id("my-plugin")
    id("telegram-reporter")
    id("validator")
    alias(libs.plugins.jetbrains.kotlin.android)
}
validateApkSize {

    size = 24 * 1024 * 1024
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
}

tgReporter {
    checkSize = false
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
    sendAdditionalFile = true
}

myPlugin {
    myDeps {
        enable.set(true)
        prefix.set("PPPPP")
    }
}


android {
    defaultConfig {
        applicationId = "com.example.todoya"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "17ef137909c745f6ab3d1577533adb01"
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":feature"))
    implementation(libs.material)
    implementation(libs.androidx.activity)

    //Compose
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //UI
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Livecycle
    kapt(libs.androidx.lifecycle.compiler)
    testImplementation(libs.androidx.lifecycle.runtime.testing)

    //Core
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.core.ktx)

    //Junit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)


    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    // Coroutines
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)

}