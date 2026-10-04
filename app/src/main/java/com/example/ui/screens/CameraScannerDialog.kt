package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.MintPrimaryDark
import com.example.ui.theme.CoralEnergyDark
import kotlinx.coroutines.delay

data class ScannedFoodPreset(
    val name: String,
    val category: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val portion: String
)

val SampleScanPresets = listOf(
    ScannedFoodPreset("Grilled Salmon Quinoa Bowl", "Lunch", 540, 42, 45, 18, "Medium bowl (350g)"),
    ScannedFoodPreset("Avocado Sourdough Toast & Poached Egg", "Breakfast", 390, 19, 36, 18, "2 slices + 1 egg"),
    ScannedFoodPreset("Greek Yogurt Protein Parfait", "Breakfast", 310, 26, 32, 6, "1 cup (220g)"),
    ScannedFoodPreset("Chicken & Sweet Potato Power Plate", "Dinner", 480, 46, 40, 12, "Plate (400g)"),
    ScannedFoodPreset("Whey Protein Shake & Almond Butter", "Snack", 280, 30, 14, 10, "Bottle (450ml)")
)

@Composable
fun CameraScannerDialog(
    onDismiss: () -> Unit,
    onLogMeal: (mealType: String, foodName: String, calories: Int, protein: Int, carbs: Int, fat: Int, portion: String) -> Unit
) {
    var selectedPreset by remember { mutableStateOf(SampleScanPresets[0]) }
    var isScanning by remember { mutableStateOf(false) }
    var scanSuccess by remember { mutableStateOf(true) }
    var selectedMealType by remember { mutableStateOf(selectedPreset.category) }
    var portionMultiplier by remember { mutableFloatStateOf(1.0f) }

    // Scan line animation
    val infiniteTransition = rememberInfiniteTransition(label = "scan_line")
    val scanOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_offset"
    )

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_camera_scanner")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "AI VISION SCANNER",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanAccentDark,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Instant Food Recognition",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MintPrimaryDark)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("ML Ready", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }

                // Camera Viewfinder Simulation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF111827))
                        .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Background Camera Texture Simulation
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Corner brackets
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("┏", fontSize = 28.sp, color = CyanAccentDark)
                            Text("┓", fontSize = 28.sp, color = CyanAccentDark)
                        }

                        // Center Recognition Target
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "Point camera at meal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF94A3B8)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Computer Vision analyzes ingredients & portions in real time",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("┗", fontSize = 28.sp, color = CyanAccentDark)
                            Text("┛", fontSize = 28.sp, color = CyanAccentDark)
                        }
                    }

                    // Dynamic Scanning Laser Line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(2.dp)
                            .offset(y = ((scanOffset - 0.5f) * 200).dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, CyanAccentDark, MintPrimaryDark, Color.Transparent)
                                )
                            )
                    )
                }

                // Quick AI Recognition Presets selector
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "AI DETECTED DISH (TAP TO SWITCH):",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(SampleScanPresets) { preset ->
                            val isSelected = preset.name == selectedPreset.name
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) CyanAccentDark.copy(alpha = 0.2f) else Color(0xFF182234),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) CyanAccentDark else Color(0xFF28364F)
                                ),
                                modifier = Modifier
                                    .clickable {
                                        selectedPreset = preset
                                        selectedMealType = preset.category
                                    }
                                    .testTag("food_preset_${preset.name.take(5)}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = if (isSelected) CyanAccentDark else Color(0xFF94A3B8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = preset.name,
                                        color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Nutrient Breakdown Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = selectedPreset.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = selectedPreset.portion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF94A3B8)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = CoralEnergyDark.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${(selectedPreset.calories * portionMultiplier).toInt()} kcal",
                                    color = CoralEnergyDark,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Macronutrient Bars
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MacroPill(
                                label = "Protein",
                                value = "${(selectedPreset.protein * portionMultiplier).toInt()}g",
                                color = MintPrimaryDark
                            )
                            MacroPill(
                                label = "Carbs",
                                value = "${(selectedPreset.carbs * portionMultiplier).toInt()}g",
                                color = CyanAccentDark
                            )
                            MacroPill(
                                label = "Fats",
                                value = "${(selectedPreset.fat * portionMultiplier).toInt()}g",
                                color = CoralEnergyDark
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Action Button: Add to Daily Flow
                Button(
                    onClick = {
                        val finalCals = (selectedPreset.calories * portionMultiplier).toInt()
                        val finalProtein = (selectedPreset.protein * portionMultiplier).toInt()
                        val finalCarbs = (selectedPreset.carbs * portionMultiplier).toInt()
                        val finalFat = (selectedPreset.fat * portionMultiplier).toInt()
                        onLogMeal(
                            selectedMealType,
                            selectedPreset.name,
                            finalCals,
                            finalProtein,
                            finalCarbs,
                            finalFat,
                            selectedPreset.portion
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("log_scanned_meal_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF003822))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Log Meal to Daily Flow",
                        color = Color(0xFF003822),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MacroPill(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1E293B),
        modifier = Modifier.width(96.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
            Text(label, fontSize = 11.sp, color = Color(0xFF94A3B8))
        }
    }
}
