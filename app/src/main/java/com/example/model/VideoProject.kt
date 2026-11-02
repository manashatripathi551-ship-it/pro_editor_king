package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_projects")
data class VideoProject(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val prompt: String,
    val style: String = "Cinematic 8K",
    val aspectRatio: String = "9:16",
    val durationSeconds: Int = 5,
    val resolution: String = "1080p",
    val modelName: String = "Google Veo 2",
    val mode: String = "TEXT_TO_VIDEO",
    val status: String = "COMPLETED", // COMPLETED, RENDERING, DRAFT
    val thumbnailUri: String? = null,
    val videoUri: String? = null,
    val musicTrack: String? = "Cyberpunk Synthwave",
    val voiceoverText: String? = null,
    val motionStrength: Float = 0.75f,
    val cameraMovement: String = "Zoom In Dramatic",
    val createdAt: Long = System.currentTimeMillis()
)

enum class VideoGenerationMode(val displayName: String, val description: String) {
    TEXT_TO_VIDEO("Text to Video", "Generate cinematic scenes from detailed descriptive prompts"),
    IMAGE_TO_VIDEO("Image to Video", "Bring still images and portraits to dynamic life"),
    AI_CARTOON("AI Cartoon Video", "Convert ideas and frames into stylized animations & anime"),
    SHORTS_MAKER("YouTube Shorts Maker", "Vertical viral 9:16 video generation with hook and captions"),
    AI_MUSIC_VOICE("AI Music & Voice", "Synthesize original soundtracks and cinematic voiceovers"),
    VIDEO_EDITING("Video Editing", "MatchCut transitions, speed ramping, and color grading")
}

data class AiModelInfo(
    val id: String,
    val name: String,
    val provider: String,
    val tag: String,
    val maxResolution: String,
    val maxDurationSec: Int,
    val isFreeTierSupported: Boolean,
    val costDescription: String,
    val description: String,
    val isOfficialGoogleModel: Boolean = false
)
