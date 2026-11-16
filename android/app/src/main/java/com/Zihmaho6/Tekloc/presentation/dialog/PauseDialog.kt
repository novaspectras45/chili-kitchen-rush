package com.Zihmaho6.Tekloc.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Zihmaho6.Tekloc.R
import com.Zihmaho6.Tekloc.core.theme.SurfaceCream
import com.Zihmaho6.Tekloc.core.theme.TextPrimary
import com.Zihmaho6.Tekloc.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onSettings: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        containerColor = SurfaceCream,
        shape = RoundedCornerShape(22.dp),
        title = {
            Text(
                text = stringResource(R.string.dialog_paused),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cta_resume),
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    text = stringResource(R.string.cta_settings),
                    onClick = onSettings,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu
            )
        }
    )
}
