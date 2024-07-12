import org.gradle.api.JavaVersion


object AndroidConst {
    const val NAMESPACE = "com.example.todoya"
    const val COMPILE_SKD = 34
    const val MIN_SKD = 28
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_1_8
    const val KOTLIN_JVM_TARGET = "1.8"
}