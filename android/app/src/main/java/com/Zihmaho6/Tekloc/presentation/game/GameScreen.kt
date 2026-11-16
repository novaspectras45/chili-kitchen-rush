package com.Zihmaho6.Tekloc.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.core.config.GameConfig
import com.Zihmaho6.Tekloc.core.di.ServiceLocator
import com.Zihmaho6.Tekloc.core.theme.BaseCream
import com.Zihmaho6.Tekloc.core.theme.BoardCream
import com.Zihmaho6.Tekloc.core.theme.BorderSand
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.HerbDeep
import com.Zihmaho6.Tekloc.core.theme.HerbGreen
import com.Zihmaho6.Tekloc.core.theme.PaperCream
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextOnAccent
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextSecondary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary
import com.Zihmaho6.Tekloc.core.ui.BoardSurface
import com.Zihmaho6.Tekloc.core.ui.HeatGauge
import com.Zihmaho6.Tekloc.core.ui.OrderTicketCard
import com.Zihmaho6.Tekloc.core.ui.PrimaryButton
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground
import com.Zihmaho6.Tekloc.core.ui.StatCard
import com.Zihmaho6.Tekloc.domain.model.ShiftResult
import com.Zihmaho6.Tekloc.domain.model.ToleranceBand
import com.Zihmaho6.Tekloc.presentation.dialog.PauseDialog
import kotlin.math.sin

