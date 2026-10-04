package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.MintPrimaryDark
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.CoralEnergyDark
import com.example.viewmodel.GeneratedWorkout
import kotlinx.coroutines.delay

@Composable
fun WorkoutPlayerDialog(
    workout: GeneratedWorkout,
    onDismiss: () -> Unit,
    onComplete: (durationMins: Int, caloriesBurned: Int, notes: String) -> Unit,
    onAdapt: (scaleDown: Boolean) -> Unit
) {
    var currentExerciseIndex by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(true) }
    var secondsLeft by remember {
        mutableIntStateOf(workout.exercises.getOrNull(0)?.durationSeconds ?: 60)
    }
    var totalSecondsElapsed by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }
    var showAdaptSheet by remember { mutableStateOf(false) }

    val currentExercise = workout.exercises.getOrNull(currentExerciseIndex)

    LaunchedEffect(isRunning, secondsLeft, isFinished) {
        if (isRunning && !isFinished) {
            while (secondsLeft > 0 && isRunning) {
                delay(1000L)
                secondsLeft -= 1
                totalSecondsElapsed += 1
            }
            if (secondsLeft <= 0 && isRunning) {
                if (currentExerciseIndex < workout.exercises.lastIndex) {
                    currentExerciseIndex += 1
                    secondsLeft = workout.exercises[currentExerciseIndex].durationSeconds
                } else {
                    isFinished = true
                    isRunning = false
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090E1A))
                .padding(20.dp)
        ) {
            if (!isFinished && currentExercise != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("workout_exit_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Exit Workout", tint = Color.White)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "FITFLOW AI ACTIVE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MintPrimaryDark,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = workout.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        FilledTonalButton(
                            onClick = { showAdaptSheet = true },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = CyanAccentDark.copy(alpha = 0.2f),
                                contentColor = CyanAccentDark
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("ai_adapt_in_session_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Adapt", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Exercise Progress Indicator
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = { (currentExerciseIndex + 1).toFloat() / workout.exercises.size.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MintPrimaryDark,
                            trackColor = Color(0xFF1E293B)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Exercise ${currentExerciseIndex + 1} of ${workout.exercises.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // Center Focus Display: Big Timer & Exercise Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MintPrimaryDark.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = currentExercise.targetMuscle.uppercase(),
                                    color = MintPrimaryDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Text(
                                text = currentExercise.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            // Circular Timer Display
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(Color(0xFF1E2F4D), Color(0xFF0F1A2D))
                                        )
                                    )
                                    .border(3.dp, MintPrimaryDark, CircleShape)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val mins = secondsLeft / 60
                                    val secs = secondsLeft % 60
                                    Text(
                                        text = String.format("%02d:%02d", mins, secs),
                                        fontSize = 44.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = currentExercise.repsOrHold,
                                        fontSize = 13.sp,
                                        color = CyanAccentDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Form Tips Box
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF18243C),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = CoralEnergyDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        text = currentExercise.tips,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                            }
                        }
                    }

                    // Playback Controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (currentExerciseIndex > 0) {
                                    currentExerciseIndex -= 1
                                    secondsLeft = workout.exercises[currentExerciseIndex].durationSeconds
                                }
                            },
                            enabled = currentExerciseIndex > 0,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                        ) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.White)
                        }

                        // Play/Pause Big Button
                        Button(
                            onClick = { isRunning = !isRunning },
                            modifier = Modifier
                                .size(74.dp)
                                .testTag("workout_play_pause_button"),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isRunning) "Pause" else "Play",
                                tint = Color(0xFF003822),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentExerciseIndex < workout.exercises.lastIndex) {
                                    currentExerciseIndex += 1
                                    secondsLeft = workout.exercises[currentExerciseIndex].durationSeconds
                                } else {
                                    isFinished = true
                                }
                            },
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                        ) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Next Exercise", tint = Color.White)
                        }
                    }
                }
            } else {
                // Celebration Completion Screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MintPrimaryDark.copy(alpha = 0.2f),
                        modifier = Modifier.size(100.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = "Completed",
                                tint = MintPrimaryDark,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "FLOW COMPLETE!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Incredible effort! You adapted and finished your workout.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(28.dp))

                    // Stats summary card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${totalSecondsElapsed / 60}m ${totalSecondsElapsed % 60}s",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MintPrimaryDark
                                )
                                Text("Duration", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${workout.calories} kcal",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralEnergyDark
                                )
                                Text("Calories", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${workout.exercises.size}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccentDark
                                )
                                Text("Exercises", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val durationMins = (totalSecondsElapsed / 60).coerceAtLeast(1)
                            onComplete(
                                durationMins,
                                workout.calories,
                                "Crushed ${workout.exercises.size} movements in ${workout.title}!"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("save_and_share_workout_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF003822))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Log & Share to Close Circle",
                            color = Color(0xFF003822),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Text("Finish Without Sharing", color = Color(0xFFCBD5E1))
                    }
                }
            }

            // Real-time Adaptation Sheet Dialog
            if (showAdaptSheet) {
                AlertDialog(
                    onDismissRequest = { showAdaptSheet = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccentDark)
                            Spacer(Modifier.width(8.dp))
                            Text("AI Real-time Adapt", color = Color.White)
                        }
                    },
                    text = {
                        Column {
                            Text(
                                "FitFlow's AI dynamically adjusts workout parameters based on your physical feedback.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                            Spacer(Modifier.height(16.dp))

                            FilledTonalButton(
                                onClick = {
                                    showAdaptSheet = false
                                    onAdapt(true)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color(0xFF1E293B),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null, tint = CoralEnergyDark)
                                Spacer(Modifier.width(8.dp))
                                Text("Scale Down (Tired / Short on Time)")
                            }

                            Spacer(Modifier.height(10.dp))

                            FilledTonalButton(
                                onClick = {
                                    showAdaptSheet = false
                                    onAdapt(false)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color(0xFF1E293B),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = MintPrimaryDark)
                                Spacer(Modifier.width(8.dp))
                                Text("Scale Up (High Energy Boost)")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showAdaptSheet = false }) {
                            Text("Cancel", color = Color(0xFF94A3B8))
                        }
                    },
                    containerColor = Color(0xFF131D31)
                )
            }
        }
    }
}
