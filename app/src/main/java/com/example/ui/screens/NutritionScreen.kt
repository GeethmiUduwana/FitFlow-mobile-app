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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NutritionLog
import com.example.ui.theme.CoralEnergyDark
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.MintPrimaryDark
import com.example.viewmodel.FitFlowViewModel

@Composable
fun NutritionScreen(
    viewModel: FitFlowViewModel,
    onOpenCameraScanner: () -> Unit
) {
    val nutritionLogs by viewModel.nutritionLogs.collectAsState()
    val waterIntake by viewModel.waterIntakeMl.collectAsState()

    val totalCals = remember(nutritionLogs) { nutritionLogs.sumOf { it.calories } }
    val totalProtein = remember(nutritionLogs) { nutritionLogs.sumOf { it.proteinG } }
    val totalCarbs = remember(nutritionLogs) { nutritionLogs.sumOf { it.carbsG } }
    val totalFat = remember(nutritionLogs) { nutritionLogs.sumOf { it.fatG } }

    var showQuickAddDialog by remember { mutableStateOf(false) }

    val calorieTarget = 2200
    val proteinTarget = 140
    val carbsTarget = 210
    val fatTarget = 70

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
                        text = "Frictionless Nutrition",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Instant camera-first macro & meal recognition",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }

                FilledTonalButton(
                    onClick = { showQuickAddDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF1E293B),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("quick_add_meal_text_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Manual", fontSize = 12.sp)
                }
            }
        }

        // Prominent Instant Camera Scanner Hero Card (Targeted to solve the 68% drop-off!)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("camera_scanner_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF233554))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF112239), Color(0xFF0F1A2D))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CyanAccentDark.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "ZERO-FRICTION LOGGING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccentDark,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "Snap Your Meal with AI Vision",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "FitFlow identifies dishes, calculates macros, and logs in under 3 seconds.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(Modifier.height(18.dp))

                        // Large Camera Button
                        Button(
                            onClick = onOpenCameraScanner,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("open_camera_nutrition_scanner_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccentDark)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFF002233), modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Open AI Camera Scanner",
                                color = Color(0xFF002233),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Daily Macro Dashboard
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TODAY'S CALORIE BUDGET",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$totalCals",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = " / $calorieTarget kcal",
                                    fontSize = 14.sp,
                                    color = Color(0xFF94A3B8),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 3.dp)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (totalCals <= calorieTarget) MintPrimaryDark.copy(alpha = 0.15f) else CoralEnergyDark.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "${(calorieTarget - totalCals).coerceAtLeast(0)} kcal left",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (totalCals <= calorieTarget) MintPrimaryDark else CoralEnergyDark,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Linear Macro Progress Bars
                    MacroProgressRow("Protein", totalProtein, proteinTarget, "g", MintPrimaryDark)
                    Spacer(Modifier.height(10.dp))
                    MacroProgressRow("Carbs", totalCarbs, carbsTarget, "g", CyanAccentDark)
                    Spacer(Modifier.height(10.dp))
                    MacroProgressRow("Fats", totalFat, fatTarget, "g", CoralEnergyDark)
                }
            }
        }

        // Today's Meals List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S MEAL ENTRIES",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${nutritionLogs.size} logged",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        if (nutritionLogs.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF131D31),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No meals logged today. Tap the AI Camera Scanner above to scan your food instantly!",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(nutritionLogs) { item ->
                NutritionLogCard(
                    log = item,
                    onDelete = { viewModel.deleteMeal(item.id) }
                )
            }
        }
    }

    // Manual Quick Add Dialog
    if (showQuickAddDialog) {
        var mealType by remember { mutableStateOf("Snack") }
        var foodName by remember { mutableStateOf("Protein Bar") }
        var calories by remember { mutableStateOf("210") }
        var protein by remember { mutableStateOf("20") }
        var carbs by remember { mutableStateOf("22") }
        var fat by remember { mutableStateOf("7") }

        AlertDialog(
            onDismissRequest = { showQuickAddDialog = false },
            title = { Text("Log Food Manually", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = foodName,
                        onValueChange = { foodName = it },
                        label = { Text("Food / Drink Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories (kcal)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = protein,
                            onValueChange = { protein = it },
                            label = { Text("Protein (g)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = carbs,
                            onValueChange = { carbs = it },
                            label = { Text("Carbs (g)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = fat,
                            onValueChange = { fat = it },
                            label = { Text("Fat (g)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val c = calories.toIntOrNull() ?: 200
                        val p = protein.toIntOrNull() ?: 15
                        val cb = carbs.toIntOrNull() ?: 20
                        val f = fat.toIntOrNull() ?: 5
                        viewModel.logMeal(mealType, foodName, c, p, cb, f, false, "Manual entry")
                        showQuickAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccentDark)
                ) {
                    Text("Save Food", color = Color(0xFF002233), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuickAddDialog = false }) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            },
            containerColor = Color(0xFF131D31)
        )
    }
}

@Composable
fun MacroProgressRow(name: String, current: Int, target: Int, unit: String, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 12.sp, color = Color(0xFFCBD5E1), fontWeight = FontWeight.Medium)
            Text("$current / $target $unit", fontSize = 12.sp, color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF1E293B)
        )
    }
}

@Composable
fun NutritionLogCard(log: NutritionLog, onDelete: () -> Unit) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (log.isCameraScanned) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyanAccentDark.copy(alpha = 0.15f),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "📷 AI Scanned",
                                fontSize = 10.sp,
                                color = CyanAccentDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = log.foodName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${log.mealType} • ${log.portionDescription}",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "P: ${log.proteinG}g  C: ${log.carbsG}g  F: ${log.fatG}g",
                    fontSize = 11.sp,
                    color = MintPrimaryDark
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${log.calories} kcal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = CoralEnergyDark
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
