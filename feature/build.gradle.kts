plugins {
    id("android-feature-lib-convention")
}


dependencies {
    implementation(project(":core"))

    implementation(libs.div)
    implementation(libs.div.core)
    implementation(libs.div.json)
    implementation(libs.div.picasso)
    implementation(libs.div.pinch.to.zoom)
    implementation(libs.div.rive)


    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.mockito.inline)
}