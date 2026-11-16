package com.Zihmaho6.Tekloc.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Zihmaho6.Tekloc.core.theme.BorderSand
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.EmberDark
import com.Zihmaho6.Tekloc.core.theme.HerbGreen
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame

@Composable
fun HeatGauge(
    heat: Float,
    target: Float,
    window: Float,
    maxHeat: Float,
    highContrast: Boolean,
    stateLabel: String,
    modifier: Modifier = Modifier,
    alert: Boolean = false
) {
    val animated by animateFloatAsState(
        targetValue = heat,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "heatFill"
    )
    val bandColor = if (alert) ChiliRed else EmberDark
    Canvas(
        modifier = modifier
            .height(30.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        val radius = CornerRadius(size.height / 2f, size.height / 2f)
        drawRoundRect(color = BorderSand, cornerRadius = radius)

        val ratio = (animated / maxHeat).coerceIn(0f, 1f)
        val fillWidth = size.width * ratio
        if (fillWidth > 0f) {
            val brush = if (highContrast) {
                val stop = when {
                    animated < maxHeat * 0.34f -> HerbGreen
                    animated < maxHeat * 0.67f -> TerracottaFlame
                    else -> ChiliRed
                }
                Brush.horizontalGradient(listOf(stop, stop))
            } else {
                Brush.horizontalGradient(
                    colors = listOf(HerbGreen, TerracottaFlame, ChiliRed),
                    startX = 0f,
                    endX = size.width
                )
            }
            drawRoundRect(
                brush = brush,
                size = Size(fillWidth, size.height),
                cornerRadius = radius
            )
        }

        val low = ((target - window) / maxHeat).coerceIn(0f, 1f)
        val high = ((target + window) / maxHeat).coerceIn(0f, 1f)
        val bandLeft = size.width * low
        val bandWidth = (size.width * high) - bandLeft
        if (bandWidth > 0f) {
            drawRoundRect(
                color = bandColor.copy(alpha = 0.22f),
                topLeft = Offset(bandLeft, 0f),
                size = Size(bandWidth, size.height),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = if (alert) ChiliRed else bandColor,
                topLeft = Offset(bandLeft, 0f),
                size = Size(bandWidth, size.height),
                cornerRadius = CornerRadius(6f, 6f),
                style = Stroke(width = if (highContrast) 8f else 5f)
            )
        }

        val needleX = (size.width * ratio).coerceIn(2f, size.width - 2f)
        drawLine(
            color = Color.White,
            start = Offset(needleX, 3f),
            end = Offset(needleX, size.height - 3f),
            strokeWidth = 5f
        )
        drawLine(
            color = EmberDark,
            start = Offset(needleX, 3f),
            end = Offset(needleX, size.height - 3f),
            strokeWidth = 2f
        )
    }
}
