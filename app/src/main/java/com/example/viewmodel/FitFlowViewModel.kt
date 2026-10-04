package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Exercise(
    val name: String,
    val durationSeconds: Int,
    val targetMuscle: String,
    val repsOrHold: String,
    val tips: String
)

data class GeneratedWorkout(
    val title: String,
    val estimatedMinutes: Int,
    val calories: Int,
    val difficulty: String,
    val exercises: List<Exercise>,
    val adaptationReason: String
)

class FitFlowViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FitFlowRepository

    init {
        val db = FitFlowDatabase.getDatabase(application, viewModelScope)
        repository = FitFlowRepository(db.fitFlowDao())
    }

    // Personas
    private val _activePersona = MutableStateFlow<Persona>(AlexRivera)
    val activePersona: StateFlow<Persona> = _activePersona.asStateFlow()

    fun switchPersona(persona: Persona) {
        _activePersona.value = persona
        generateDailyFlow() // Refresh AI plan based on selected persona
    }

    // Database streams
    val workoutLogs: StateFlow<List<WorkoutLog>> = repository.workoutLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val nutritionLogs: StateFlow<List<NutritionLog>> = repository.nutritionLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val circlePosts: StateFlow<List<CirclePost>> = repository.circlePosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val challenges: StateFlow<List<Challenge>> = repository.challenges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bugReports: StateFlow<List<BugReport>> = repository.bugReports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Water intake state in ml (defaults to 1750 ml)
    private val _waterIntakeMl = MutableStateFlow(1750)
    val waterIntakeMl: StateFlow<Int> = _waterIntakeMl.asStateFlow()

    fun addWater(amountMl: Int = 250) {
        _waterIntakeMl.value = (_waterIntakeMl.value + amountMl).coerceAtMost(4000)
        viewModelScope.launch {
            repository.logMeal(
                mealType = "Hydration",
                foodName = "Pure Water + Electrolytes",
                calories = 0,
                proteinG = 0,
                carbsG = 0,
                fatG = 0,
                isCameraScanned = false,
                portionDescription = "$amountMl ml"
            )
        }
    }

    // Daily Flow AI Configuration
    val selectedDurationMins = MutableStateFlow(18)
    val selectedIntensity = MutableStateFlow("Adaptive Moderate")
    val selectedEquipment = MutableStateFlow("Dumbbells & Mat")
    val currentEnergyLevel = MutableStateFlow("Normal") // "Fatigued / Low", "Normal", "High Energy"

    private val _aiGeneratedWorkout = MutableStateFlow<GeneratedWorkout?>(null)
    val aiGeneratedWorkout: StateFlow<GeneratedWorkout?> = _aiGeneratedWorkout.asStateFlow()

    private val _isGeneratingAi = MutableStateFlow(false)
    val isGeneratingAi: StateFlow<Boolean> = _isGeneratingAi.asStateFlow()

    init {
        generateDailyFlow()
    }

    fun generateDailyFlow() {
        viewModelScope.launch {
            _isGeneratingAi.value = true
            delay(400) // Smooth AI computation feeling

            val persona = _activePersona.value
            val duration = selectedDurationMins.value
            val energy = currentEnergyLevel.value

            val exercises = when {
                persona.id == "alex" && energy == "Fatigued / Low" -> listOf(
                    Exercise("Decompression Foam Rolling & Cat-Cow", 90, "Mobility", "10 slow cycles", "Release lower back tension from desk work"),
                    Exercise("World's Greatest Stretch", 90, "Hips & Thoracic", "5 per side", "Open chest and hip flexors"),
                    Exercise("Kettlebell Deadlift (Light)", 120, "Posterior Chain", "3 sets of 10", "Keep core engaged, neutral spine"),
                    Exercise("Gentle Breathing Reset", 60, "Parasympathetic", "Deep diaphragmatic", "Reduce cortisol after high-stress calls")
                )
                persona.id == "alex" -> listOf(
                    Exercise("Dumbbell Thrusters (Compound)", 120, "Full Body", "3 sets of 12 reps", "Explode out of the squat with overhead press"),
                    Exercise("Renegade Rows & Push-ups", 120, "Chest, Back & Core", "3 sets of 8 reps", "Keep hips steady, zero rotation"),
                    Exercise("Kettlebell Romanian Deadlift", 120, "Hamstrings & Glutes", "3 sets of 10 reps", "Hinge deeply at hips"),
                    Exercise("Mountain Climber Finisher", 90, "Cardio Core", "3 x 30s bursts", "Drive knees straight toward elbows")
                )
                energy == "Fatigued / Low" -> listOf(
                    Exercise("Seated Spinal Twist", 90, "Spine Mobility", "Hold 30s each side", "Breathe smoothly, do not force"),
                    Exercise("Glute Bridges", 120, "Glutes & Core", "2 sets of 12", "Squeeze glutes at the top"),
                    Exercise("Wall Push-ups", 90, "Chest & Shoulders", "2 sets of 10", "Safe gentle upper body work"),
                    Exercise("Child's Pose Rest", 60, "Lower Back", "Hold with calm breaths", "Restful grounding pose")
                )
                else -> listOf(
                    Exercise("Bodyweight Goblet Squats", 120, "Quads & Glutes", "3 sets of 10 reps", "Chest up, weight balanced on feet"),
                    Exercise("Incline Bench Push-ups", 90, "Chest & Arms", "3 sets of 8 reps", "Maintain straight plank alignment"),
                    Exercise("Deadbugs Core Stability", 90, "Abdominals", "2 sets of 12 reps", "Press lower back flat to mat"),
                    Exercise("Bird-Dog Balance", 90, "Posterior Chain", "2 sets of 10 per side", "Reach arm and opposite leg slowly")
                )
            }

            val estimatedCalories = (duration * 9.5).toInt()
            val reason = if (persona.id == "alex") {
                "Tailored for Alex's busy schedule: High metabolic efficiency, compound dumbbells, 0 wasted downtime."
            } else {
                "Personalized for Priya's beginner journey: Safe progressive loading, joint-friendly form, positive reinforcement."
            }

            _aiGeneratedWorkout.value = GeneratedWorkout(
                title = if (persona.id == "alex") "AI Adaptive Express Flow ($duration min)" else "Beginner Flow & Core ($duration min)",
                estimatedMinutes = duration,
                calories = estimatedCalories,
                difficulty = if (persona.id == "alex") "High Efficiency" else "Gentle Progressive",
                exercises = exercises,
                adaptationReason = reason
            )
            _isGeneratingAi.value = false
        }
    }

    fun adaptWorkoutInRealTime(scaleDown: Boolean) {
        val current = _aiGeneratedWorkout.value ?: return
        val newDuration = if (scaleDown) (current.estimatedMinutes - 6).coerceAtLeast(10) else current.estimatedMinutes + 5
        selectedDurationMins.value = newDuration
        generateDailyFlow()
    }

    // Workout Logger
    fun logCompletedWorkout(title: String, category: String, durationMins: Int, calories: Int, intensity: String, notes: String) {
        viewModelScope.launch {
            repository.logWorkout(
                title = title,
                category = category,
                durationMinutes = durationMins,
                caloriesBurned = calories,
                intensity = intensity,
                personaId = _activePersona.value.id,
                notes = notes,
                isAiAdapted = true
            )
            // Also post achievement to Circle if user chooses
            val persona = _activePersona.value
            repository.createPost(
                authorName = persona.name,
                authorRole = persona.occupation,
                avatarColorHex = persona.avatarColorHex,
                content = "Crushed today's $durationMins-min AI Daily Flow! Burned $calories kcal. $notes",
                workoutTag = "$durationMins min $category",
                privacyLevel = "Close Circle"
            )
        }
    }

    fun deleteWorkout(id: Long) {
        viewModelScope.launch { repository.deleteWorkout(id) }
    }

    // Nutrition Logging
    fun logMeal(
        mealType: String,
        foodName: String,
        calories: Int,
        proteinG: Int,
        carbsG: Int,
        fatG: Int,
        isCameraScanned: Boolean = false,
        portionDescription: String = "1 serving"
    ) {
        viewModelScope.launch {
            repository.logMeal(
                mealType = mealType,
                foodName = foodName,
                calories = calories,
                proteinG = proteinG,
                carbsG = carbsG,
                fatG = fatG,
                isCameraScanned = isCameraScanned,
                portionDescription = portionDescription
            )
        }
    }

    fun deleteMeal(id: Long) {
        viewModelScope.launch { repository.deleteMeal(id) }
    }

    // Social Circles & Props
    fun toggleProps(post: CirclePost) {
        viewModelScope.launch { repository.toggleProps(post) }
    }

    fun createCirclePost(content: String, privacyLevel: String, workoutTag: String? = null) {
        val persona = _activePersona.value
        viewModelScope.launch {
            repository.createPost(
                authorName = persona.name,
                authorRole = persona.occupation,
                avatarColorHex = persona.avatarColorHex,
                content = content,
                workoutTag = workoutTag,
                privacyLevel = privacyLevel
            )
        }
    }

    fun toggleJoinChallenge(challenge: Challenge) {
        viewModelScope.launch { repository.toggleJoinChallenge(challenge) }
    }

    // Lab 06 Activity 6 Bug Tracking
    fun submitBugReport(title: String, severity: String, component: String, description: String) {
        viewModelScope.launch {
            repository.addBugReport(title, severity, component, description)
        }
    }

    fun updateBugStatus(report: BugReport, newStatus: String) {
        viewModelScope.launch {
            repository.updateBugStatus(report, newStatus)
        }
    }
}
