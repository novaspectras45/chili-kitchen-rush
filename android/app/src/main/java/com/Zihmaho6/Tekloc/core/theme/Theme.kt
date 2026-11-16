package com.Zihmaho6.Tekloc.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KitchenColorScheme = lightColorScheme(
    primary = ChiliRed,
    onPrimary = TextOnAccent,
    primaryContainer = TerracottaFlame,
    onPrimaryContainer = TextOnAccent,
    secondary = TerracottaFlame,
    onSecondary = TextOnAccent,
    secondaryContainer = BoardCream,
    onSecondaryContainer = TextPrimary,
    tertiary = HerbGreen,
    onTertiary = TextOnAccent,
    tertiaryContainer = HerbGreen,
    onTertiaryContainer = TextOnAccent,
    background = BaseCream,
    onBackground = TextPrimary,
    surface = SurfaceCream,
    onSurface = TextPrimary,
    surfaceVariant = BoardCream,
    onSurfaceVariant = TextSecondary,
    outline = BorderSand,
    outlineVariant = BorderSand,
    error = ChiliDeep,
    onError = TextOnAccent,
    scrim = EmberDark
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KitchenColorScheme,
        typography = AppTypography,
        content = content
    )
}
