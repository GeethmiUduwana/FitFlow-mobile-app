package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FitFlowDao {
    // Workouts
    @Query("SELECT * FROM workout_logs ORDER BY completedTimestamp DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLog): Long

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteWorkoutLog(id: Long)

    // Nutrition
    @Query("SELECT * FROM nutrition_logs ORDER BY timestamp DESC")
    fun getAllNutritionLogs(): Flow<List<NutritionLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionLog(log: NutritionLog): Long

    @Query("DELETE FROM nutrition_logs WHERE id = :id")
    suspend fun deleteNutritionLog(id: Long)

    // Social Circles
    @Query("SELECT * FROM circle_posts ORDER BY timestamp DESC")
    fun getAllCirclePosts(): Flow<List<CirclePost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCirclePost(post: CirclePost): Long

    @Update
    suspend fun updateCirclePost(post: CirclePost)

    // Challenges
    @Query("SELECT * FROM challenges ORDER BY id ASC")
    fun getAllChallenges(): Flow<List<Challenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<Challenge>)

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    // Bug Reports (Lab 06 QA tracking)
    @Query("SELECT * FROM bug_reports ORDER BY timestamp DESC")
    fun getAllBugReports(): Flow<List<BugReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBugReport(report: BugReport): Long

    @Update
    suspend fun updateBugReport(report: BugReport)
}
