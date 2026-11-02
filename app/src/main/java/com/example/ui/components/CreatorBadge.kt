package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentMint
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple

@Composable
fun CreatorBadge(
    creatorName: String = "Manas Tripathi",
    photoUri: String?,
    onPhotoSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onPhotoSelected(uri.toString())
        }
    }

    if (isCompact) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(24.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(24.dp))
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("creator_compact_badge"),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, ElectricGradient, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (!photoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Creator $creatorName",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Brush.radialGradient(listOf(ElectricCyan, VividPurple))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkObsidian
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = creatorName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Creator",
                        tint = ElectricCyan,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Text(
                    text = "Lead Architect",
                    fontSize = 10.sp,
                    color = AccentMint
                )
            }
        }
    } else {
        // Full Welcome Screen Creator Showcase Card
        Surface(
            modifier = modifier
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, ElectricGradient, RoundedCornerShape(24.dp))
                .testTag("creator_full_showcase"),
            color = DarkSurfaceElevated
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar with Glowing Ring and Camera Picker Button
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(DarkObsidian)
                            .border(3.dp, ElectricGradient, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!photoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = photoUri,
                                contentDescription = "Creator Real Photo: $creatorName",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            // High-end stylized typographic portrait badge
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                ElectricCyan.copy(alpha = 0.85f),
                                                VividPurple.copy(alpha = 0.85f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "MT",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DarkObsidian,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "CREATOR",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkObsidian
                                    )
                                }
                            }
                        }
                    }

                    // Camera pick action button overlay
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ElectricCyan)
                            .border(2.dp, DarkObsidian, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Set Creator Real Photo",
                            tint = DarkObsidian,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = creatorName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Creator",
                        tint = ElectricCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = ElectricCyan.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Created by Manas Tripathi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ElectricCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (photoUri != null) "Real photo loaded • Recognizable portrait preserved"
                    else "Tap avatar to load your real photo from gallery",
                    fontSize = 11.sp,
                    color = if (photoUri != null) AccentMint else TextSecondary
                )
            }
        }
    }
}
