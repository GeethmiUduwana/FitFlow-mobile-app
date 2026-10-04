package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Challenge
import com.example.data.CirclePost
import com.example.ui.theme.CoralEnergyDark
import com.example.ui.theme.CyanAccentDark
import com.example.ui.theme.MintPrimaryDark
import com.example.viewmodel.FitFlowViewModel

@Composable
fun CirclesScreen(
    viewModel: FitFlowViewModel
) {
    val posts by viewModel.circlePosts.collectAsState()
    val challenges by viewModel.challenges.collectAsState()
    val activePersona by viewModel.activePersona.collectAsState()

    var showCreatePostDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All Circles") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090E1A))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
    ) {
        // Header & Privacy Callout
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Accountability Circles",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Safe, encouraging peer accountability",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }

                FilledTonalButton(
                    onClick = { showCreatePostDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MintPrimaryDark,
                        contentColor = Color(0xFF003822)
                    ),
                    modifier = Modifier.testTag("create_circle_post_button")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Post", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Privacy Guarantee Banner (Directly responds to Priya's need for safety)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF131D31),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = CyanAccentDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Private by Design",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Posts are shared strictly with your designated circle. No open follower pressure or public vanity metrics.",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Community & Habit Challenges
        item {
            Text(
                text = "ACTIVE ACCOUNTABILITY CHALLENGES",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(challenges) { challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        onToggleJoin = { viewModel.toggleJoinChallenge(challenge) }
                    )
                }
            }
        }

        // Social Circle Activity Feed
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CIRCLE ACTIVITY FEED",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                // Circle filter chips
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("All", "Close Circle").forEach { filter ->
                        val isSelected = (filter == "All" && selectedFilter == "All Circles") || (filter == selectedFilter)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MintPrimaryDark.copy(alpha = 0.2f) else Color(0xFF1E293B),
                            modifier = Modifier.clickable { selectedFilter = if (filter == "All") "All Circles" else "Close Circle" }
                        ) {
                            Text(
                                text = filter,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MintPrimaryDark else Color(0xFF94A3B8),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        val filteredPosts = if (selectedFilter == "Close Circle") {
            posts.filter { it.privacyLevel == "Close Circle" }
        } else posts

        items(filteredPosts) { post ->
            CirclePostCard(
                post = post,
                onGiveProps = { viewModel.toggleProps(post) }
            )
        }
    }

    // New Circle Post Dialog
    if (showCreatePostDialog) {
        var postText by remember { mutableStateOf("") }
        var privacyLevel by remember { mutableStateOf("Close Circle") }
        var workoutTag by remember { mutableStateOf("Completed: 20m Daily Flow") }

        AlertDialog(
            onDismissRequest = { showCreatePostDialog = false },
            title = { Text("Share with Your Circle", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = postText,
                        onValueChange = { postText = it },
                        placeholder = { Text("Share a win, reflection, or encouragement...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = workoutTag,
                        onValueChange = { workoutTag = it },
                        label = { Text("Activity Tag (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("PRIVACY LEVEL:", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Close Circle", "Public Community").forEach { level ->
                            val isSelected = privacyLevel == level
                            FilterChip(
                                selected = isSelected,
                                onClick = { privacyLevel = level },
                                label = { Text(level, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (postText.isNotBlank()) {
                            viewModel.createCirclePost(postText, privacyLevel, workoutTag)
                            showCreatePostDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimaryDark)
                ) {
                    Text("Post to Circle", color = Color(0xFF003822), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreatePostDialog = false }) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            },
            containerColor = Color(0xFF131D31)
        )
    }
}

@Composable
fun ChallengeCard(
    challenge: Challenge,
    onToggleJoin: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .testTag("challenge_card_${challenge.title.take(6)}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF233554))
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = challenge.rewardBadge,
                        fontSize = 11.sp,
                        color = CyanAccentDark,
                        fontWeight = FontWeight.Bold
                    )
                    if (challenge.joined) {
                        Surface(
                            shape = CircleShape,
                            color = MintPrimaryDark.copy(alpha = 0.2f),
                            modifier = Modifier.size(18.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MintPrimaryDark,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = challenge.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = challenge.description,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 15.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Progress", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Text(
                        "${challenge.currentProgress} / ${challenge.targetGoal} ${challenge.unit}",
                        fontSize = 11.sp,
                        color = MintPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { (challenge.currentProgress.toFloat() / challenge.targetGoal.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MintPrimaryDark,
                    trackColor = Color(0xFF1E293B)
                )

                Spacer(Modifier.height(10.dp))

                FilledTonalButton(
                    onClick = onToggleJoin,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (challenge.joined) Color(0xFF1E293B) else MintPrimaryDark.copy(alpha = 0.2f),
                        contentColor = if (challenge.joined) Color(0xFFCBD5E1) else MintPrimaryDark
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        if (challenge.joined) "Joined • Active" else "Join Challenge",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CirclePostCard(
    post: CirclePost,
    onGiveProps: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Author header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(post.avatarColorHex),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = post.authorName.split(" ").map { it.take(1) }.joinToString(""),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = post.authorName,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Text(
                            text = post.authorRole,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1E293B)
                ) {
                    Text(
                        text = "🔒 ${post.privacyLevel}",
                        fontSize = 10.sp,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (!post.workoutTag.isNullOrBlank()) {
                Spacer(Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MintPrimaryDark.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = MintPrimaryDark,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = post.workoutTag,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintPrimaryDark
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = post.content,
                fontSize = 13.sp,
                color = Color(0xFFE2E8F0),
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(14.dp))

            // Action: Give Props Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onGiveProps,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (post.isUserPropped) CoralEnergyDark.copy(alpha = 0.2f) else Color(0xFF1E293B),
                        contentColor = if (post.isUserPropped) CoralEnergyDark else Color(0xFFCBD5E1)
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("give_props_button_${post.id}")
                ) {
                    Icon(
                        Icons.Default.Celebration,
                        contentDescription = "Give Props",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (post.isUserPropped) "Propped (${post.propsCount})" else "Give Props (${post.propsCount})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Cheered by circle",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}
