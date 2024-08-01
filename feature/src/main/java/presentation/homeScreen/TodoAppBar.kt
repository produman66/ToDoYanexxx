package presentation.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.feature.R


/**
 * Composable function for the top app bar of the Todo list screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isCollapsed: Boolean,
    isEyeClosed: Boolean,
    countCompletedTodo: Int,
    onEyeToggle: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    val elevation = if (isCollapsed) 8.dp else 0.dp

    Column(
        modifier = Modifier
            .shadow(elevation)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        LargeTopAppBar(
            title = {
                if (scrollBehavior.state.collapsedFraction < 0.5) {
                    Text(
                        text = stringResource(id = R.string.my_todo),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 48.dp)
                            .semantics {
                                contentDescription = "Мои дела"
                            },

                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.my_todo),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .semantics {
                                contentDescription = "Мои дела"
                            },
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = if (isCollapsed) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
            ),
            actions = {
                if (scrollBehavior.state.collapsedFraction > 0.8) {
                    IconButton(
                        onClick = onSettingsClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = stringResource(id = R.string.settings),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }

                    IconButton(onClick = onInfoClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.assignment),
                            contentDescription = stringResource(id = R.string.info),
                            tint = MaterialTheme.colorScheme.tertiary,
                        )

                    }
                    Image(
                        painter = painterResource(
                            if (isEyeClosed) R.drawable.visibility_off
                            else R.drawable.visibility
                        ),
                        contentDescription = if (isEyeClosed) stringResource(id = R.string.visibility) else stringResource(
                            id = R.string.visibility_off
                        ),
                        modifier = Modifier
                            .clickable {
                                onEyeToggle()
                            }
                            .padding(horizontal = 20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )

        AnimatedVisibility(
            visible = scrollBehavior.state.collapsedFraction < 0.5,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 64.dp,
                        end = 24.dp,
                        bottom = 10.dp
                    )
                    .background(MaterialTheme.colorScheme.primary)
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Выполнено - ${countCompletedTodo}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                Image(
                    painter = painterResource(
                        if (isEyeClosed) R.drawable.visibility_off
                        else R.drawable.visibility
                    ),
                    contentDescription = if (isEyeClosed) stringResource(id = R.string.visibility) else stringResource(
                        id = R.string.visibility_off
                    ),
                    modifier = Modifier
                        .clickable {
                            onEyeToggle()
                        }.semantics {
                            role = Role.Button
                        },

                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                )
            }
        }
    }
}