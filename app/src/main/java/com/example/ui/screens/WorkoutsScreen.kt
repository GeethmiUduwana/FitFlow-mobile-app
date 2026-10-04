package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkoutLog
import com.example.ui.theme.CoralEnergyDark
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.MintPrimaryDark
import com.example.viewmodel.FitFlowViewModel

@Composable
fun WorkoutsScreen(
    viewModel: FitFlowViewModel,
    onStartWorkout: () -> Unit
) {
    val aiWorkout by viewModel.aiGeneratedWorkout.collectAsState()
    val workoutLogs by viewModel.workoutLogs.collectAsState()
    val activePersona by viewModel.activePersona.collectAsState()
    val isGenerating by viewModel.isGeneratingAi.collectAsState()

    var showManualLogDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090E1A))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AI Workout Engine",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Real-time adaptive workout architecture",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }

                FilledTonalButton(
                    onClick = { showManualLogDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF1E293B),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("manual_workout_log_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Log", fontSize = 12.sp)
                }
            }
        }

        // Active Adaptive Flow Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_ai_workout_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyanAccentDark.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "ADAPTIVE FLOW ENGINE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccentDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.generateDailyFlow() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Regenerate",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = aiWorkout?.title ?: "Personalized Flow",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "${aiWorkout?.estimatedMinutes ?: 18} Minutes • ~${aiWorkout?.calories ?: 180} Calories • ${aiWorkout?.exercises?.size ?: 4} Exercises",
                        fontSize = 13.sp,
                        color = MintPrimaryDark,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = aiWorkout?.adaptationReason ?: "",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 16.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("launch_workout_session_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF003822))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Launch Interactive Session",
                            color = Color(0xFF003822),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Exercises Breakdown in Current Adaptive Plan
        item {
            Text(
                text = "FLOW EXERCISE SEQUENCE",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        items(aiWorkout?.exercises ?: emptyList()) { exercise ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = exercise.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${exercise.targetMuscle} • ${exercise.repsOrHold}",
                            fontSize = 12.sp,
                            color = CyanAccentDark,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = exercise.tips,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Text(
                            text = "${exercise.durationSeconds}s",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Workout Log History
        item {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "LOGGED SESSIONS HISTORY",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        if (workoutLogs.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF131D31),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No recorded sessions yet.",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(workoutLogs) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = log.title,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "${log.durationMinutes} min • ${log.intensity} • ${log.caloriesBurned} kcal",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )
                            if (log.notes.isNotBlank()) {
                                Text(
                                    text = log.notes,
                                    fontSize = 11.sp,
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.deleteWorkout(log.id) }) {
                            Icon(
                                Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Manual Quick Log Dialog
    if (showManualLogDialog) {
        var title by remember { mutableStateOf("Outdoor Cycling Flow") }
        var category by remember { mutableStateOf("Cardio") }
        var duration by remember { mutableStateOf("25") }
        var calories by remember { mutableStateOf("220") }
        var notes by remember { mutableStateOf("Smooth cadence in the park.") }

        AlertDialog(
            onDismissRequest = { showManualLogDialog = false },
            title = { Text("Log Custom Session", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Session Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Minutes") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = calories,
                            onValueChange = { calories = it },
                            label = { Text("Calories") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Reflection / Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val d = duration.toIntOrNull() ?: 20
                        val c = calories.toIntOrNull() ?: 180
                        viewModel.logCompletedWorkout(title, category, d, c, "Custom Log", notes)
                        showManualLogDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark)
                ) {
                    Text("Save Session", color = Color(0xFF003822), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualLogDialog = false }) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            },
            containerColor = Color(0xFF131D31)
        )
    }
}
