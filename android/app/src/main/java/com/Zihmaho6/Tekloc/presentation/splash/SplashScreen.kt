package com.Zihmaho6.Tekloc.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.core.di.ServiceLocator
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.EmberDark
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextOnAccent
import com.Zihmaho6.Tekloc.core.theme.TextOnDark
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val state by splashViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.finished) {
        if (state.finished) onFinished()
    }
    SplashScreen(state = state)
}

@Composable
fun SplashScreen(state: SplashUiState) {
    val transition = rememberInfiniteTransition(label = "splashLoop")
    val shimmer by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )
    val glow by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    ScreenBackground(
        overlay = listOf(
            EmberDark.copy(alpha = 0.95f),
            Color(0xFF5A2418).copy(alpha = 0.92f),
            ChiliRed.copy(alpha = 0.80f)
        )
    ) {
        if (!state.reduceMotion) {
            EmberSpecks(transition = transition)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(224.dp)
                        .alpha(glow)
                        .background(
                            Brush.radialGradient(
                                listOf(TerracottaFlame.copy(alpha = 0.55f), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
                Surface(
                    modifier = Modifier
                        .size(190.dp)
                        .graphicsLayer {
                            scaleX = if (state.reduceMotion) 1f else shimmer
                            scaleY = if (state.reduceMotion) 1f else shimmer
                        },
                    shape = CircleShape,
                    color = Color(0xFFF2EAD8).copy(alpha = 0.12f),
                    border = BorderStroke(2.dp, TerracottaFlame.copy(alpha = 0.40f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = stringResource(R.string.cd_logo),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(132.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextOnAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.brand_subtitle),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                letterSpacing = 3.sp,
                color = TextOnDark.copy(alpha = 0.80f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                color = ChiliRed,
                trackColor = EmberDark.copy(alpha = 0.60f),
                modifier = Modifier
                    .width(180.dp)
                    .height(6.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = TextOnDark.copy(alpha = 0.72f)
            )
        }
    }
}

@Composable
private fun EmberSpecks(transition: androidx.compose.animation.core.InfiniteTransition) {
    val first by transition.animateFloat(
        initialValue = 0f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ember1"
    )
    val second by transition.animateFloat(
        initialValue = 0f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ember2"
    )
    val third by transition.animateFloat(
        initialValue = 0f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ember3"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Speck(size = 6, offsetX = 56, rise = first, modifier = Modifier.align(Alignment.BottomStart))
        Speck(size = 9, offsetX = 0, rise = second, modifier = Modifier.align(Alignment.BottomCenter))
        Speck(size = 12, offsetX = -48, rise = third, modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun Speck(size: Int, offsetX: Int, rise: Float, modifier: Modifier = Modifier) {
    val progress = (-rise / 120f).coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .padding(bottom = 90.dp)
            .offset(x = offsetX.dp, y = rise.dp)
            .size(size.dp)
            .alpha((1f - progress) * 0.85f)
            .background(TerracottaFlame, CircleShape)
    )
}
