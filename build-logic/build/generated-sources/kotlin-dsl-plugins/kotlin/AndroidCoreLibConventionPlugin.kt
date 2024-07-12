/**
 * Precompiled [android-core-lib-convention.gradle.kts][Android_core_lib_convention_gradle] script plugin.
 *
 * @see Android_core_lib_convention_gradle
 */
public
class AndroidCoreLibConventionPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Android_core_lib_convention_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
