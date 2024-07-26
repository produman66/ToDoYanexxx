package presentation.infoScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class SampleDivActionHandler(private val navController: NavController) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME_SAMPLE && handleSampleAction(url, view.view.context)) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleSampleAction(action: Uri, context: Context): Boolean {
        return when (action.host) {
            "back" -> {
                Log.d("sampleHandler", "${action.host}")
                navController.popBackStack()
                true
            }
            else -> {
                Log.d("sampleHandler", "${action.host}")
                false
            }
        }
    }

    companion object {
        const val SCHEME_SAMPLE = "sample-action"
    }
}