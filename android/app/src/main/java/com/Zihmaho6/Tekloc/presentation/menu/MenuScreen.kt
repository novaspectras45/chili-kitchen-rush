package com.Zihmaho6.Tekloc.presentation.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.Zihmaho6.Tekloc.core.theme.BaseCream
import com.Zihmaho6.Tekloc.core.theme.BoardCream
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextOnAccent
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextSecondary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary
import com.Zihmaho6.Tekloc.core.ui.BoardSurface
import com.Zihmaho6.Tekloc.core.ui.CoinPill
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground

@Composable
fun MenuRoute(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onStats: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = { onPlay(state.nextLevel) },
        onLevels = onLevels,
        onStats = onStats,
        onCollection = onCollection,
        onSettings = onSettings
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onStats: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        shown = true
    }
    val entry by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(durationMillis = 280),
        label = "menuEntry"
    )
    val transition = rememberInfiniteTransition(label = "menuLoop")
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    ScreenBackground(
        overlay = listOf(
            BaseCream.copy(alpha = 0.95f),
            SurfaceCream.copy(alpha = 0.97f),
            BoardCream.copy(alpha = 0.98f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
                .alpha(entry)
                .graphicsLayer { translationY = (1f - entry) * 24f }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.brand_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        maxLines = 2,
                        modifier = Modifier.width(150.dp)
                    )
                }
                CoinPill(coins = state.coins.toString())
            }

            Spacer(modifier = Modifier.height(10.dp))

            BoardSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.menu_shift_ready, state.nextLevel),
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (state.bestScore > 0) {
                                stringResource(R.string.menu_best, state.bestScore)
                            } else {
                                stringResource(R.string.menu_no_best)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = stringResource(R.string.cd_cook),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(92.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = TextTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = 184.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayTile(
                    onClick = onPlay,
                    scale = if (state.reduceMotion) 1f else breathe,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BentoTile(
                        label = stringResource(R.string.cta_levels),
                        value = stringResource(
                            R.string.menu_levels_value,
                            state.unlockedLevels,
                            state.levelCount
                        ),
                        iconRes = R.drawable.ic_grid,
                        onClick = onLevels,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    BentoTile(
                        label = stringResource(R.string.cta_stats),
                        value = if (state.topTier > 0) {
                            stringResource(R.string.menu_stats_value, state.topTier)
                        } else {
                            null
                        },
                        iconRes = R.drawable.ic_bar,
                        onClick = onStats,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BentoTile(
                    label = stringResource(R.string.cta_collection),
                    value = stringResource(
                        R.string.menu_collection_value,
                        state.unlockedRecipes,
                        state.recipeCount
                    ),
                    iconRes = R.drawable.ic_star,
                    onClick = onCollection,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                BentoTile(
                    label = stringResource(R.string.cta_settings),
                    value = null,
                    iconRes = R.drawable.ic_slider,
                    onClick = onSettings,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PlayTile(
    onClick: () -> Unit,
    scale: Float,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(listOf(TerracottaFlame, ChiliRed)),
                    shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(88.dp)
                    .alpha(0.25f)
            )
            Text(
                text = stringResource(R.string.cta_play),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = TextOnAccent
            )
        }
    }
}

@Composable
private fun BentoTile(
    label: String,
    value: String?,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceCream,
            contentColor = TextPrimary
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = ChiliRed,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                maxLines = 1
            )
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextTertiary,
                    maxLines = 1
                )
            }
        }
    }
}
