package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AiModelInfo
import com.example.model.VideoProject
import com.example.service.AiVideoEngine
import com.example.service.GenerationState
import com.example.ui.components.CinematicVideoPlayer
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentMint
import com.example.ui.theme.AccentPink
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGradient
import com.example.ui.theme.FilmStripBorderBrush
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple
import kotlinx.coroutines.launch

@Composable
fun GeneratorScreen(
    initialMode: String,
    aiVideoEngine: AiVideoEngine,
    remainingCredits: Int,
    onUseCredit: () -> Boolean,
    onVideoSaved: (VideoProject) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val generationState by aiVideoEngine.generationState.collectAsState()

    var promptText by remember {
        mutableStateOf(
            when (initialMode) {
                "IMAGE_TO_VIDEO" -> "Animate this photo with fluid organic motion, dynamic camera glide, and cinematic lighting"
                "AI_CARTOON" -> "Studio Ghibli anime style girl gazing at glowing starlight floating city, whimsical breeze"
                "SHORTS_MAKER" -> "Top 5 futuristic AI gadgets you won't believe exist in 2026, dynamic fast-cut viral reel"
                else -> "Ultra-realistic 8k cinematic shot of a neon cyberpunk city at night with flying vehicles and glowing holographic signs"
            }
        )
    }

    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }
    var selectedStyle by remember {
        mutableStateOf(
            when (initialMode) {
                "AI_CARTOON" -> "Anime / Studio Ghibli"
                "SHORTS_MAKER" -> "Cyberpunk Sci-Fi"
                else -> "Cinematic 8K"
            }
        )
    }
    var selectedAspectRatio by remember {
        mutableStateOf(
            if (initialMode == "SHORTS_MAKER") "9:16" else "9:16"
        )
    }
    var selectedDuration by remember { mutableIntStateOf(5) }
    var selectedResolution by remember { mutableStateOf("1080p") }
    var selectedModel by remember { mutableStateOf(AiVideoEngine.AVAILABLE_MODELS.first()) }
    var selectedCameraMotion by remember { mutableStateOf("Cinematic Glide Pan") }
    var isEnhancingPrompt by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
        }
    }

    val styles = listOf(
        "Cinematic 8K",
        "Anime / Studio Ghibli",
        "AI Cartoon",
        "3D Pixar Style",
        "Cyberpunk Sci-Fi",
        "Hyper-Realistic",
        "Vintage VHS Retro"
    )

    val aspectRatios = listOf("9:16", "16:9", "1:1")
    val durations = listOf(5, 10, 15)
    val resolutions = listOf("720p", "1080p", "4K")
    val cameraMotions = listOf(
        "Cinematic Glide Pan",
        "Zoom In Dramatic",
        "Drone Aerial Fly-through",
        "Orbit 360",
        "Static Cinema"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("generator_screen")
    ) {
        // Top App Bar
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
                    .testTag("generator_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (initialMode) {
                        "IMAGE_TO_VIDEO" -> "Image to Video Studio"
                        "AI_CARTOON" -> "AI Cartoon & Anime Studio"
                        "SHORTS_MAKER" -> "YouTube Shorts Maker (9:16)"
                        else -> "Text to Video Studio"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Google AI & DeepMind Video Pipeline",
                    fontSize = 11.sp,
                    color = ElectricCyan
                )
            }

            Surface(
                color = DarkSurfaceElevated,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "$remainingCredits Free Left",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (remainingCredits > 0) AccentMint else AccentPink,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Active State Display: Progress or Result or Input Form
        when (val state = generationState) {
            is GenerationState.Progress -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, FilmStripBorderBrush, RoundedCornerShape(24.dp))
                            .shadow(20.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                progress = { state.percent / 100f },
                                modifier = Modifier.size(72.dp),
                                color = ElectricCyan,
                                trackColor = DarkSurfaceBorder,
                                strokeWidth = 6.dp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "${state.percent}%",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ElectricCyan
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = state.stageName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = state.detail,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            LinearProgressIndicator(
                                progress = { state.percent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = VividPurple,
                                trackColor = DarkSurfaceBorder
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Model: ${selectedModel.name} • Manas-MatchCut",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            is GenerationState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = AccentMint.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AccentMint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Video Successfully Generated!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentMint
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    CinematicVideoPlayer(
                        project = state.project,
                        onDownloadClick = {
                            Toast.makeText(
                                context,
                                "Video saved to Movies/ManasMatchCut (${state.project.resolution})",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (state.project.aspectRatio == "9:16") 380.dp else 220.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = state.project.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.project.prompt,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Model: ${state.project.modelName}",
                                    fontSize = 11.sp,
                                    color = ElectricCyan
                                )
                                Text(
                                    text = "${state.project.resolution} • ${state.project.durationSeconds}s",
                                    fontSize = 11.sp,
                                    color = AccentMint
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                aiVideoEngine.resetState()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Create Another", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                onVideoSaved(state.project)
                                Toast.makeText(context, "Saved to My Projects!", Toast.LENGTH_SHORT).show()
                                onBackClick()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .background(ElectricGradient, RoundedCornerShape(14.dp)),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = DarkObsidian,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save & Exit", color = DarkObsidian, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            else -> {
                // Video Configuration Form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    // Image to Video Photo Upload Card (Shown if mode is IMAGE_TO_VIDEO or optional for others)
                    if (initialMode == "IMAGE_TO_VIDEO" || selectedPhotoUri != null) {
                        Text(
                            text = "Reference Photo to Animate",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .border(1.5.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("photo_upload_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated)
                        ) {
                            if (selectedPhotoUri != null) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    AsyncImage(
                                        model = selectedPhotoUri,
                                        contentDescription = "Uploaded Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    IconButton(
                                        onClick = { selectedPhotoUri = null },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .size(28.dp)
                                            .background(DarkObsidian.copy(alpha = 0.8f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove Photo",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Upload",
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tap to choose a photo to animate",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Portraits, landscapes, artwork, product stills",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Text Prompt Input Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Video Prompt",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        // AI Enhance Button
                        Surface(
                            color = ElectricCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clickable(enabled = !isEnhancingPrompt) {
                                    scope.launch {
                                        isEnhancingPrompt = true
                                        promptText = aiVideoEngine.enhancePromptWithAi(
                                            rawPrompt = promptText,
                                            style = selectedStyle
                                        )
                                        isEnhancingPrompt = false
                                        Toast.makeText(context, "Prompt enhanced with cinema direction!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .testTag("enhance_prompt_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isEnhancingPrompt) "Enhancing..." else "AI Enhance",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("prompt_text_field"),
                        minLines = 3,
                        maxLines = 5,
                        placeholder = {
                            Text(
                                text = "Describe what happens in your video... (e.g. A neon robot walks in rain, cinematic lighting, 60fps)",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        },
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // AI Model Selector
                    Text(
                        text = "AI Video Generation Model",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Movie,
                                        contentDescription = null,
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = selectedModel.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                                Surface(
                                    color = if (selectedModel.isFreeTierSupported) AccentMint.copy(alpha = 0.2f) else AccentGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (selectedModel.isFreeTierSupported) "Free Tier Ready" else "Pro API Required",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedModel.isFreeTierSupported) AccentMint else AccentGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = selectedModel.description,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Availability: ${selectedModel.costDescription}",
                                fontSize = 10.sp,
                                color = ElectricCyan
                            )
                        }
                    }

                    // Model switch chips
                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(AiVideoEngine.AVAILABLE_MODELS) { model ->
                            val isSelected = selectedModel.id == model.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedModel = model },
                                label = { Text(model.name, fontSize = 11.sp) },
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

                    // Visual Style Selector
                    Text(
                        text = "Visual Style Preset",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(styles) { style ->
                            val isSelected = selectedStyle == style
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedStyle = style },
                                label = { Text(style, fontSize = 11.sp) },
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

                    // Aspect Ratio, Duration, and Resolution in 3 columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Aspect Ratio
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Aspect Ratio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (ratio in aspectRatios) {
                                    val isSelected = selectedAspectRatio == ratio
                                    Surface(
                                        color = if (isSelected) ElectricCyan else DarkSurfaceCard,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedAspectRatio = ratio }
                                    ) {
                                        Text(
                                            text = ratio,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) DarkObsidian else TextSecondary,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Duration
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Duration", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (dur in durations) {
                                    val isSelected = selectedDuration == dur
                                    Surface(
                                        color = if (isSelected) VividPurple else DarkSurfaceCard,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedDuration = dur }
                                    ) {
                                        Text(
                                            text = "${dur}s",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Resolution
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Resolution", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (res in resolutions) {
                                    val isSelected = selectedResolution == res
                                    Surface(
                                        color = if (isSelected) AccentMint else DarkSurfaceCard,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedResolution = res }
                                    ) {
                                        Text(
                                            text = res,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) DarkObsidian else TextSecondary,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Camera Movement Selector
                    Text("Camera Motion", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cameraMotions) { motion ->
                            val isSelected = selectedCameraMotion == motion
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCameraMotion = motion },
                                label = { Text(motion, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ElectricBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = DarkSurfaceCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Big Generate Video Button
                    Button(
                        onClick = {
                            if (promptText.isBlank()) {
                                Toast.makeText(context, "Please enter a video prompt", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (remainingCredits <= 0 && selectedModel.isFreeTierSupported) {
                                Toast.makeText(context, "Daily Free limit reached! Resetting tomorrow or add custom API key in Settings.", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            val creditUsed = onUseCredit()
                            scope.launch {
                                val completedProject = aiVideoEngine.generateVideo(
                                    title = promptText.take(28),
                                    prompt = promptText,
                                    style = selectedStyle,
                                    aspectRatio = selectedAspectRatio,
                                    durationSeconds = selectedDuration,
                                    resolution = selectedResolution,
                                    modelName = selectedModel.name,
                                    mode = initialMode,
                                    cameraMovement = selectedCameraMotion,
                                    musicTrack = "Cyberpunk Synthwave",
                                    photoUri = selectedPhotoUri
                                )
                                onVideoSaved(completedProject)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ElectricGradient)
                            .testTag("generate_video_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = DarkObsidian,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate AI Video",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkObsidian
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Transparent Model & Free Tier Disclosure Note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Generated on Google AI & DeepMind pipeline. Consumes 1 credit.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
