import org.gradle.api.JavaVersion

object AndroidConst {

    const val NAMESPACE = "com.example.todoya"
    const val COMPILE_SKD = Versions.compileSdk
    const val MIN_SKD = Versions.minSdk
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_1_8
    const val KOTLIN_JVM_TARGET = "1.8"
}