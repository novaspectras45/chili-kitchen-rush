package com.Zihmaho6.Tekloc.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Zihmaho6.Tekloc.core.theme.BoardCream
import com.Zihmaho6.Tekloc.core.theme.BorderSand

@Composable
fun BoardSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    container: Color = BoardCream,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = container,
        tonalElevation = 0.dp,
        shadowElevation = elevation,
        border = BorderStroke(2.dp, BorderSand)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.16f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.06f)
                        )
                    )
                ),
            content = content
        )
    }
}
