package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.BuildConfig
import com.example.service.AiVideoEngine
import com.example.ui.theme.AccentGold
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
fun SettingsAndPlanScreen(
    remainingCredits: Int,
    creatorPhotoUri: String?,
    userApiKey: String?,
    userPlan: String,
    onSaveApiKey: (String?) -> Unit,
    onSaveCreatorPhoto: (String?) -> Unit,
    onUpgradePlan: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var customApiKeyInput by remember { mutableStateOf(userApiKey ?: "") }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showDeployGuideDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onSaveCreatorPhoto(uri.toString())
            Toast.makeText(context, "Creator real photo updated!", Toast.LENGTH_SHORT).show()
        }
    }

    val hasSystemKey = try {
        BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"
    } catch (e: Throwable) {
        false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("settings_and_plan_screen")
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
                    text = "Account & AI Settings",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Plans, API Keys & Cloud Models",
                    fontSize = 11.sp,
                    color = ElectricCyan
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Creator Profile Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, ElectricGradient, CircleShape)
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (!creatorPhotoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = creatorPhotoUri,
                                    contentDescription = "Manas Tripathi",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Brush.radialGradient(listOf(ElectricCyan, VividPurple))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("MT", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkObsidian)
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan)
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Change photo",
                                tint = DarkObsidian,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Manas Tripathi",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = ElectricCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Creator & Lead Architect",
                            fontSize = 12.sp,
                            color = AccentMint
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tap photo to select real profile picture",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Free Tier & Usage Card
            Text(
                text = "Plan & Generation Credits",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = userPlan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Resets daily at 00:00 midnight",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Surface(
                            color = AccentMint.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "$remainingCredits / 5 Free Credits",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentMint,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Free Tier includes up to 5 video generations per day in 720p/1080p using Google AI preview quotas. High-throughput commercial rendering (4K 60fps & unlimited queue) requires a connected Google Cloud / AI Studio API key.",
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                onUpgradePlan("Pro Creator (Unlimited)")
                                Toast.makeText(context, "Upgraded to Pro Creator!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(ElectricGradient, RoundedCornerShape(12.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Upgrade to Pro", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkObsidian)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Google AI API Credentials Manager
            Text(
                text = "Google AI & DeepMind API Key",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "API Key Configuration",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Status pill
                    Surface(
                        color = if (hasSystemKey || customApiKeyInput.isNotBlank()) AccentMint.copy(alpha = 0.15f) else AccentPink.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (customApiKeyInput.isNotBlank()) "Using Custom API Key"
                            else if (hasSystemKey) "AI Studio Environment Key Connected"
                            else "No API Key (Using Free Local Simulation)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (hasSystemKey || customApiKeyInput.isNotBlank()) AccentMint else AccentPink,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Enter your Google AI Studio or Vertex AI Key to enable direct high-speed rendering with Veo 2 & Imagen 3.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customApiKeyInput,
                        onValueChange = { customApiKeyInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("AIzaSy...", color = TextSecondary, fontSize = 12.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceCard,
                            unfocusedContainerColor = DarkSurfaceCard
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                onSaveApiKey(if (customApiKeyInput.isBlank()) null else customApiKeyInput)
                                Toast.makeText(context, "API Key updated successfully!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Save Key", color = DarkObsidian, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // AI Models & Cost Transparency Disclosures
            Text(
                text = "Model Specifications & Cost Transparency",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AiVideoEngine.AVAILABLE_MODELS.forEach { model ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = model.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = model.provider,
                                    fontSize = 11.sp,
                                    color = ElectricCyan
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = model.description,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pricing: ${model.costDescription}",
                                fontSize = 10.sp,
                                color = AccentGold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Legal & Production Links
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTermsDialog = true }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Policy, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Terms of Service", fontSize = 13.sp, color = TextPrimary)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivacyDialog = true }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = VividPurple, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Privacy Policy", fontSize = 13.sp, color = TextPrimary)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDeployGuideDialog = true }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Cloud, contentDescription = null, tint = AccentMint, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Signed APK & Release Guide", fontSize = 13.sp, color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Dialogs
        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = { showTermsDialog = false },
                title = { Text("Terms of Service • Manas-MatchCut", color = TextPrimary, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "1. Manas-MatchCut provides AI video generation using Google AI and connected video models.\n\n2. Free tier grants 5 generations per day subject to provider capacity.\n\n3. Users retain full rights to their original uploaded media and generated outputs.\n\n4. Prohibited use includes generating unlawful, non-consensual or malicious content.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showTermsDialog = false }) {
                        Text("Accept", color = ElectricCyan)
                    }
                },
                containerColor = DarkSurfaceElevated
            )
        }

        if (showPrivacyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                title = { Text("Privacy Policy", color = TextPrimary, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "1. Your uploaded photos and prompts are processed solely to synthesize requested video animations.\n\n2. Real photos selected for creator recognition remain strictly on your device.\n\n3. API keys entered into the app are stored exclusively in private on-device SharedPreferences.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showPrivacyDialog = false }) {
                        Text("Close", color = ElectricCyan)
                    }
                },
                containerColor = DarkSurfaceElevated
            )
        }

        if (showDeployGuideDialog) {
            AlertDialog(
                onDismissRequest = { showDeployGuideDialog = false },
                title = { Text("Signed Release & APK Build Guide", color = TextPrimary, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "• Native Android Packaging: Application ID configured as com.aistudio.manasmatchcut.app\n• Target SDK: 36 (Android 15+)\n• Build Release APK: Run 'gradle :app:assembleRelease'\n• Keystore: Pre-configured signing configurations in build.gradle.kts\n• Output: app/build/outputs/apk/release/app-release.apk\n• Verification: Tested via compile_applet and Robolectric JVM test suite.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showDeployGuideDialog = false }) {
                        Text("Got It", color = ElectricCyan)
                    }
                },
                containerColor = DarkSurfaceElevated
            )
        }
    }
}
