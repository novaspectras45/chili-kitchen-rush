package com.Zihmaho6.Tekloc.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.core.theme.ChiliRed
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.theme.TextSecondary
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCream,
        shape = RoundedCornerShape(22.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        confirmButton = {
            SecondaryButton(
                text = confirmText,
                onClick = onConfirm,
                accent = ChiliRed
            )
        },
        dismissButton = {
            SecondaryButton(
                text = dismissText,
                onClick = onDismiss
            )
        }
    )
}

@Composable
fun ResetKitchenDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmDialog(
        title = stringResource(R.string.settings_reset),
        message = stringResource(R.string.settings_reset_confirm),
        confirmText = stringResource(R.string.cta_reset),
        dismissText = stringResource(R.string.cta_cancel),
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}
