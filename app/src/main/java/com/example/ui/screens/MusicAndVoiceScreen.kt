package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentMint
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGradient
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple
import kotlinx.coroutines.delay

@Composable
fun MusicAndVoiceScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Music, 1: Voiceover

    // Music State
    var selectedGenre by remember { mutableStateOf("Cyberpunk Synthwave") }
    var selectedMood by remember { mutableStateOf("Epic & Cinematic") }
    var tempoBpm by remember { mutableFloatStateOf(128f) }
    var isMusicPlaying by remember { mutableStateOf(false) }

    // Voiceover State
    var voiceScript by remember {
        mutableStateOf("In a world transformed by artificial intelligence, your imagination becomes reality with Manas-MatchCut.")
    }
    var selectedVoice by remember { mutableStateOf("Deep Movie Trailer") }
    var voicePitch by remember { mutableFloatStateOf(1.0f) }
    var voiceSpeed by remember { mutableFloatStateOf(1.0f) }
    var isVoicePlaying by remember { mutableStateOf(false) }

    val genres = listOf(
        "Cyberpunk Synthwave",
        "Cinematic Orchestral",
        "Lo-Fi Chill Beat",
        "Epic Trap Bass",
        "Ambient Piano & Pad",
        "Retro 80s Synth"
    )

    val moods = listOf("Epic & Cinematic", "Futuristic", "Emotional", "Energetic", "Dark Suspense")

    val voiceActors = listOf(
        "Deep Movie Trailer",
        "Warm Documentary",
        "Energetic Creator",
        "Calm Storyteller",
        "Futuristic AI Synth"
    )

    // Animated waveform bars
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val barPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar_pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("music_and_voice_screen")
    ) {
        // Top Bar
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
                    text = "AI Music & Voice Studio",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Cinematic Audio Synthesizer • Manas-MatchCut",
                    fontSize = 11.sp,
                    color = ElectricCyan
                )
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurfaceElevated,
            contentColor = ElectricCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = ElectricCyan
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI Soundtrack", fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI Voiceover", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            if (selectedTab == 0) {
                // AI Soundtrack Generator
                Text(
                    text = "Genre & Musical Style",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(genres) { genre ->
                        val isSelected = selectedGenre == genre
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedGenre = genre },
                            label = { Text(genre, fontSize = 11.sp) },
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

                Text(
                    text = "Cinematic Mood",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moods) { mood ->
                        val isSelected = selectedMood == mood
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMood = mood },
                            label = { Text(mood, fontSize = 11.sp) },
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

                Text(
                    text = "Tempo (BPM): ${tempoBpm.toInt()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Slider(
                    value = tempoBpm,
                    onValueChange = { tempoBpm = it },
                    valueRange = 80f..160f,
                    colors = SliderDefaults.colors(
                        thumbColor = ElectricCyan,
                        activeTrackColor = ElectricCyan,
                        inactiveTrackColor = DarkSurfaceBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Music Player Preview Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "$selectedGenre ($selectedMood)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${tempoBpm.toInt()} BPM • AI Generated Track",
                                    fontSize = 11.sp,
                                    color = AccentMint
                                )
                            }

                            IconButton(
                                onClick = { isMusicPlaying = !isMusicPlaying },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(ElectricCyan)
                            ) {
                                Icon(
                                    imageVector = if (isMusicPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = DarkObsidian,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Waveform Visualizer
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            val barCount = 28
                            val barWidth = size.width / (barCount * 1.5f)
                            for (i in 0 until barCount) {
                                val heightFactor = if (isMusicPlaying) {
                                    val base = ((i * 13) % 10) / 10f
                                    (base * barPulse).coerceIn(0.2f, 1f)
                                } else 0.2f
                                val barHeight = size.height * heightFactor
                                val x = i * (barWidth * 1.5f)
                                val y = (size.height - barHeight) / 2
                                drawRoundRect(
                                    color = if (i % 2 == 0) ElectricCyan else VividPurple,
                                    topLeft = Offset(x, y),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(4f, 4f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Track synthesized & added to video pipeline!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(ElectricGradient, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Apply to AI Video", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkObsidian)
                }
            } else {
                // AI Voiceover Studio
                Text(
                    text = "Narration Script",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = voiceScript,
                    onValueChange = { voiceScript = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricCyan,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = DarkSurfaceElevated,
                        unfocusedContainerColor = DarkSurfaceCard
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Voice Tone & Persona",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(voiceActors) { actor ->
                        val isSelected = selectedVoice == actor
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedVoice = actor },
                            label = { Text(actor, fontSize = 11.sp) },
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

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Speed: ${String.format("%.1fx", voiceSpeed)}", fontSize = 12.sp, color = TextPrimary)
                        Slider(
                            value = voiceSpeed,
                            onValueChange = { voiceSpeed = it },
                            valueRange = 0.75f..1.5f,
                            colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Pitch: ${String.format("%.1fx", voicePitch)}", fontSize = 12.sp, color = TextPrimary)
                        Slider(
                            value = voicePitch,
                            onValueChange = { voicePitch = it },
                            valueRange = 0.8f..1.2f,
                            colors = SliderDefaults.colors(thumbColor = VividPurple, activeTrackColor = VividPurple)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = selectedVoice,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Neural Text-to-Speech Preview",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        IconButton(
                            onClick = { isVoicePlaying = !isVoicePlaying },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(VividPurple)
                        ) {
                            Icon(
                                imageVector = if (isVoicePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Voiceover generated & synced!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(ElectricGradient, RoundedCornerShape(14.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Synthesize & Attach Voiceover", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkObsidian)
                }
            }
        }
    }
}
