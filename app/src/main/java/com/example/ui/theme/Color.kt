package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Manas-MatchCut Electric Blue & Neon Purple Dark Theme Palette
val ElectricCyan = Color(0xFF00E5FF)
val ElectricBlue = Color(0xFF0091FF)
val ElectricBlueDark = Color(0xFF0052CC)

val VividPurple = Color(0xFF9D00FF)
val NeonPurpleLight = Color(0xFFC77DFF)
val DeepViolet = Color(0xFF5A189A)

val DarkObsidian = Color(0xFF080811)
val DarkSurface = Color(0xFF101222)
val DarkSurfaceElevated = Color(0xFF181A32)
val DarkSurfaceCard = Color(0xFF1E213D)
val DarkSurfaceBorder = Color(0xFF2E3258)

val AccentPink = Color(0xFFFF007F)
val AccentMint = Color(0xFF00F5D4)
val AccentGold = Color(0xFFFFD166)

val TextPrimary = Color(0xFFF0F3FF)
val TextSecondary = Color(0xFF9BA3C7)
val TextTertiary = Color(0xFF6B7294)

// Signature Gradients
val ElectricGradient = Brush.horizontalGradient(
    colors = listOf(ElectricCyan, VividPurple)
)

val ElectricGradientVertical = Brush.verticalGradient(
    colors = listOf(ElectricCyan, VividPurple)
)

val CardGlowGradient = Brush.linearGradient(
    colors = listOf(ElectricCyan.copy(alpha = 0.25f), VividPurple.copy(alpha = 0.25f))
)

val StudioSurfaceGradient = Brush.verticalGradient(
    colors = listOf(DarkSurfaceElevated, DarkSurface)
)

val FilmStripBorderBrush = Brush.horizontalGradient(
    colors = listOf(ElectricCyan, VividPurple, AccentPink)
)
