package presentation.infoScreen

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.feature.R
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div.rive.OkHttpDivRiveNetworkDelegate
import com.yandex.div.rive.RiveCustomViewAdapter
import com.yandex.div2.DivData
import okhttp3.OkHttpClient
import org.json.JSONObject
import presentation.settingsScreen.SettingsUiState
import presentation.settingsScreen.ThemeMode

@Composable
fun InfoScreen(
    uiState: SettingsUiState,
    navController: NavController,
) {
    val context = LocalContext.current
    val contextWrapper = remember { ContextThemeWrapper(context, R.style.Base_Theme_ToDoYa) }
    val configuration = remember { createDivConfiguration(contextWrapper, navController) }
    val divData = remember { loadDivDataFromJson(context) }
    val isDarkTheme = when (uiState.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    AndroidView(
        factory = { context ->
            Div2View(
                Div2Context(
                    contextWrapper,
                    configuration,
                    R.style.Base_Theme_ToDoYa,
                    context as LifecycleOwner
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { div2View ->
        div2View.setData(divData, DivDataTag("your_unique_tag_here"))
        div2View.setVariable("theme", if (isDarkTheme) "dark" else "light")
    }
}


private fun createDivConfiguration(context: Context, navController: NavController): DivConfiguration {
    val imageLoader =  PicassoDivImageLoader(context)
    val configuration = DivConfiguration.Builder(imageLoader)
        .actionHandler(SampleDivActionHandler(navController))
        .divCustomContainerViewAdapter(RiveCustomViewAdapter.Builder(context, OkHttpDivRiveNetworkDelegate(
            OkHttpClient.Builder().build())).build())
        .build()
    return configuration
}

private fun loadDivDataFromJson(context: Context): DivData? {
    return try {
        val data = IOUtils.toString(context.assets.open("card.json"))
        val jsonObject = JSONObject(data)
        val cardJson = jsonObject.getJSONObject("card")
        val environment = DivParsingEnvironment(ParsingErrorLogger.LOG)
        environment.parseTemplates(jsonObject.getJSONObject("templates"))
        DivData(environment, cardJson)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}





