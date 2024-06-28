package com.example.todoya.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPalettePreview(darkTheme: Boolean = false) {
    val colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette
    Column {
        colorScheme.let {
            listOf(
                "Primary" to it.primary,
                "Secondary" to it.secondary,
                "Tertiary" to it.tertiary,
                "Error" to it.error,
                "Scrim" to it.scrim,
                "OutlineVariant" to it.outlineVariant,
                "Surface" to it.surface,
                "OnPrimary" to it.onPrimary,
                "OnSecondary" to it.onSecondary,
                "OnSurface" to it.onSurface
            ).forEach { (name, color) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(name, color = color)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TextStylesPreview() {
    Column {
        Text("Large title — 32/38", style = Typography.titleLarge)
        Text("Title — 20/32", style = Typography.titleMedium)
        Text("BUTTON — 14/24", style = Typography.titleSmall)
        Text("Body — 16/20", style = Typography.bodyMedium)
        Text("Subhead — 14/20", style = Typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun LightColorPalettePreview() {
    TodoYaTheme(darkTheme = false) {
        ColorPalettePreview(darkTheme = false)
    }
}

@Preview(showBackground = true)
@Composable
fun DarkColorPalettePreview() {
    TodoYaTheme(darkTheme = true) {
        ColorPalettePreview(darkTheme = true)
    }
}

@Preview(showBackground = true)
@Composable
fun TextStylesLightPreview() {
    TodoYaTheme(darkTheme = false) {
        TextStylesPreview()
    }
}
