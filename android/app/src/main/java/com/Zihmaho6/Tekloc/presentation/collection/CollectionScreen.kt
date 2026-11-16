package com.Zihmaho6.Tekloc.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.Zihmaho6.Tekloc.core.theme.TextSecondary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary
import com.Zihmaho6.Tekloc.core.ui.ChiliPips
import com.Zihmaho6.Tekloc.core.ui.EmptyStateMessage
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton
import com.Zihmaho6.Tekloc.core.ui.SectionHeader

@Composable
fun CollectionRoute(onBack: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()
    CollectionScreen(state = state, onBack = onBack)
}

@Composable
fun CollectionScreen(
    state: CollectionUiState,
    onBack: () -> Unit
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
                title = stringResource(R.string.collection_title),
                backDescription = stringResource(R.string.cd_menu),
                onBack = onBack,
                coins = state.coins.toString()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.unlocked == 0) {
                    EmptyStateMessage(text = stringResource(R.string.collection_empty))
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.recipes, key = { it.id }) { card ->
                            RecipeTile(card = card)
                        }
                    }
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
private fun RecipeTile(card: RecipeCard) {
    val lockedLabel = stringResource(R.string.state_locked)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .semantics { if (!card.unlocked) stateDescription = lockedLabel },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(1.5.dp, if (card.unlocked) ChiliRed.copy(alpha = 0.45f) else BorderSand),
        elevation = CardDefaults.cardElevation(defaultElevation = if (card.unlocked) 5.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .alpha(if (card.unlocked) 1f else 0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                if (card.unlocked) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = stringResource(R.string.cd_dish),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(46.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(card.nameRes),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(5.dp))
            ChiliPips(filled = card.signatureHeat, total = 10, pipSize = 7.dp)
            Spacer(modifier = Modifier.height(6.dp))
            if (card.unlocked) {
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    for (slot in 1..3) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            tint = if (slot <= card.mastery) TerracottaFlame else BorderSand,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(card.noteRes),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    color = TextSecondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = stringResource(R.string.collection_locked),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextTertiary
                )
            }
        }
    }
}
