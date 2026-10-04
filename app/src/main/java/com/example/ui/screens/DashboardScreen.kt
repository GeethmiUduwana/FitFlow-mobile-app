package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NutritionLog
import com.example.data.WorkoutLog
import com.example.model.Persona
import com.example.ui.theme.CoralEnergyDark
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.MintPrimaryDark
import com.example.viewmodel.FitFlowViewModel

@Composable
fun DashboardScreen(
    viewModel: FitFlowViewModel,
    onStartWorkout: () -> Unit,
    onOpenNutritionScanner: () -> Unit,
    onNavigateToWorkouts: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToCircles: () -> Unit
) {
    val activePersona by viewModel.activePersona.collectAsState()
    val aiWorkout by viewModel.aiGeneratedWorkout.collectAsState()
    val isGeneratingAi by viewModel.isGeneratingAi.collectAsState()
    val workoutLogs by viewModel.workoutLogs.collectAsState()
    val nutritionLogs by viewModel.nutritionLogs.collectAsState()
    val waterIntake by viewModel.waterIntakeMl.collectAsState()
    val currentEnergy by viewModel.currentEnergyLevel.collectAsState()
    val selectedDuration by viewModel.selectedDurationMins.collectAsState()

    val totalCaloriesBurned = remember(workoutLogs) { workoutLogs.sumOf { it.caloriesBurned } }
    val totalMinutes = remember(workoutLogs) { workoutLogs.sumOf { it.durationMinutes } }
    val totalCaloriesConsumed = remember(nutritionLogs) { nutritionLogs.sumOf { it.calories } }
    val totalProtein = remember(nutritionLogs) { nutritionLogs.sumOf { it.proteinG } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090E1A))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
    ) {
        // Welcome & Daily Greeting
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Welcome back, ${activePersona.name.split(" ").first()} 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "AI coach tuned for your ${activePersona.goal.lowercase()}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Hero AI "Daily Flow" Recommendation Card (Core Case Study Feature)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_flow_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF233554))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF1A2744), Color(0xFF10192A))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Badge & AI Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MintPrimaryDark.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MintPrimaryDark.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MintPrimaryDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "DAILY FLOW AI RECOMMENDATION",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MintPrimaryDark
                                    )
                                }
                            }

                            if (isGeneratingAi) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = CyanAccentDark,
                                    strokeWidth = 2.dp
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Workout Title & Metadata
                        Text(
                            text = aiWorkout?.title ?: "AI Adaptive Flow",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = aiWorkout?.adaptationReason ?: "Adaptive workout calculated for today.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(14.dp))

                        // Energy Level Quick Tuning Chips
                        Text(
                            text = "CURRENT ENERGY LEVEL:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Fatigued / Low", "Normal", "High Energy").forEach { energy ->
                                val isSelected = currentEnergy == energy
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.currentEnergyLevel.value = energy
                                        viewModel.generateDailyFlow()
                                    },
                                    label = { Text(energy, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CyanAccentDark.copy(alpha = 0.25f),
                                        selectedLabelColor = CyanAccentDark,
                                        containerColor = Color(0xFF1E293B),
                                        labelColor = Color(0xFF94A3B8)
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = if (isSelected) CyanAccentDark else Color(0xFF334155)
                                    )
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Duration Chips (Solves Alex Rivera's chaotic calendar problem!)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(10, 18, 30, 45).forEach { mins ->
                                    val isSelected = selectedDuration == mins
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) MintPrimaryDark.copy(alpha = 0.2f) else Color(0xFF1E293B),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) MintPrimaryDark else Color(0xFF334155)
                                        ),
                                        modifier = Modifier.clickable {
                                            viewModel.selectedDurationMins.value = mins
                                            viewModel.generateDailyFlow()
                                        }
                                    ) {
                                        Text(
                                            text = "${mins}m",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MintPrimaryDark else Color(0xFFCBD5E1),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Est. ${aiWorkout?.calories ?: 180} kcal",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoralEnergyDark
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // Launch Workout Primary Button
                        Button(
                            onClick = onStartWorkout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_daily_flow_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF003822))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Start Today's Flow (${aiWorkout?.estimatedMinutes ?: 18} min)",
                                color = Color(0xFF003822),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Today's Vital Metrics (Calorie burn, active minutes, water)
        item {
            Text(
                text = "TODAY'S VITAL STATS",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Calories Burned
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Burned",
                    value = "$totalCaloriesBurned",
                    unit = "kcal",
                    icon = Icons.Default.LocalFireDepartment,
                    color = CoralEnergyDark,
                    target = "Goal: 500"
                )

                // Active Time
                MetricCard(
                    modifier = Modifier.weight(1f),
                    title = "Active Flow",
                    value = "$totalMinutes",
                    unit = "min",
                    icon = Icons.Default.Timer,
                    color = MintPrimaryDark,
                    target = "Goal: 30m"
                )

                // Water Hydration with quick add
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hydration_quick_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = CyanAccentDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Surface(
                                shape = CircleShape,
                                color = CyanAccentDark.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { viewModel.addWater(250) }
                                    .testTag("quick_add_water_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add Water",
                                        tint = CyanAccentDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "${waterIntake / 1000f}L",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Goal: 2.5L",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickActionItem(
                        icon = Icons.Default.CameraAlt,
                        label = "Scan Meal",
                        tint = CyanAccentDark,
                        onClick = onOpenNutritionScanner
                    )
                    QuickActionItem(
                        icon = Icons.Default.FitnessCenter,
                        label = "Workouts",
                        tint = MintPrimaryDark,
                        onClick = onNavigateToWorkouts
                    )
                    QuickActionItem(
                        icon = Icons.Default.Restaurant,
                        label = "Nutrition",
                        tint = CoralEnergyDark,
                        onClick = onNavigateToNutrition
                    )
                    QuickActionItem(
                        icon = Icons.Default.Groups,
                        label = "Circles",
                        tint = Color(0xFFA78BFA),
                        onClick = onNavigateToCircles
                    )
                }
            }
        }

        // Recent Workouts Log
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT FLOW SESSIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                TextButton(onClick = onNavigateToWorkouts) {
                    Text("View All", fontSize = 12.sp, color = MintPrimaryDark)
                }
            }

            if (workoutLogs.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF131D31),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No workouts logged yet. Start your first Daily Flow session above!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    workoutLogs.take(3).forEach { log ->
                        WorkoutLogItem(log)
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    target: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.width(2.dp))
                Text(text = unit, fontSize = 12.sp, color = color, fontWeight = FontWeight.SemiBold)
            }
            Text(text = target, fontSize = 11.sp, color = Color(0xFF94A3B8))
        }
    }
}

@Composable
fun QuickActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = tint.copy(alpha = 0.15f),
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(text = label, fontSize = 11.sp, color = Color(0xFFCBD5E1), fontWeight = FontWeight.Medium)
    }
}

@Composable
fun WorkoutLogItem(log: WorkoutLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MintPrimaryDark.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = MintPrimaryDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = log.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "${log.durationMinutes} min • ${log.intensity}",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CoralEnergyDark.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "+${log.caloriesBurned} kcal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoralEnergyDark,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
