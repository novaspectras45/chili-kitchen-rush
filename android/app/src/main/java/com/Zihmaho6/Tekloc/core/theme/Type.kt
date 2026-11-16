package com.Zihmaho6.Tekloc.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    displaySmall = TextStyle(
        fontSize = 32.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 2.5.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        lineHeight = 29.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 23.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 15.sp,
        lineHeight = 21.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.2.sp
    )
)
