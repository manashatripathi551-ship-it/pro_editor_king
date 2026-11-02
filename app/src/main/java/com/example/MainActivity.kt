package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.model.VideoProject
import com.example.ui.MainViewModel
import com.example.ui.ScreenDestination
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DownloadVideosScreen
import com.example.ui.screens.GeneratorScreen
import com.example.ui.screens.MusicAndVoiceScreen
import com.example.ui.screens.MyProjectsScreen
import com.example.ui.screens.SettingsAndPlanScreen
import com.example.ui.screens.VideoEditingScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.DarkObsidian
import com.example.ui.theme.ManasMatchCutTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManasMatchCutTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkObsidian
                ) { innerPadding ->
                    ManasMatchCutApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ManasMatchCutApp(viewModel: MainViewModel) {
    var screenStack by remember { mutableStateOf(listOf<ScreenDestination>(ScreenDestination.Welcome)) }
    val currentScreen = screenStack.lastOrNull() ?: ScreenDestination.Welcome

    val projects by viewModel.allProjects.collectAsState()
    val remainingCredits by viewModel.remainingCredits.collectAsState()
    val creatorPhotoUri by viewModel.creatorPhotoUri.collectAsState()
    val userApiKey by viewModel.userApiKey.collectAsState()
    val userPlan by viewModel.userPlan.collectAsState()

    fun navigateTo(dest: ScreenDestination) {
        screenStack = screenStack + dest
    }

    fun popBack() {
        if (screenStack.size > 1) {
            screenStack = screenStack.dropLast(1)
        }
    }

    // Handle back button on secondary screens
    if (screenStack.size > 1) {
        BackHandler {
            popBack()
        }
    }

    Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            is ScreenDestination.Welcome -> {
                WelcomeScreen(
                    creatorPhotoUri = creatorPhotoUri,
                    onCreatorPhotoSelected = { uri ->
                        viewModel.setCreatorPhoto(uri)
                    },
                    onStartCreatingClick = {
                        navigateTo(ScreenDestination.Dashboard)
                    }
                )
            }

            is ScreenDestination.Dashboard -> {
                DashboardScreen(
                    remainingCredits = remainingCredits,
                    creatorPhotoUri = creatorPhotoUri,
                    projects = projects,
                    onModuleClick = { moduleId ->
                        when (moduleId) {
                            "TEXT_TO_VIDEO", "IMAGE_TO_VIDEO", "AI_CARTOON", "SHORTS_MAKER" -> {
                                navigateTo(ScreenDestination.Generator(moduleId))
                            }
                            "AI_MUSIC_VOICE" -> navigateTo(ScreenDestination.MusicAndVoice)
                            "VIDEO_EDITING" -> {
                                val targetProject = projects.firstOrNull() ?: VideoProject(
                                    title = "MatchCut Cinema Reel",
                                    prompt = "Futuristic neon city sequence with match cuts",
                                    style = "Cyberpunk Sci-Fi",
                                    aspectRatio = "9:16",
                                    durationSeconds = 10
                                )
                                navigateTo(ScreenDestination.VideoEditing(targetProject))
                            }
                            "MY_PROJECTS" -> navigateTo(ScreenDestination.MyProjects)
                            "DOWNLOAD_VIDEOS" -> navigateTo(ScreenDestination.DownloadVideos)
                        }
                    },
                    onProjectClick = { proj ->
                        navigateTo(ScreenDestination.VideoEditing(proj))
                    },
                    onSettingsClick = {
                        navigateTo(ScreenDestination.SettingsAndPlan)
                    },
                    onCreditClick = {
                        navigateTo(ScreenDestination.SettingsAndPlan)
                    }
                )
            }

            is ScreenDestination.Generator -> {
                GeneratorScreen(
                    initialMode = screen.mode,
                    aiVideoEngine = viewModel.aiVideoEngine,
                    remainingCredits = remainingCredits,
                    onUseCredit = { viewModel.useCredit() },
                    onVideoSaved = { proj ->
                        viewModel.saveProject(proj)
                    },
                    onBackClick = { popBack() }
                )
            }

            is ScreenDestination.MusicAndVoice -> {
                MusicAndVoiceScreen(
                    onBackClick = { popBack() }
                )
            }

            is ScreenDestination.VideoEditing -> {
                VideoEditingScreen(
                    project = screen.project,
                    onBackClick = { popBack() }
                )
            }

            is ScreenDestination.MyProjects -> {
                MyProjectsScreen(
                    projects = projects,
                    onProjectSelect = { proj ->
                        navigateTo(ScreenDestination.VideoEditing(proj))
                    },
                    onEditProject = { proj ->
                        navigateTo(ScreenDestination.VideoEditing(proj))
                    },
                    onDeleteProject = { proj ->
                        viewModel.deleteProject(proj)
                    },
                    onBackClick = { popBack() }
                )
            }

            is ScreenDestination.DownloadVideos -> {
                DownloadVideosScreen(
                    projects = projects,
                    onBackClick = { popBack() }
                )
            }

            is ScreenDestination.SettingsAndPlan -> {
                SettingsAndPlanScreen(
                    remainingCredits = remainingCredits,
                    creatorPhotoUri = creatorPhotoUri,
                    userApiKey = userApiKey,
                    userPlan = userPlan,
                    onSaveApiKey = { key -> viewModel.setApiKey(key) },
                    onSaveCreatorPhoto = { uri -> viewModel.setCreatorPhoto(uri) },
                    onUpgradePlan = { plan -> viewModel.setPlan(plan) },
                    onBackClick = { popBack() }
                )
            }
        }
    }
}
