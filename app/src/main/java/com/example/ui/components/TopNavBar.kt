package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.model.Persona
import com.example.model.PredefinedPersonas
import com.example.ui.theme.CoralEnergyDark
import com.example.ui.theme.MintPrimaryDark
import com.example.ui.theme.CyanAccentDark

@Composable
fun TopNavBar(
    activePersona: Persona,
    onSelectPersona: (Persona) -> Unit,
    onOpenLabSuite: () -> Unit
) {
    var showPersonaDialog by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF090E1A),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showPersonaDialog = true }
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MintPrimaryDark.copy(alpha = 0.15f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = "FitFlow Brand",
                            tint = MintPrimaryDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FitFlow",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1E293B)
                        ) {
                            Text(
                                text = "v2.0",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintPrimaryDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Active Persona Subtext
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(activePersona.avatarColorHex))
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Persona: ${activePersona.name}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // Right Actions: Streak & Lab 06 Portal
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Streak Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CoralEnergyDark.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoralEnergyDark.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = CoralEnergyDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "7d Flow",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoralEnergyDark
                        )
                    }
                }

                // Lab 06 & Release Portal Button
                FilledTonalButton(
                    onClick = onOpenLabSuite,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = CyanAccentDark.copy(alpha = 0.2f),
                        contentColor = CyanAccentDark
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("open_lab06_suite_button")
                ) {
                    Icon(
                        Icons.Default.Science,
                        contentDescription = "Lab 06 Release Suite",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Lab 06",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Persona Switcher Dialog (Directly humanizes the HCI Case Study findings!)
    if (showPersonaDialog) {
        AlertDialog(
            onDismissRequest = { showPersonaDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, contentDescription = null, tint = CyanAccentDark)
                    Spacer(Modifier.width(8.dp))
                    Text("Select User Persona", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        "Experience the FitFlow redesign through the HCI Case Study research personas:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(Modifier.height(16.dp))

                    PredefinedPersonas.forEach { persona ->
                        val isSelected = persona.id == activePersona.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onSelectPersona(persona)
                                    showPersonaDialog = false
                                }
                                .testTag("select_persona_${persona.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF131D31)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSelected) MintPrimaryDark else Color(0xFF243247)
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(persona.avatarColorHex),
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = persona.initials,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                        Spacer(Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "${persona.name} (${persona.age})",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 15.sp
                                            )
                                            Text(
                                                text = persona.occupation,
                                                fontSize = 12.sp,
                                                color = Color(0xFF94A3B8)
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Active",
                                            tint = MintPrimaryDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = persona.bio,
                                    fontSize = 12.sp,
                                    color = Color(0xFFCBD5E1),
                                    lineHeight = 16.sp
                                )
                                Spacer(Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF0F172A)
                                ) {
                                    Text(
                                        text = "Goal: ${persona.goal}",
                                        fontSize = 11.sp,
                                        color = CyanAccentDark,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPersonaDialog = false }) {
                    Text("Close", color = Color.White)
                }
            },
            containerColor = Color(0xFF0F172A)
        )
    }
}