@Composable
fun GameRoute(
    level: Int,
    attempt: Int,
    onFinished: (ShiftResult) -> Unit,
    onMenu: () -> Unit,
    onSettings: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(level, attempt) {
        gameViewModel.prepare(level, attempt)
    }
    LaunchedEffect(Unit) {
        gameViewModel.finished.collect { result -> onFinished(result) }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameScreen(
        state = state,
        onGo = gameViewModel::go,
        onHeatUp = gameViewModel::heatUp,
        onCoolDown = gameViewModel::coolDown,
        onServe = gameViewModel::serve,
        onPause = gameViewModel::pause,
        onResume = gameViewModel::resume,
        onSettings = onSettings,
        onMenu = {
            gameViewModel.abandon()
            onMenu()
        }
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onGo: () -> Unit,
    onHeatUp: () -> Unit,
    onCoolDown: () -> Unit,
    onServe: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onSettings: () -> Unit,
    onMenu: () -> Unit
) {
    val wokScale = remember { Animatable(1f) }
    val gain = remember { Animatable(0f) }
    val shake = remember { Animatable(0f) }

    LaunchedEffect(state.pressToken) {
        if (state.pressToken > 0) {
            wokScale.animateTo(1.06f, spring(dampingRatio = 0.45f, stiffness = 520f))
            wokScale.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = 520f))
        }
    }
    LaunchedEffect(state.gainToken) {
        if (state.gainToken > 0) {
            gain.snapTo(0f)
            gain.animateTo(1f, tween(durationMillis = 620))
        }
    }
    LaunchedEffect(state.failToken) {
        if (state.failToken > 0) {
            shake.snapTo(0f)
            shake.animateTo(1f, tween(durationMillis = 280))
        }
    }

    val shakeOffset = if (shake.value in 0.001f..0.999f) {
        sin(shake.value * 4f * 6.2832f) * 8f
    } else {
        0f
    }

    ScreenBackground(
        overlay = listOf(
            BaseCream.copy(alpha = 0.90f),
            BoardCream.copy(alpha = 0.95f),
            BorderSand.copy(alpha = 0.97f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            GameHeader(state = state, onPause = onPause)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(118.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.rail.isEmpty()) {
                    Text(
                        text = stringResource(R.string.game_ready),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    state.rail.take(GameConfig.RAIL_SIZE).forEachIndexed { index, ticket ->
                        OrderTicketCard(
                            name = stringResource(ticket.nameRes),
                            rushLabel = stringResource(R.string.game_rush),
                            heatPips = ticket.pips,
                            maxPips = 10,
                            rush = ticket.rush,
                            rushRatio = ticket.rushRemaining / GameConfig.RUSH_SECONDS,
                            active = index == 0,
                            tilt = if (index % 2 == 0) -1.5f else 1.5f,
                            dishDescription = stringResource(R.string.cd_dish),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BoardSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = 198.dp),
                shape = RoundedCornerShape(26.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(192.dp)
                                .alpha(0.12f + (state.heat / GameConfig.HEAT_MAX) * 0.58f)
                                .background(
                                    Brush.radialGradient(
                                        listOf(TerracottaFlame, Color.Transparent)
                                    ),
                                    CircleShape
                                )
                        )
                        Image(
                            painter = painterResource(R.drawable.sprite_3),
                            contentDescription = stringResource(R.string.cd_wok),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(150.dp)
                                .graphicsLayer {
                                    scaleX = wokScale.value
                                    scaleY = wokScale.value
                                }
                        )
                        if (state.lastPoints > 0 && gain.value in 0.001f..0.999f) {
                            Text(
                                text = stringResource(R.string.game_points_gain, state.lastPoints),
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = HerbDeep,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .alpha(1f - gain.value)
                                    .graphicsLayer { translationY = -gain.value * 40f }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.game_heat_value, state.heat),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ChiliRed,
                            modifier = Modifier.semantics {
                                liveRegion = LiveRegionMode.Polite
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    HeatGauge(
                        heat = state.heat,
                        target = state.targetHeat,
                        window = state.window,
                        maxHeat = GameConfig.HEAT_MAX,
                        highContrast = state.highContrast,
                        alert = shake.value in 0.001f..0.999f,
                        stateLabel = stringResource(
                            R.string.state_heat,
                            state.heat,
                            state.targetHeat - state.window,
                            state.targetHeat + state.window
                        ),
                        modifier = Modifier
                            .widthIn(max = 320.dp)
                            .fillMaxWidth()
                            .graphicsLayer { translationX = shakeOffset }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            StatStrip(state = state)

            Spacer(modifier = Modifier.height(10.dp))

            ControlsBlock(
                state = state,
                onGo = onGo,
                onHeatUp = onHeatUp,
                onCoolDown = onCoolDown,
                onServe = onServe
            )
        }
    }

    if (state.phase == GamePhase.PAUSED) {
        PauseDialog(
            onResume = onResume,
            onSettings = onSettings,
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onPause),
            shape = CircleShape,
            color = SurfaceCream,
            border = BorderStroke(1.5.dp, BorderSand),
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_shift, state.level),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(5.dp))
            if (state.numericTimer) {
                val whole = state.shiftRemaining.toInt().coerceAtLeast(0)
                Text(
                    text = stringResource(R.string.game_clock, whole / 60, whole % 60),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = timerColor(state),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(132.dp)
                        .height(6.dp)
                        .background(BorderSand, RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(
                                (state.shiftRemaining / state.shiftTotal).coerceIn(0f, 1f)
                            )
                            .height(6.dp)
                            .background(timerColor(state), RoundedCornerShape(3.dp))
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            for (slot in 1..GameConfig.MAX_HEARTS) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = null,
                    tint = if (slot <= state.hearts) ChiliRed else BorderSand,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

private fun timerColor(state: GameUiState): Color {
    val ratio = if (state.shiftTotal <= 0f) 0f else state.shiftRemaining / state.shiftTotal
    return when {
        ratio > 0.55f -> HerbGreen
        ratio > 0.25f -> TerracottaFlame
        else -> ChiliRed
    }
}

@Composable
private fun StatStrip(state: GameUiState) {
    val plates = stringResource(R.string.game_plates_value, state.plates, state.quota)
    val activeTicket = state.active
    val secondValue = if (activeTicket != null) {
        stringResource(R.string.game_heat_value, activeTicket.targetHeat)
    } else {
        stringResource(bandLabel(state.band))
    }
    val third = when {
        state.combo > 0 -> stringResource(R.string.game_combo_value, state.combo) to true
        state.coins > 0 -> state.coins.toString() to false
        else -> null
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            iconRes = R.drawable.ic_pip,
            label = stringResource(R.string.game_plates),
            value = plates,
            accent = HerbGreen,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        StatCard(
            iconRes = R.drawable.ic_star,
            label = stringResource(R.string.game_target),
            value = secondValue,
            accent = TerracottaFlame,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        if (third != null) {
            StatCard(
                iconRes = if (third.second) R.drawable.ic_bar else R.drawable.ic_coin,
                label = if (third.second) {
                    stringResource(R.string.game_combo)
                } else {
                    stringResource(R.string.game_coins)
                },
                value = third.first,
                accent = ChiliRed,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun ControlsBlock(
    state: GameUiState,
    onGo: () -> Unit,
    onHeatUp: () -> Unit,
    onCoolDown: () -> Unit,
    onServe: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = SurfaceCream,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            if (state.phase == GamePhase.IDLE) {
                Text(
                    text = stringResource(R.string.game_hint),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.leftHanded) {
                    CoolChip(enabled = state.running, onClick = onCoolDown)
                    HeatChip(enabled = state.running, onClick = onHeatUp)
                } else {
                    HeatChip(enabled = state.running, onClick = onHeatUp)
                    CoolChip(enabled = state.running, onClick = onCoolDown)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            val idle = state.phase == GamePhase.IDLE
            PrimaryButton(
                text = if (idle) {
                    stringResource(R.string.cta_go)
                } else {
                    stringResource(R.string.cta_serve)
                },
                onClick = if (idle) onGo else onServe,
                enabled = idle || state.canServe,
                disabledState = stringResource(R.string.state_disabled)
            )
        }
    }
}

@Composable
private fun RowScope.HeatChip(enabled: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) {
                        Brush.horizontalGradient(listOf(TerracottaFlame, ChiliRed))
                    } else {
                        Brush.horizontalGradient(listOf(BorderSand, BorderSand))
                    },
                    shape
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.cd_chili),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.cta_heat_up),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 13.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (enabled) TextOnAccent else TextTertiary
            )
        }
    }
}

@Composable
private fun RowScope.CoolChip(enabled: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(2.dp, if (enabled) HerbGreen else BorderSand),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PaperCream,
            disabledContainerColor = PaperCream
        ),
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_lime),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.cta_cool_down),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 13.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (enabled) HerbDeep else TextTertiary
            )
        }
    }
}

private fun bandLabel(band: ToleranceBand): Int = when (band) {
    ToleranceBand.WIDE -> R.string.band_wide
    ToleranceBand.TIGHT -> R.string.band_tight
    ToleranceBand.RAZOR -> R.string.band_razor
}
