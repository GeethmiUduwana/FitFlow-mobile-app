package com.example.data

import kotlinx.coroutines.flow.Flow

class FitFlowRepository(private val dao: FitFlowDao) {
    // Workouts
    val workoutLogs: Flow<List<WorkoutLog>> = dao.getAllWorkoutLogs()

    suspend fun logWorkout(
        title: String,
        category: String,
        durationMinutes: Int,
        caloriesBurned: Int,
        intensity: String,
        personaId: String,
        notes: String = "",
        isAiAdapted: Boolean = true
    ): Long {
        return dao.insertWorkoutLog(
            WorkoutLog(
                title = title,
                category = category,
                durationMinutes = durationMinutes,
                caloriesBurned = caloriesBurned,
                intensity = intensity,
                personaId = personaId,
                notes = notes,
                isAiAdapted = isAiAdapted
            )
        )
    }

    suspend fun deleteWorkout(id: Long) = dao.deleteWorkoutLog(id)

    // Nutrition
    val nutritionLogs: Flow<List<NutritionLog>> = dao.getAllNutritionLogs()

    suspend fun logMeal(
        mealType: String,
        foodName: String,
        calories: Int,
        proteinG: Int,
        carbsG: Int,
        fatG: Int,
        isCameraScanned: Boolean = false,
        portionDescription: String = "1 standard serving"
    ): Long {
        return dao.insertNutritionLog(
            NutritionLog(
                mealType = mealType,
                foodName = foodName,
                calories = calories,
                proteinG = proteinG,
                carbsG = carbsG,
                fatG = fatG,
                isCameraScanned = isCameraScanned,
                portionDescription = portionDescription
            )
        )
    }

    suspend fun deleteMeal(id: Long) = dao.deleteNutritionLog(id)

    // Social Circles
    val circlePosts: Flow<List<CirclePost>> = dao.getAllCirclePosts()

    suspend fun createPost(
        authorName: String,
        authorRole: String,
        avatarColorHex: Long,
        content: String,
        workoutTag: String? = null,
        privacyLevel: String = "Close Circle"
    ): Long {
        return dao.insertCirclePost(
            CirclePost(
                authorName = authorName,
                authorRole = authorRole,
                avatarColorHex = avatarColorHex,
                content = content,
                workoutTag = workoutTag,
                privacyLevel = privacyLevel
            )
        )
    }

    suspend fun toggleProps(post: CirclePost) {
        val updated = post.copy(
            isUserPropped = !post.isUserPropped,
            propsCount = if (post.isUserPropped) (post.propsCount - 1).coerceAtLeast(0) else post.propsCount + 1
        )
        dao.updateCirclePost(updated)
    }

    // Challenges
    val challenges: Flow<List<Challenge>> = dao.getAllChallenges()

    suspend fun toggleJoinChallenge(challenge: Challenge) {
        dao.updateChallenge(challenge.copy(joined = !challenge.joined))
    }

    suspend fun incrementChallengeProgress(challenge: Challenge, amount: Int = 1) {
        val newProgress = (challenge.currentProgress + amount).coerceAtMost(challenge.targetGoal)
        dao.updateChallenge(challenge.copy(currentProgress = newProgress))
    }

    // Bug Reports for Lab 06 Activity 6
    val bugReports: Flow<List<BugReport>> = dao.getAllBugReports()

    suspend fun addBugReport(
        title: String,
        severity: String,
        component: String,
        description: String
    ): Long {
        return dao.insertBugReport(
            BugReport(
                title = title,
                severity = severity,
                component = component,
                description = description
            )
        )
    }

    suspend fun updateBugStatus(report: BugReport, newStatus: String) {
        dao.updateBugReport(report.copy(status = newStatus))
    }
}
