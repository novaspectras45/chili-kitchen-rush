package com.Zihmaho6.Tekloc.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.core.theme.BorderSand
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.PaperCream
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary

@Composable
fun OrderTicketCard(
    name: String,
    rushLabel: String,
    heatPips: Int,
    maxPips: Int,
    rush: Boolean,
    rushRatio: Float,
    active: Boolean,
    tilt: Float,
    dishDescription: String,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (active) 1.06f else 0.96f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 320f),
        label = "ticketLift"
    )
    Card(
        modifier = modifier
            .width(104.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = tilt
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = PaperCream),
        border = BorderStroke(if (active) 2.dp else 1.dp, if (active) ChiliRed else BorderSand),
        elevation = CardDefaults.cardElevation(defaultElevation = if (active) 8.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pin),
                    contentDescription = null,
                    tint = if (rush) ChiliRed else TextTertiary,
                    modifier = Modifier.size(10.dp)
                )
            }
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = dishDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(34.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(20.dp)
            )
            ChiliPips(filled = heatPips, total = maxPips, pipSize = 7.dp)
            if (rush) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(ChiliRed.copy(alpha = 0.14f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(rushRatio.coerceIn(0f, 1f))
                            .height(12.dp)
                            .background(ChiliRed.copy(alpha = 0.55f), RoundedCornerShape(6.dp))
                    )
                    Text(
                        text = rushLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ChiliRed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
