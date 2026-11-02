package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.VideoProject
import com.example.ui.theme.AccentMint
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FilmStripBorderBrush
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CinematicVideoPlayer(
    project: VideoProject,
    modifier: Modifier = Modifier,
    onDownloadClick: () -> Unit = {},
    showControls: Boolean = true
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var isMuted by remember { mutableStateOf(false) }
    var controlsVisible by remember { mutableStateOf(true) }

    val durationMs = (project.durationSeconds * 1000).toLong().coerceAtLeast(3000L)

    // Playback loop
    LaunchedEffect(isPlaying, durationMs) {
        while (isPlaying) {
            delay(50)
            currentProgress += 50f / durationMs
            if (currentProgress >= 1f) {
                currentProgress = 0f
            }
        }
    }

    // Auto-hide controls after 3 seconds of playing
    LaunchedEffect(controlsVisible, isPlaying) {
        if (controlsVisible && isPlaying) {
            delay(3500)
            controlsVisible = false
        }
    }

    val aspectMultiplier = when (project.aspectRatio) {
        "9:16" -> 9f / 16f
        "16:9" -> 16f / 9f
        else -> 1f
    }

    // Infinite cinematic wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "video_motion")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkObsidian)
            .border(1.5.dp, FilmStripBorderBrush, RoundedCornerShape(20.dp))
            .clickable { controlsVisible = !controlsVisible }
            .testTag("cinematic_video_player"),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectMultiplier)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // If user uploaded an image for image-to-video, animate with parallax/zoom
            if (!project.thumbnailUri.isNullOrBlank()) {
                val imageScale = 1.0f + (currentProgress * 0.15f)
                AsyncImage(
                    model = project.thumbnailUri,
                    contentDescription = "Source Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(aspectMultiplier)
                )
            }

            // Cinematic procedural motion canvas overlay
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("video_motion_canvas")
            ) {
                val w = size.width
                val h = size.height

                // Dark atmospheric gradient background
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkObsidian.copy(alpha = if (project.thumbnailUri != null) 0.35f else 0.95f),
                            DarkSurface.copy(alpha = if (project.thumbnailUri != null) 0.45f else 0.95f),
                            Color.Black.copy(alpha = if (project.thumbnailUri != null) 0.55f else 1.0f)
                        )
                    )
                )

                // Neon Cyber / Cinematic Light Rays
                val primaryColor = when (project.style) {
                    "Cyberpunk Sci-Fi" -> ElectricCyan
                    "Anime / Studio Ghibli" -> AccentMint
                    "AI Cartoon" -> AccentPink
                    "3D Pixar Style" -> VividPurple
                    else -> ElectricBlue
                }
                val secondaryColor = when (project.style) {
                    "Cyberpunk Sci-Fi" -> VividPurple
                    "Anime / Studio Ghibli" -> AccentPink
                    "AI Cartoon" -> AccentMint
                    "3D Pixar Style" -> AccentPink
                    else -> ElectricCyan
                }

                // Dynamic motion waveform ribbons
                val path1 = Path()
                val path2 = Path()
                val steps = 40
                val dx = w / steps

                path1.moveTo(0f, h * 0.5f)
                path2.moveTo(0f, h * 0.65f)

                for (i in 0..steps) {
                    val x = i * dx
                    val angle = (i.toFloat() / steps * 6.28f * 2) + waveOffset + (currentProgress * 3f)
                    val y1 = h * 0.5f + (sin(angle) * 35f * glowPulse)
                    val y2 = h * 0.65f + (cos(angle) * 45f * (1.2f - glowPulse))
                    path1.lineTo(x, y1)
                    path2.lineTo(x, y2)
                }

                drawPath(
                    path = path1,
                    brush = Brush.horizontalGradient(listOf(primaryColor, secondaryColor)),
                    style = Stroke(width = 3.5.dp.toPx())
                )
                drawPath(
                    path = path2,
                    brush = Brush.horizontalGradient(listOf(secondaryColor, primaryColor)),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Cinematic particle motes
                for (i in 0..20) {
                    val px = (w * ((i * 17 + (waveOffset * 30)) % 100) / 100f)
                    val py = (h * ((i * 29 + (currentProgress * h)) % h) / h)
                    drawCircle(
                        color = if (i % 2 == 0) primaryColor.copy(alpha = 0.5f * glowPulse) else secondaryColor.copy(alpha = 0.4f),
                        radius = (2.dp + (i % 4).dp).toPx(),
                        center = Offset(px, py)
                    )
                }

                // Cinematic letterbox / camera lines
                drawLine(
                    color = Color.White.copy(alpha = 0.15f),
                    start = Offset(w * 0.1f, h * 0.15f),
                    end = Offset(w * 0.1f + 30f, h * 0.15f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.15f),
                    start = Offset(w * 0.9f, h * 0.85f),
                    end = Offset(w * 0.9f - 30f, h * 0.85f),
                    strokeWidth = 2f
                )
            }

            // Top Status Bar: Live Model & Resolution Tag
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = DarkObsidian.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) AccentMint else AccentPink)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${project.modelName} • ${project.resolution}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }

                // Watermark & Audio indicator
                Surface(
                    color = DarkObsidian.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Audio Track",
                            tint = ElectricCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = project.musicTrack ?: "MatchCut Audio",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }
                }
            }

            // Subtle Cinematic Watermark
            Text(
                text = "Manas-MatchCut AI • By Manas Tripathi",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.45f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = if (controlsVisible) 64.dp else 12.dp)
            )

            // Center Play/Pause button on tap
            AnimatedVisibility(
                visible = controlsVisible || !isPlaying,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(12.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(ElectricCyan, VividPurple)))
                        .testTag("play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = DarkObsidian,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Bottom Controls Bar
            AnimatedVisibility(
                visible = controlsVisible && showControls,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, DarkObsidian.copy(alpha = 0.95f))
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    // Timeline Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentSec = (currentProgress * project.durationSeconds).toInt()
                        Text(
                            text = String.format("%02d:%02d", currentSec / 60, currentSec % 60),
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.width(36.dp)
                        )

                        Slider(
                            value = currentProgress,
                            onValueChange = {
                                currentProgress = it
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(28.dp)
                                .testTag("video_timeline_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = ElectricCyan,
                                activeTrackColor = ElectricCyan,
                                inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                            )
                        )

                        val totalSec = project.durationSeconds
                        Text(
                            text = String.format("%02d:%02d", totalSec / 60, totalSec % 60),
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.width(36.dp)
                        )
                    }

                    // Action buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { currentProgress = 0f; isPlaying = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = "Restart",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = { isMuted = !isMuted },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                    contentDescription = "Toggle Mute",
                                    tint = if (isMuted) AccentPink else TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_SUBJECT, project.title)
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "🎬 Check out my AI video '${project.title}' created with Manas-MatchCut by Manas Tripathi!\n\nPrompt: ${project.prompt}"
                                        )
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onDownloadClick,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("download_video_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download Video",
                                    tint = AccentMint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
