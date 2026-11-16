package com.Zihmaho6.Tekloc.presentation.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.Zihmaho6.Tekloc.core.theme.HerbGreen
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextOnAccent
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextSecondary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton
import com.Zihmaho6.Tekloc.core.ui.SectionHeader
import com.Zihmaho6.Tekloc.core.ui.StatCard
import com.Zihmaho6.Tekloc.domain.model.Station

@Composable
fun StatsRoute(onBack: () -> Unit) {
    val statsViewModel: StatsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by statsViewModel.state.collectAsStateWithLifecycle()
    StatsScreen(
        state = state,
        onBack = onBack,
        onUpgrade = statsViewModel::upgrade
    )
}

@Composable
fun StatsScreen(
    state: StatsUiState,
    onBack: () -> Unit,
    onUpgrade: (Station) -> Unit
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
                title = stringResource(R.string.stats_title),
                backDescription = stringResource(R.string.cd_menu),
                onBack = onBack,
                coins = state.coins.toString()
            )

            val visible = listOf(
                state.shifts > 0,
                state.plates > 0,
                state.perfectRate > 0,
                state.stars > 0
            ).count { it }
            if (visible >= 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.shifts > 0) {
                        StatCard(
                            iconRes = R.drawable.ic_bar,
                            label = stringResource(R.string.stats_shifts),
                            value = state.shifts.toString(),
                            accent = TerracottaFlame,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.plates > 0) {
                        StatCard(
                            iconRes = R.drawable.ic_pip,
                            label = stringResource(R.string.stats_plates),
                            value = state.plates.toString(),
                            accent = HerbGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.perfectRate > 0) {
                        StatCard(
                            iconRes = R.drawable.ic_star,
                            label = stringResource(R.string.stats_rate),
                            value = stringResource(R.string.stats_rate_value, state.perfectRate),
                            accent = ChiliRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.stars > 0) {
                        StatCard(
                            iconRes = R.drawable.ic_star,
                            label = stringResource(R.string.stats_stars),
                            value = state.stars.toString(),
                            accent = TerracottaFlame,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            Text(
                text = stringResource(R.string.stats_stations),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.stations, key = { it.station.name }) { row ->
                    StationCard(row = row, onUpgrade = { onUpgrade(row.station) })
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
private fun StationCard(row: StationRow, onUpgrade: () -> Unit) {
    val shortLabel = stringResource(R.string.station_short)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(1.5.dp, BorderSand),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(row.nameRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        for (slot in 1..3) {
                            Icon(
                                painter = painterResource(R.drawable.ic_pip),
                                contentDescription = null,
                                tint = if (slot <= row.tier) ChiliRed else BorderSand,
                                modifier = Modifier.size(9.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(row.effectRes, row.effectValue),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            if (row.maxed) {
                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .width(96.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.cta_maxed),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = HerbGreen
                    )
                }
            } else {
                Button(
                    onClick = onUpgrade,
                    enabled = row.affordable,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChiliRed,
                        contentColor = TextOnAccent,
                        disabledContainerColor = BorderSand,
                        disabledContentColor = TextTertiary
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .width(112.dp)
                        .semantics {
                            if (!row.affordable) stateDescription = shortLabel
                        }
                ) {
                    Text(
                        text = stringResource(R.string.cta_upgrade, row.cost),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Unspecified
                    )
                }
            }
        }
    }
}
