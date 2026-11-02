package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoGenerationMode
import com.example.model.VideoProject
import com.example.ui.components.CreatorBadge
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentMint
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkObsidian
import androidx.compose.ui.text.style.TextOverflow
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple

data class DashboardModuleItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badge: String? = null,
    val accentColor: Color = ElectricCyan
)

@Composable
fun DashboardScreen(
    remainingCredits: Int,
    creatorPhotoUri: String?,
    projects: List<VideoProject>,
    onModuleClick: (String) -> Unit,
    onProjectClick: (VideoProject) -> Unit,
    onSettingsClick: () -> Unit,
    onCreditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modules = listOf(
        DashboardModuleItem(
            id = "TEXT_TO_VIDEO",
            title = "Text to Video",
            description = "AI prompt to 8K cinematic footage",
            icon = Icons.Default.Movie,
            badge = "Google Veo 2",
            accentColor = ElectricCyan
        ),
        DashboardModuleItem(
            id = "IMAGE_TO_VIDEO",
            title = "Image to Video",
            description = "Bring still photos to life with motion",
            icon = Icons.Default.Image,
            badge = "Fluid Motion",
            accentColor = VividPurple
        ),
        DashboardModuleItem(
            id = "AI_CARTOON",
            title = "AI Cartoon Video",
            description = "Anime, 3D Pixar, & hand-drawn styles",
            icon = Icons.Default.Animation,
            badge = "Stylized",
            accentColor = AccentPink
        ),
        DashboardModuleItem(
            id = "SHORTS_MAKER",
            title = "YouTube Shorts Maker",
            description = "9:16 viral vertical format & hooks",
            icon = Icons.Default.Subscriptions,
            badge = "9:16 Viral",
            accentColor = AccentGold
        ),
        DashboardModuleItem(
            id = "AI_MUSIC_VOICE",
            title = "AI Music & Voice",
            description = "Soundtracks & movie trailer voiceover",
            icon = Icons.Default.MusicNote,
            badge = "HQ Audio",
            accentColor = AccentMint
        ),
        DashboardModuleItem(
            id = "VIDEO_EDITING",
            title = "Video Editing",
            description = "MatchCut transition & speed ramps",
            icon = Icons.Default.ContentCut,
            badge = "Studio Tools",
            accentColor = ElectricBlue
        ),
        DashboardModuleItem(
            id = "MY_PROJECTS",
            title = "My Projects",
            description = "Saved drafts, renders & timeline assets",
            icon = Icons.Default.FolderSpecial,
            badge = "${projects.size} items",
            accentColor = ElectricCyan
        ),
        DashboardModuleItem(
            id = "DOWNLOAD_VIDEOS",
            title = "Download Videos",
            description = "Export MP4 in 720p, 1080p, and 4K",
            icon = Icons.Default.Download,
            badge = "Export Hub",
            accentColor = AccentMint
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidian)
            .statusBarsPadding()
            .testTag("dashboard_screen")
    ) {
        // Top Studio Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Creator Compact Badge
            CreatorBadge(
                creatorName = "Manas Tripathi",
                photoUri = creatorPhotoUri,
                onPhotoSelected = {},
                isCompact = true
            )

            // Right Actions: Credits Pill and Settings
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = DarkSurfaceElevated,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clickable { onCreditClick() }
                        .testTag("credit_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (remainingCredits > 0) AccentMint else AccentPink)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$remainingCredits / 5 Credits",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (remainingCredits > 0) ElectricCyan else AccentPink
                        )
                    }
                }

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceElevated)
                        .testTag("settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings & AI Keys",
                        tint = TextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Welcome Studio Hero Banner Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .border(1.5.dp, ElectricGradient, RoundedCornerShape(20.dp))
                        .shadow(12.dp, RoundedCornerShape(20.dp))
                        .clickable { onModuleClick("TEXT_TO_VIDEO") }
                        .testTag("hero_create_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = ElectricCyan.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "MANAS-MATCHCUT AI STUDIO",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricCyan,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = AccentGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Generate Cinematic AI Video",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Powered by Google Veo 2, Imagen 3 & Gemini Director. Describe your vision or animate a photo in seconds.",
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Start Generation",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Studio Features Grid Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Creation Studio",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "8 AI Engines",
                        fontSize = 12.sp,
                        color = ElectricCyan
                    )
                }
            }

            // 8 Modules Grid
            items(modules.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (module in pair) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
                                .clickable { onModuleClick(module.id) }
                                .testTag("module_${module.id.lowercase()}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(module.accentColor.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = module.icon,
                                            contentDescription = module.title,
                                            tint = module.accentColor,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    if (module.badge != null) {
                                        Surface(
                                            color = module.accentColor.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = module.badge,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = module.accentColor,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = module.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = module.description,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp,
                                    color = TextSecondary,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Recent Projects Showcase Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recent Projects",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        color = ElectricCyan,
                        modifier = Modifier.clickable { onModuleClick("MY_PROJECTS") }
                    )
                }
            }

            if (projects.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No videos generated yet",
                                fontSize = 14.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Tap Text to Video or Image to Video to create your first scene!",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(projects) { proj ->
                            Card(
                                modifier = Modifier
                                    .width(200.dp)
                                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                                    .clickable { onProjectClick(proj) }
                                    .testTag("recent_project_${proj.id}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                            ) {
                                Column {
                                    // Thumbnail Simulation Box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(DarkSurfaceBorder, DarkObsidian)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play",
                                            tint = ElectricCyan,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(DarkObsidian.copy(alpha = 0.7f))
                                                .padding(6.dp)
                                        )

                                        Surface(
                                            color = DarkObsidian.copy(alpha = 0.8f),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(6.dp)
                                        ) {
                                            Text(
                                                text = "${proj.durationSeconds}s • ${proj.aspectRatio}",
                                                fontSize = 9.sp,
                                                color = TextPrimary,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = proj.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = proj.modelName,
                                            fontSize = 11.sp,
                                            color = ElectricCyan,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
