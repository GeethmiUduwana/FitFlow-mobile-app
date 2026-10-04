package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        WorkoutLog::class,
        NutritionLog::class,
        CirclePost::class,
        Challenge::class,
        BugReport::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitFlowDatabase : RoomDatabase() {
    abstract fun fitFlowDao(): FitFlowDao

    companion object {
        @Volatile
        private var INSTANCE: FitFlowDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FitFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitFlowDatabase::class.java,
                    "fitflow_db"
                )
                    .addCallback(FitFlowDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class FitFlowDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.fitFlowDao())
                }
            }
        }

        suspend fun populateInitialData(dao: FitFlowDao) {
            val now = System.currentTimeMillis()
            val hourMs = 3600_000L
            val dayMs = 86400_000L

            // Initial Workouts reflecting Alex Rivera (busy marketing lead) & Priya Singh (beginner teacher)
            dao.insertWorkoutLog(
                WorkoutLog(
                    title = "AI Daily Flow: Express Core & Cardio",
                    category = "HIIT",
                    durationMinutes = 18,
                    caloriesBurned = 210,
                    intensity = "Adaptive Moderate",
                    completedTimestamp = now - (2 * hourMs),
                    personaId = "alex",
                    notes = "Automatically scaled from 30m to 18m due to chaotic calendar. Completed all 4 supersets!",
                    isAiAdapted = true
                )
            )
            dao.insertWorkoutLog(
                WorkoutLog(
                    title = "Beginner Low-Impact Full Body",
                    category = "Strength",
                    durationMinutes = 25,
                    caloriesBurned = 180,
                    intensity = "Beginner Gentle",
                    completedTimestamp = now - dayMs,
                    personaId = "priya",
                    notes = "Felt super encouraging. Followed form cue animations closely.",
                    isAiAdapted = true
                )
            )
            dao.insertWorkoutLog(
                WorkoutLog(
                    title = "Dynamic Flow Mobility Reset",
                    category = "Mobility",
                    durationMinutes = 15,
                    caloriesBurned = 95,
                    intensity = "Recovery Flow",
                    completedTimestamp = now - (2 * dayMs),
                    personaId = "alex",
                    notes = "Shoulder & hip mobility after prolonged desk hours.",
                    isAiAdapted = false
                )
            )

            // Initial Nutrition Logs
            dao.insertNutritionLog(
                NutritionLog(
                    mealType = "Breakfast",
                    foodName = "Avocado Toast with Poached Egg",
                    calories = 380,
                    proteinG = 18,
                    carbsG = 34,
                    fatG = 20,
                    timestamp = now - (4 * hourMs),
                    isCameraScanned = true,
                    portionDescription = "2 slices multigrain with 1 egg"
                )
            )
            dao.insertNutritionLog(
                NutritionLog(
                    mealType = "Lunch",
                    foodName = "Grilled Salmon Quinoa Bowl",
                    calories = 540,
                    proteinG = 42,
                    carbsG = 45,
                    fatG = 18,
                    timestamp = now - (1 * hourMs),
                    isCameraScanned = true,
                    portionDescription = "Medium bowl with lemon vinaigrette"
                )
            )
            dao.insertNutritionLog(
                NutritionLog(
                    mealType = "Hydration",
                    foodName = "Electrolyte Pure Water",
                    calories = 0,
                    proteinG = 0,
                    carbsG = 0,
                    fatG = 0,
                    timestamp = now - (30 * 60_000L),
                    isCameraScanned = false,
                    portionDescription = "500 ml"
                )
            )

            // Initial Circle Posts
            dao.insertCirclePost(
                CirclePost(
                    authorName = "Alex Rivera",
                    authorRole = "Busy Marketing Pro",
                    avatarColorHex = 0xFF0EA5E9,
                    content = "Had 20 minutes between client pitches. AI Daily Flow trimmed the rest intervals and gave me a killer dumbbell circuit. Consistency over perfection! 🔥",
                    workoutTag = "18m AI Express HIIT",
                    timestamp = now - (2 * hourMs),
                    propsCount = 14,
                    isUserPropped = true,
                    privacyLevel = "Close Circle"
                )
            )
            dao.insertCirclePost(
                CirclePost(
                    authorName = "Priya Singh",
                    authorRole = "Beginner Teacher",
                    avatarColorHex = 0xFF00B774,
                    content = "Hit day 5 of the Beginner Flow! Huge thank you to this circle for keeping me accountable. Never thought I’d look forward to morning workouts. 🙌",
                    workoutTag = "25m Gentle Full Body",
                    timestamp = now - (5 * hourMs),
                    propsCount = 28,
                    isUserPropped = false,
                    privacyLevel = "Close Circle"
                )
            )
            dao.insertCirclePost(
                CirclePost(
                    authorName = "Marcus Chen",
                    authorRole = "Community Coach",
                    avatarColorHex = 0xFFF97316,
                    content = "Tip of the day: When feeling fatigued, don’t skip entirely! Tap 'Scale to Recovery' on the Daily Flow card to keep your neural groove without central fatigue.",
                    workoutTag = "Coaching Insight",
                    timestamp = now - (18 * hourMs),
                    propsCount = 42,
                    isUserPropped = false,
                    privacyLevel = "Public Community"
                )
            )

            // Initial Challenges
            dao.insertChallenges(
                listOf(
                    Challenge(
                        title = "7-Day Flow Consistency",
                        description = "Complete any workout or mobility session 7 days in a row.",
                        currentProgress = 5,
                        targetGoal = 7,
                        unit = "days",
                        joined = true,
                        rewardBadge = "⚡ Consistency Champion",
                        category = "Habit"
                    ),
                    Challenge(
                        title = "10k Daily Steps Surge",
                        description = "Hit 10,000 steps daily with live tracking and cheer friends.",
                        currentProgress = 7450,
                        targetGoal = 10000,
                        unit = "steps",
                        joined = true,
                        rewardBadge = "👟 Swift Mover",
                        category = "Activity"
                    ),
                    Challenge(
                        title = "Clean Fuel Camera Logger",
                        description = "Snap your lunch & dinner with the AI Nutrition Scanner for 5 days.",
                        currentProgress = 3,
                        targetGoal = 5,
                        unit = "days",
                        joined = false,
                        rewardBadge = "🥗 Mindful Eater",
                        category = "Nutrition"
                    ),
                    Challenge(
                        title = "Hydro Flow (2.5L / Day)",
                        description = "Maintain healthy hydration levels throughout your active day.",
                        currentProgress = 1500,
                        targetGoal = 2500,
                        unit = "ml",
                        joined = true,
                        rewardBadge = "💧 Hydration Master",
                        category = "Wellness"
                    )
                )
            )

            // Initial Beta Testing / Lab 06 Bug Reports (referenced in Case Study 150-user beta)
            dao.insertBugReport(
                BugReport(
                    title = "AI Workout swap exercise animation frame rate drop on low-end devices",
                    severity = "Medium",
                    component = "AI Engine",
                    description = "When swapping exercises in 10-min adaptive mode, frame rate dipped slightly on Android 10. Resolved with lazy rendering.",
                    status = "Resolved",
                    timestamp = now - (3 * dayMs)
                )
            )
            dao.insertBugReport(
                BugReport(
                    title = "Camera nutrition macro recalculation debounce",
                    severity = "Low",
                    component = "Camera Logging",
                    description = "Ensure macro recalculation waits 250ms when user adjusts portion size slider.",
                    status = "Resolved",
                    timestamp = now - (2 * dayMs)
                )
            )
            dao.insertBugReport(
                BugReport(
                    title = "Verify GDPR data export format in Settings",
                    severity = "Low",
                    component = "Privacy / Compliance",
                    description = "Ensure anonymized JSON export meets Article 20 requirements.",
                    status = "Open",
                    timestamp = now - (1 * dayMs)
                )
            )
        }
    }
}
