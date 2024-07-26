package presentation.homeScreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
private  fun  SnackbarCountdown (
    timerProgress: Float ,
    secondsRemaining: Int ,
    color: Color
) {
    Box(
        modifier = Modifier.size( 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas( Modifier. matchParentSize()) {
            val strokeStyle = Stroke(
                width = 3. dp. toPx(),
                cap = StrokeCap. Round
            )
            drawCircle(
                color = color.copy(alpha = 0.12f ),
                style = strokeStyle
            )
            drawArc(
                color = color,
                startAngle = - 90f ,
                sweepAngle = (- 360f * timerProgress),
                useCenter = false ,
                style = strokeStyle
            )
        }
        Text(
            text = secondsRemaining. toString(),
            style = LocalTextStyle.current.copy(
                fontSize = 14.sp ,
                color = color
            )
        )
    }
}


@Composable
fun CountdownSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    durationInSeconds: Int = 5,
    actionOnNewLine: Boolean = false,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionColor: Color = SnackbarDefaults.actionColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {
    val totalDuration = remember(durationInSeconds) { durationInSeconds * 1000 }
    var millisRemaining by remember { mutableStateOf(totalDuration) }

    var visible by remember { mutableStateOf(true) }
    LaunchedEffect(snackbarData) {
        while (millisRemaining > 0) {
            delay(40)
            millisRemaining -= 40
        }
        visible = false
        delay(300)
        snackbarData.dismiss()
    }

    val actionLabel = snackbarData.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
                onClick = { snackbarData.performAction() },
                content = { Text(actionLabel) }
            )
        }
    } else {
        null
    }
    val dismissActionComposable: (@Composable () -> Unit)? =
        if (snackbarData.visuals.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = { snackbarData.dismiss() },
                    content = {
                        Icon(Icons.Rounded.Close, null)
                    }
                )
            }
        } else {
            null
        }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 1500)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(durationMillis = 1500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 1000)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 1000)
        )
    ) {

        Snackbar(
            modifier = modifier.padding(12.dp),
            action = actionComposable,
            actionOnNewLine = actionOnNewLine,
            dismissAction = dismissActionComposable,
            dismissActionContentColor = dismissActionContentColor,
            actionContentColor = actionContentColor,
            containerColor = containerColor,
            contentColor = contentColor,
            shape = shape,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SnackbarCountdown(
                    timerProgress = millisRemaining.toFloat() / totalDuration.toFloat(),
                    secondsRemaining = (millisRemaining / 1000) + 1,
                    color = contentColor
                )
                Text(snackbarData.visuals.message)
            }
        }
    }
}
