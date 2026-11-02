package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.VideoProject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferences(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("manas_matchcut_prefs", Context.MODE_PRIVATE)

    companion object {
        const val MAX_DAILY_FREE_CREDITS = 5
        private const val KEY_CREDITS_USED = "credits_used"
        private const val KEY_LAST_DATE = "last_date_key"
        private const val KEY_CREATOR_PHOTO_URI = "creator_photo_uri"
        private const val KEY_USER_API_KEY = "user_api_key"
        private const val KEY_USER_PLAN = "user_plan"
        private const val KEY_SEEDED = "has_seeded_sample_projects"
    }

    private val _remainingCredits = MutableStateFlow(MAX_DAILY_FREE_CREDITS)
    val remainingCredits: StateFlow<Int> = _remainingCredits.asStateFlow()

    private val _creatorPhotoUri = MutableStateFlow(prefs.getString(KEY_CREATOR_PHOTO_URI, null))
    val creatorPhotoUri: StateFlow<String?> = _creatorPhotoUri.asStateFlow()

    private val _userApiKey = MutableStateFlow(prefs.getString(KEY_USER_API_KEY, null))
    val userApiKey: StateFlow<String?> = _userApiKey.asStateFlow()

    private val _userPlan = MutableStateFlow(prefs.getString(KEY_USER_PLAN, "Free Tier") ?: "Free Tier")
    val userPlan: StateFlow<String> = _userPlan.asStateFlow()

    init {
        checkDailyReset()
        _remainingCredits.value = getRemainingCreditsInternal()
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun checkDailyReset() {
        val today = getTodayDateString()
        val lastDate = prefs.getString(KEY_LAST_DATE, "")
        if (lastDate != today) {
            prefs.edit()
                .putString(KEY_LAST_DATE, today)
                .putInt(KEY_CREDITS_USED, 0)
                .apply()
            _remainingCredits.value = MAX_DAILY_FREE_CREDITS
        }
    }

    private fun getRemainingCreditsInternal(): Int {
        val used = prefs.getInt(KEY_CREDITS_USED, 0)
        return (MAX_DAILY_FREE_CREDITS - used).coerceAtLeast(0)
    }

    fun useCredit(): Boolean {
        checkDailyReset()
        val currentUsed = prefs.getInt(KEY_CREDITS_USED, 0)
        if (currentUsed < MAX_DAILY_FREE_CREDITS || _userPlan.value != "Free Tier") {
            val newUsed = currentUsed + 1
            prefs.edit().putInt(KEY_CREDITS_USED, newUsed).apply()
            _remainingCredits.value = (MAX_DAILY_FREE_CREDITS - newUsed).coerceAtLeast(0)
            return true
        }
        return false
    }

    fun addBonusCredits(amount: Int) {
        val currentUsed = prefs.getInt(KEY_CREDITS_USED, 0)
        val newUsed = (currentUsed - amount).coerceAtLeast(0)
        prefs.edit().putInt(KEY_CREDITS_USED, newUsed).apply()
        _remainingCredits.value = (MAX_DAILY_FREE_CREDITS - newUsed).coerceAtLeast(0)
    }

    fun setCreatorPhotoUri(uriString: String?) {
        prefs.edit().putString(KEY_CREATOR_PHOTO_URI, uriString).apply()
        _creatorPhotoUri.value = uriString
    }

    fun setUserApiKey(apiKey: String?) {
        prefs.edit().putString(KEY_USER_API_KEY, apiKey).apply()
        _userApiKey.value = apiKey
    }

    fun setUserPlan(plan: String) {
        prefs.edit().putString(KEY_USER_PLAN, plan).apply()
        _userPlan.value = plan
    }

    fun hasSeeded(): Boolean = prefs.getBoolean(KEY_SEEDED, false)

    fun markSeeded() {
        prefs.edit().putBoolean(KEY_SEEDED, true).apply()
    }

    suspend fun seedInitialProjectsIfNeeded(dao: ProjectDao) {
        if (!hasSeeded()) {
            val sampleProjects = listOf(
                VideoProject(
                    title = "Neon Cyberpunk Tokyo Rain",
                    prompt = "Ultra-realistic 8k cinematic shot of a futuristic Tokyo alleyway bathed in glowing electric cyan and magenta neon signs, rain reflection puddles, 60fps slow pan",
                    style = "Cyberpunk Sci-Fi",
                    aspectRatio = "9:16",
                    durationSeconds = 10,
                    resolution = "1080p",
                    modelName = "Google Veo 2",
                    mode = "TEXT_TO_VIDEO",
                    status = "COMPLETED",
                    musicTrack = "Cyberpunk Synthwave",
                    cameraMovement = "Cinematic Glide Pan",
                    createdAt = System.currentTimeMillis() - 3600000L * 2
                ),
                VideoProject(
                    title = "Portrait to Life: Creator Motion",
                    prompt = "Bring portrait photo to life with gentle breathing motion, studio rim lighting, cinematic 35mm depth of field, warm golden ambient aura",
                    style = "Hyper-Realistic 8K",
                    aspectRatio = "1:1",
                    durationSeconds = 5,
                    resolution = "1080p",
                    modelName = "Google Imagen 3 Video",
                    mode = "IMAGE_TO_VIDEO",
                    status = "COMPLETED",
                    musicTrack = "Ambient Piano",
                    cameraMovement = "Zoom In Dramatic",
                    createdAt = System.currentTimeMillis() - 3600000L * 6
                ),
                VideoProject(
                    title = "Anime Sky City Flight",
                    prompt = "Studio Ghibli style floating islands city with airships gliding through sunset clouds, painterly textures and vibrant anime aesthetics",
                    style = "Anime / Studio Ghibli",
                    aspectRatio = "16:9",
                    durationSeconds = 15,
                    resolution = "4K",
                    modelName = "Gemini Multimodal Director",
                    mode = "AI_CARTOON",
                    status = "COMPLETED",
                    musicTrack = "Cinematic Orchestral",
                    cameraMovement = "Drone Aerial Fly-through",
                    createdAt = System.currentTimeMillis() - 3600000L * 12
                )
            )
            sampleProjects.forEach { dao.insertProject(it) }
            markSeeded()
        }
    }
}
