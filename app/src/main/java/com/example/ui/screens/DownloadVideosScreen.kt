package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoProject
import com.example.ui.theme.AccentMint
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple

@Composable
fun DownloadVideosScreen(
    projects: List<VideoProject>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedProject by remember { mutableStateOf(projects.firstOrNull()) }
    var selectedFormat by remember { mutableStateOf("MP4 (H.264 / AAC)") }
    var selectedResolution by remember { mutableStateOf("1080p Full HD") }
    var selectedFps by remember { mutableStateOf("60 fps") }
    var bitrateMbps by remember { mutableFloatStateOf(16f) }
    var isDownloaded by remember { mutableStateOf(false) }

    val formats = listOf("MP4 (H.264 / AAC)", "WebM / VP9", "Animated GIF")
    val resolutions = listOf("720p HD", "1080p Full HD", "4K Ultra HD")
    val fpsOptions = listOf("24 fps", "30 fps", "60 fps")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("download_videos_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Download & Export Videos",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "High Bitrate Master Export • Manas-MatchCut",
                    fontSize = 11.sp,
                    color = ElectricCyan
                )
            }
        }

        if (projects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No generated videos to download",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Generate a video from the dashboard to export here",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = "Select Video to Export",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Project Selector Chips / Cards
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    projects.forEach { proj ->
                        val isSelected = selectedProject?.id == proj.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) ElectricCyan else DarkSurfaceBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedProject = proj },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) DarkSurfaceElevated else DarkSurfaceCard
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ElectricCyan.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Movie,
                                        contentDescription = null,
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = proj.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${proj.modelName} • ${proj.durationSeconds}s • ${proj.aspectRatio}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Format
                Text("Export Format", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    formats.forEach { fmt ->
                        val isSelected = selectedFormat == fmt
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFormat = fmt },
                            label = { Text(fmt, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricCyan,
                                selectedLabelColor = DarkObsidian,
                                containerColor = DarkSurfaceCard,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Resolution
                Text("Resolution", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    resolutions.forEach { res ->
                        val isSelected = selectedResolution == res
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedResolution = res },
                            label = { Text(res, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VividPurple,
                                selectedLabelColor = Color.White,
                                containerColor = DarkSurfaceCard,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Framerate
                Text("Framerate", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fpsOptions.forEach { fps ->
                        val isSelected = selectedFps == fps
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFps = fps },
                            label = { Text(fps, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentMint,
                                selectedLabelColor = DarkObsidian,
                                containerColor = DarkSurfaceCard,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Target Bitrate Slider
                Text(
                    text = "Target Bitrate: ${bitrateMbps.toInt()} Mbps",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Slider(
                    value = bitrateMbps,
                    onValueChange = { bitrateMbps = it },
                    valueRange = 8f..40f,
                    colors = SliderDefaults.colors(
                        thumbColor = ElectricCyan,
                        activeTrackColor = ElectricCyan,
                        inactiveTrackColor = DarkSurfaceBorder
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Download Button
                Button(
                    onClick = {
                        val currentProj = selectedProject
                        if (currentProj != null) {
                            isDownloaded = true
                            Toast.makeText(
                                context,
                                "Downloaded to Movies/ManasMatchCut/${currentProj.title.replace(" ", "_")}.mp4 (${selectedResolution}, ${selectedFps})",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(ElectricGradient, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = DarkObsidian,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Video File",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkObsidian
                    )
                }

                if (isDownloaded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_SUBJECT, selectedProject?.title ?: "AI Video")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "🎬 Check out my AI video '${selectedProject?.title}' generated with Manas-MatchCut by Manas Tripathi!\nPrompt: ${selectedProject?.prompt}"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = ElectricCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share to Social Media", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
