package com.Zihmaho6.Tekloc.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Zihmaho6.Tekloc.core.theme.PaperCream
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TerracottaFlame
import com.Zihmaho6.Tekloc.core.theme.TextOnAccent
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextSecondary
import com.Zihmaho6.Tekloc.core.theme.TextTertiary
import com.Zihmaho6.Tekloc.core.ui.ScreenBackground
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton
import com.Zihmaho6.Tekloc.core.ui.SectionHeader
import com.Zihmaho6.Tekloc.presentation.dialog.ResetKitchenDialog

@Composable
fun SettingsRoute(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onBack = onBack,
        onLeftHanded = settingsViewModel::setLeftHanded,
        onNumericTimer = settingsViewModel::setNumericTimer,
        onHighContrast = settingsViewModel::setHighContrast,
        onReduceMotion = settingsViewModel::setReduceMotion,
        onAskReset = settingsViewModel::askReset,
        onDismissReset = settingsViewModel::dismissReset,
        onConfirmReset = settingsViewModel::confirmReset
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onLeftHanded: (Boolean) -> Unit,
    onNumericTimer: (Boolean) -> Unit,
    onHighContrast: (Boolean) -> Unit,
    onReduceMotion: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onDismissReset: () -> Unit,
    onConfirmReset: () -> Unit
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
                title = stringResource(R.string.settings_title),
                backDescription = stringResource(R.string.cd_menu),
                onBack = onBack,
                coins = state.coins.toString()
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    SegmentedRow(
                        title = stringResource(R.string.settings_hand),
                        note = stringResource(R.string.settings_hand_note),
                        leftText = stringResource(R.string.settings_hand_left),
                        rightText = stringResource(R.string.settings_hand_right),
                        leftSelected = state.leftHanded,
                        onLeft = { onLeftHanded(true) },
                        onRight = { onLeftHanded(false) }
                    )
                }
                item {
                    SegmentedRow(
                        title = stringResource(R.string.settings_timer),
                        note = stringResource(R.string.settings_timer_note),
                        leftText = stringResource(R.string.settings_timer_bar),
                        rightText = stringResource(R.string.settings_timer_seconds),
                        leftSelected = !state.numericTimer,
                        onLeft = { onNumericTimer(false) },
                        onRight = { onNumericTimer(true) }
                    )
                }
                item {
                    SwitchRow(
                        title = stringResource(R.string.settings_contrast),
                        note = stringResource(R.string.settings_contrast_note),
                        checked = state.highContrast,
                        onChange = onHighContrast
                    )
                }
                item {
                    SwitchRow(
                        title = stringResource(R.string.settings_motion),
                        note = stringResource(R.string.settings_motion_note),
                        checked = state.reduceMotion,
                        onChange = onReduceMotion
                    )
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
                        border = BorderStroke(1.5.dp, ChiliRed.copy(alpha = 0.4f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.settings_reset),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = stringResource(R.string.settings_reset_note),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 11.sp,
                                    color = TextTertiary
                                )
                            }
                            TextButton(
                                onClick = onAskReset,
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.cta_reset),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ChiliRed
                                )
                            }
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

    if (state.confirmReset) {
        ResetKitchenDialog(
            onConfirm = onConfirmReset,
            onDismiss = onDismissReset
        )
    }
}

@Composable
private fun SwitchRow(
    title: String,
    note: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    val stateLabel = if (checked) {
        stringResource(R.string.state_on)
    } else {
        stringResource(R.string.state_off)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(1.5.dp, BorderSand),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .semantics { stateDescription = stateLabel },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = TextTertiary
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Switch(
                checked = checked,
                onCheckedChange = onChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PaperCream,
                    checkedTrackColor = ChiliRed,
                    uncheckedThumbColor = TextTertiary,
                    uncheckedTrackColor = BorderSand
                )
            )
        }
    }
}

@Composable
private fun SegmentedRow(
    title: String,
    note: String,
    leftText: String,
    rightText: String,
    leftSelected: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCream),
        border = BorderStroke(1.5.dp, BorderSand),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Text(
                text = note,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SegmentChip(
                    text = leftText,
                    selected = leftSelected,
                    onClick = onLeft,
                    modifier = Modifier.weight(1f)
                )
                SegmentChip(
                    text = rightText,
                    selected = !leftSelected,
                    onClick = onRight,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SegmentChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedLabel = stringResource(R.string.state_selected)
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, if (selected) ChiliRed else BorderSand),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (selected) 3.dp else 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) ChiliRed else PaperCream,
            contentColor = if (selected) TextOnAccent else TextSecondary
        ),
        modifier = modifier
            .height(48.dp)
            .semantics { if (selected) stateDescription = selectedLabel }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
