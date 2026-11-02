package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.components.CinematicVideoPlayer
import com.example.ui.theme.AccentMint
import com.example.ui.theme.AccentPink
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
fun VideoEditingScreen(
    project: VideoProject,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTransition by remember { mutableStateOf("MatchCut: Shape Align") }
    var speedRate by remember { mutableFloatStateOf(1.0f) }
    var selectedLut by remember { mutableStateOf("Teal & Orange") }
    var selectedRatio by remember { mutableStateOf(project.aspectRatio) }

    val transitions = listOf(
        "MatchCut: Shape Align",
        "MatchCut: Motion Whip",
        "MatchCut: Eye Trace",
        "Zoom In Morph",
        "Glitch Dissolve"
    )

    val luts = listOf(
        "Teal & Orange",
        "Cyber Neon 2077",
        "Golden Sunset",
        "Film Noir B&W",
        "Retro Kodachrome"
    )

    val speeds = listOf(0.5f, 1.0f, 1.5f, 2.0f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("video_editing_screen")
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
                    text = "MatchCut Video Editor",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Cinematic Transitions & Grading • Manas Tripathi",
                    fontSize = 11.sp,
                    color = ElectricCyan
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Live Video Preview with edited ratio
            CinematicVideoPlayer(
                project = project.copy(
                    aspectRatio = selectedRatio,
                    style = selectedLut
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Signature MatchCut Transitions
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCut,
                    contentDescription = null,
                    tint = ElectricCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Signature MatchCut Transitions",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transitions) { trans ->
                    val isSelected = selectedTransition == trans
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTransition = trans },
                        label = { Text(trans, fontSize = 11.sp) },
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

            // Color Grading LUTs
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = VividPurple, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Color Grading LUTs", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(luts) { lut ->
                    val isSelected = selectedLut == lut
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedLut = lut },
                        label = { Text(lut, fontSize = 11.sp) },
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

            // Speed Ramping
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = AccentMint, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Speed Ramping", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (speed in speeds) {
                    val isSelected = speedRate == speed
                    Surface(
                        color = if (isSelected) AccentMint else DarkSurfaceCard,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { speedRate = speed }
                    ) {
                        Text(
                            text = "${speed}x",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) DarkObsidian else TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Edited video exported with $selectedTransition & $selectedLut!", Toast.LENGTH_SHORT).show()
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(ElectricGradient, RoundedCornerShape(14.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = DarkObsidian)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Render & Export MatchCut Cut", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkObsidian)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
