package com.Zihmaho6.Tekloc.presentation.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Zihmaho6.Tekloc.core.di.ServiceLocator
import com.Zihmaho6.Tekloc.core.theme.BaseCream
import com.Zihmaho6.Tekloc.core.theme.BoardCream
import com.Zihmaho6.Tekloc.core.theme.BorderSand
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.EmberDark
import com.Zihmaho6.Tekloc.core.theme.EmberMid
import com.Zihmaho6.Tekloc.core.theme.HerbGreen
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextOnAccent
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.ui.BoardSurface
import com.Zihmaho6.Tekloc.core.ui.PrimaryButton
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton
import com.Zihmaho6.Tekloc.core.ui.StatCard
import com.Zihmaho6.Tekloc.domain.model.ShiftOutcome
import com.Zihmaho6.Tekloc.domain.model.ShiftResult

@Composable
fun ResultRoute(
    shift: ShiftResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onCollection: () -> Unit
) {
    val resultViewModel: ResultViewModel = viewModel(factory = ServiceLocator.factory)
    val state by resultViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(shift) {
        resultViewModel.present(shift)
    }
    ResultScreen(
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu,
        onCollection = onCollection
    )
}

@Composable
fun ResultScreen(
    state: ResultUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    onCollection: () -> Unit
) {
    val starPop = remember { Animatable(0.4f) }
    LaunchedEffect(state.stars, state.score) {
        starPop.snapTo(0.4f)
        starPop.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = 260f))
    }

    val overlay = if (state.cleared) {
        listOf(
            BaseCream.copy(alpha = 0.95f),
            BoardCream.copy(alpha = 0.97f),
            BorderSand.copy(alpha = 0.98f)
        )
    } else {
        listOf(
            EmberDark.copy(alpha = 0.92f),
            EmberMid.copy(alpha = 0.95f),
            EmberDark.copy(alpha = 0.97f)
        )
    }
    val headline = when (state.outcome) {
        ShiftOutcome.QUOTA_MET -> R.string.result_clean
        ShiftOutcome.TIMER_OUT -> R.string.result_bell
        ShiftOutcome.HEARTS_LOST -> R.string.result_burned
    }
    val titleColor = if (state.cleared) TextPrimary else TextOnAccent

    ScreenBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            BoardSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp),
                shape = RoundedCornerShape(24.dp),
                container = if (state.cleared) BoardCream else EmberMid,
                elevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = stringResource(R.string.cd_dish),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(116.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(headline),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = titleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (slot in 1..3) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = null,
                        tint = if (slot <= state.stars) TerracottaFlame else BorderSand,
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer {
                                val pop = if (slot <= state.stars) starPop.value else 1f
                                scaleX = pop
                                scaleY = pop
                            }
                    )
                }
            }

            if (state.newRecord) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.result_record),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextOnAccent,
                    modifier = Modifier
                        .background(ChiliRed, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    iconRes = R.drawable.ic_pip,
                    label = stringResource(R.string.game_plates),
                    value = stringResource(R.string.game_plates_value, state.plates, state.quota),
                    accent = HerbGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    iconRes = R.drawable.ic_bar,
                    label = stringResource(R.string.result_score),
                    value = state.score.toString(),
                    accent = TerracottaFlame,
                    modifier = Modifier.weight(1f)
                )
                if (state.perfects > 0) {
                    StatCard(
                        iconRes = R.drawable.ic_star,
                        label = stringResource(R.string.result_perfect),
                        value = state.perfects.toString(),
                        accent = ChiliRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (state.bestCombo > 0 || state.coins > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        iconRes = R.drawable.ic_grid,
                        label = stringResource(R.string.result_best_combo),
                        value = stringResource(R.string.game_combo_value, state.bestCombo),
                        accent = TerracottaFlame,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        iconRes = R.drawable.ic_coin,
                        label = stringResource(R.string.result_coins),
                        value = state.coins.toString(),
                        accent = ChiliRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.cta_play_again),
                onClick = onPlayAgain
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.cta_collection),
                    onClick = onCollection,
                    accent = ChiliRed,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}
