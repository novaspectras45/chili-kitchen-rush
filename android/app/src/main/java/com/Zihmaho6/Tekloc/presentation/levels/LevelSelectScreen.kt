package com.Zihmaho6.Tekloc.presentation.levels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
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
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton
import com.Zihmaho6.Tekloc.core.ui.SectionHeader
import com.Zihmaho6.Tekloc.domain.model.ToleranceBand

@Composable
fun LevelSelectRoute(
    onBack: () -> Unit,
    onPickLevel: (Int) -> Unit
) {
    val levelsViewModel: LevelSelectViewModel = viewModel(factory = ServiceLocator.factory)
    val state by levelsViewModel.state.collectAsStateWithLifecycle()
    LevelSelectScreen(state = state, onBack = onBack, onPickLevel = onPickLevel)
}

@Composable
fun LevelSelectScreen(
    state: LevelSelectUiState,
    onBack: () -> Unit,
    onPickLevel: (Int) -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            BaseCream.copy(alpha = 0.94f),
            SurfaceCream.copy(alpha = 0.97f),
            BoardCream.copy(alpha = 0.98f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            SectionHeader(
                title = stringResource(R.string.level_select_title),
                backDescription = stringResource(R.string.cd_menu),
                onBack = onBack,
                coins = state.coins.toString()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.levels, key = { it.index }) { card ->
                    LevelTile(card = card, onPick = { onPickLevel(card.index) })
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LevelTile(card: LevelCard, onPick: () -> Unit) {
    val lockedLabel = stringResource(R.string.state_locked)
    val base = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
    val tileModifier = if (card.unlocked) {
        base.clickable(onClick = onPick)
    } else {
        base.semantics { stateDescription = lockedLabel }
    }
    Card(
        modifier = tileModifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(2.dp, if (card.unlocked) ChiliRed else BorderSand),
        elevation = CardDefaults.cardElevation(defaultElevation = if (card.unlocked) 5.dp else 1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (card.unlocked) 1f else 0.45f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (card.unlocked) {
                    Text(
                        text = stringResource(R.string.level_number, card.index),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (slot in 1..3) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            tint = if (slot <= card.stars) TerracottaFlame else BorderSand,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(bandLabel(card.band)),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = TextTertiary
                )
            }
        }
    }
}

private fun bandLabel(band: ToleranceBand): Int = when (band) {
    ToleranceBand.WIDE -> R.string.band_wide
    ToleranceBand.TIGHT -> R.string.band_tight
    ToleranceBand.RAZOR -> R.string.band_razor
}
