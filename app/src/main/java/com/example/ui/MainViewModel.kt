package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.UserPreferences
import com.example.model.VideoProject
import com.example.service.AiVideoEngine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Welcome : ScreenDestination()
    object Dashboard : ScreenDestination()
    data class Generator(val mode: String) : ScreenDestination()
    object MusicAndVoice : ScreenDestination()
    data class VideoEditing(val project: VideoProject) : ScreenDestination()
    object MyProjects : ScreenDestination()
    object DownloadVideos : ScreenDestination()
    object SettingsAndPlan : ScreenDestination()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val projectDao = database.projectDao()
    val userPreferences = UserPreferences(application)
    val aiVideoEngine = AiVideoEngine(application)

    val allProjects: StateFlow<List<VideoProject>> = projectDao.getAllProjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val remainingCredits: StateFlow<Int> = userPreferences.remainingCredits
    val creatorPhotoUri: StateFlow<String?> = userPreferences.creatorPhotoUri
    val userApiKey: StateFlow<String?> = userPreferences.userApiKey
    val userPlan: StateFlow<String> = userPreferences.userPlan

    init {
        viewModelScope.launch {
            userPreferences.seedInitialProjectsIfNeeded(projectDao)
        }
    }

    fun saveProject(project: VideoProject) {
        viewModelScope.launch {
            projectDao.insertProject(project)
        }
    }

    fun deleteProject(project: VideoProject) {
        viewModelScope.launch {
            projectDao.deleteProject(project)
        }
    }

    fun useCredit(): Boolean {
        return userPreferences.useCredit()
    }

    fun setCreatorPhoto(uri: String?) {
        userPreferences.setCreatorPhotoUri(uri)
    }

    fun setApiKey(key: String?) {
        userPreferences.setUserApiKey(key)
    }

    fun setPlan(plan: String) {
        userPreferences.setUserPlan(plan)
    }
}
