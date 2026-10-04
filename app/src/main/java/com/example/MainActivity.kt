package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.TopNavBar
import com.example.ui.screens.*
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.FitFlowTheme
import com.example.ui.theme.MintPrimaryDark
import com.example.viewmodel.FitFlowViewModel

enum class FitFlowScreen(val label: String) {
    DASHBOARD("Daily Flow"),
    WORKOUTS("Workouts"),
    NUTRITION("Nutrition"),
    CIRCLES("Circles"),
    LAB_SUITE("Lab 06")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitFlowTheme(darkTheme = true) {
                FitFlowApp()
            }
        }
    }
}

@Composable
fun FitFlowApp(
    viewModel: FitFlowViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf(FitFlowScreen.DASHBOARD) }
    val activePersona by viewModel.activePersona.collectAsState()
    val aiWorkout by viewModel.aiGeneratedWorkout.collectAsState()

    var isWorkoutPlayerActive by remember { mutableStateOf(false) }
    var isCameraScannerActive by remember { mutableStateOf(false) }

    // Back handling: If on secondary tab or Lab 06, go back to Dashboard
    BackHandler(enabled = currentScreen != FitFlowScreen.DASHBOARD) {
        currentScreen = FitFlowScreen.DASHBOARD
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF090E1A),
        topBar = {
            TopNavBar(
                activePersona = activePersona,
                onSelectPersona = { persona -> viewModel.switchPersona(persona) },
                onOpenLabSuite = { currentScreen = FitFlowScreen.LAB_SUITE }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0E1626),
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("fitflow_bottom_navigation")
            ) {
                NavigationBarItem(
                    selected = currentScreen == FitFlowScreen.DASHBOARD,
                    onClick = { currentScreen = FitFlowScreen.DASHBOARD },
                    icon = {
                        Icon(
                            if (currentScreen == FitFlowScreen.DASHBOARD) Icons.Default.Bolt else Icons.Outlined.Bolt,
                            contentDescription = "Daily Flow"
                        )
                    },
                    label = { Text("Daily Flow", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF003822),
                        selectedTextColor = MintPrimaryDark,
                        indicatorColor = MintPrimaryDark,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )

                NavigationBarItem(
                    selected = currentScreen == FitFlowScreen.WORKOUTS,
                    onClick = { currentScreen = FitFlowScreen.WORKOUTS },
                    icon = {
                        Icon(
                            if (currentScreen == FitFlowScreen.WORKOUTS) Icons.Default.FitnessCenter else Icons.Outlined.FitnessCenter,
                            contentDescription = "Workouts"
                        )
                    },
                    label = { Text("Workouts", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF003822),
                        selectedTextColor = MintPrimaryDark,
                        indicatorColor = MintPrimaryDark,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_workouts")
                )

                NavigationBarItem(
                    selected = currentScreen == FitFlowScreen.NUTRITION,
                    onClick = { currentScreen = FitFlowScreen.NUTRITION },
                    icon = {
                        Icon(
                            if (currentScreen == FitFlowScreen.NUTRITION) Icons.Default.CameraAlt else Icons.Outlined.CameraAlt,
                            contentDescription = "Nutrition"
                        )
                    },
                    label = { Text("Nutrition", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF002233),
                        selectedTextColor = CyanAccentDark,
                        indicatorColor = CyanAccentDark,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_nutrition")
                )

                NavigationBarItem(
                    selected = currentScreen == FitFlowScreen.CIRCLES,
                    onClick = { currentScreen = FitFlowScreen.CIRCLES },
                    icon = {
                        Icon(
                            if (currentScreen == FitFlowScreen.CIRCLES) Icons.Default.Groups else Icons.Outlined.Groups,
                            contentDescription = "Circles"
                        )
                    },
                    label = { Text("Circles", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF003822),
                        selectedTextColor = MintPrimaryDark,
                        indicatorColor = MintPrimaryDark,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_circles")
                )

                NavigationBarItem(
                    selected = currentScreen == FitFlowScreen.LAB_SUITE,
                    onClick = { currentScreen = FitFlowScreen.LAB_SUITE },
                    icon = {
                        Icon(
                            if (currentScreen == FitFlowScreen.LAB_SUITE) Icons.Default.Science else Icons.Outlined.Science,
                            contentDescription = "Lab 06 Suite"
                        )
                    },
                    label = { Text("Lab 06", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF002233),
                        selectedTextColor = CyanAccentDark,
                        indicatorColor = CyanAccentDark,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_lab06")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                FitFlowScreen.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onStartWorkout = { isWorkoutPlayerActive = true },
                    onOpenNutritionScanner = { isCameraScannerActive = true },
                    onNavigateToWorkouts = { currentScreen = FitFlowScreen.WORKOUTS },
                    onNavigateToNutrition = { currentScreen = FitFlowScreen.NUTRITION },
                    onNavigateToCircles = { currentScreen = FitFlowScreen.CIRCLES }
                )
                FitFlowScreen.WORKOUTS -> WorkoutsScreen(
                    viewModel = viewModel,
                    onStartWorkout = { isWorkoutPlayerActive = true }
                )
                FitFlowScreen.NUTRITION -> NutritionScreen(
                    viewModel = viewModel,
                    onOpenCameraScanner = { isCameraScannerActive = true }
                )
                FitFlowScreen.CIRCLES -> CirclesScreen(
                    viewModel = viewModel
                )
                FitFlowScreen.LAB_SUITE -> Lab06SuiteScreen(
                    viewModel = viewModel,
                    onBackToApp = { currentScreen = FitFlowScreen.DASHBOARD }
                )
            }

            // Interactive AI Workout Player Session Dialog
            if (isWorkoutPlayerActive) {
                aiWorkout?.let { workout ->
                    WorkoutPlayerDialog(
                        workout = workout,
                        onDismiss = { isWorkoutPlayerActive = false },
                        onComplete = { duration, calories, notes ->
                            viewModel.logCompletedWorkout(
                                workout.title,
                                "HIIT",
                                duration,
                                calories,
                                workout.difficulty,
                                notes
                            )
                            isWorkoutPlayerActive = false
                        },
                        onAdapt = { scaleDown ->
                            viewModel.adaptWorkoutInRealTime(scaleDown)
                        }
                    )
                }
            }

            // Interactive Camera Vision Nutrition Scanner Dialog
            if (isCameraScannerActive) {
                CameraScannerDialog(
                    onDismiss = { isCameraScannerActive = false },
                    onLogMeal = { mealType, foodName, calories, protein, carbs, fat, portion ->
                        viewModel.logMeal(
                            mealType = mealType,
                            foodName = foodName,
                            calories = calories,
                            proteinG = protein,
                            carbsG = carbs,
                            fatG = fat,
                            isCameraScanned = true,
                            portionDescription = portion
                        )
                        isCameraScannerActive = false
                    }
                )
            }
        }
    }
}
