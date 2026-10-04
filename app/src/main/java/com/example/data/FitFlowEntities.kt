package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "HIIT", "Strength", "Mobility", "Cardio"
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val intensity: String, // "Adaptive Light", "Moderate", "High Intensity"
    val completedTimestamp: Long = System.currentTimeMillis(),
    val personaId: String = "alex", // "alex", "priya", "custom"
    val notes: String = "",
    val isAiAdapted: Boolean = true
)

@Entity(tableName = "nutrition_logs")
data class NutritionLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mealType: String, // "Breakfast", "Lunch", "Dinner", "Snack", "Hydration"
    val foodName: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isCameraScanned: Boolean = false,
    val portionDescription: String = "1 standard serving"
)

@Entity(tableName = "circle_posts")
data class CirclePost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorName: String,
    val authorRole: String,
    val avatarColorHex: Long = 0xFF00B774,
    val content: String,
    val workoutTag: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val propsCount: Int = 0,
    val isUserPropped: Boolean = false,
    val privacyLevel: String = "Close Circle" // "Close Circle", "Public Community", "Private"
)

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val currentProgress: Int,
    val targetGoal: Int,
    val unit: String = "days",
    val joined: Boolean = false,
    val rewardBadge: String,
    val category: String = "Consistency"
)

@Entity(tableName = "bug_reports")
data class BugReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val severity: String, // "Low", "Medium", "High", "Critical"
    val component: String, // "AI Engine", "Camera Logging", "Circles Feed", "Performance"
    val description: String,
    val status: String = "Open", // "Open", "Investigating", "Resolved"
    val timestamp: Long = System.currentTimeMillis()
)
