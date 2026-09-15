package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HabitItemUi
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.ForestDeepEmerald
import com.example.ui.theme.ForestPastelSage
import com.example.ui.theme.ForestSoftSage
import com.example.ui.theme.ForestUltraLight
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.StreakGold
import com.example.ui.theme.StreakOrange

@Composable
fun HabitItemCard(
    habit: HabitItemUi,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = habit.isCompleted

    // Animated scale on toggle
    val checkScale by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0.9f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "checkScale"
    )

    // Animated background tint
    val cardBackground by animateColorAsState(
        targetValue = if (isCompleted) ForestUltraLight else CrispWhite,
        animationSpec = tween(durationMillis = 300),
        label = "cardBgColor"
    )

    val cardBorderColor by animateColorAsState(
        targetValue = if (isCompleted) ForestPastelSage else SlateBorder,
        animationSpec = tween(durationMillis = 300),
        label = "cardBorderColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("habit_card_${habit.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        border = BorderStroke(1.2.dp, cardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Emoji avatar
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isCompleted) ForestPastelSage.copy(alpha = 0.5f) else ForestUltraLight,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = habit.emoji,
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Center: Title, Category, Streak
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onToggle() }
            ) {
                Text(
                    text = habit.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCompleted) SlateLight else SlateDark,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category Tag
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ForestDeepEmerald.copy(alpha = 0.08f),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = habit.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = ForestDeepEmerald,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }

                    // Streak Pill
                    if (habit.streak > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StreakGold.copy(alpha = 0.15f),
                            border = BorderStroke(0.5.dp, StreakOrange.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "🔥 ${habit.streak}d streak",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StreakOrange,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "New habit",
                            fontSize = 11.sp,
                            color = SlateMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right: Delete button & Interactive Checkbox
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("delete_habit_${habit.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete habit",
                        tint = SlateLight.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Checkbox button with satisfying bounce & fill animation
                Surface(
                    shape = CircleShape,
                    color = if (isCompleted) ForestDeepEmerald else Color.Transparent,
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (isCompleted) ForestDeepEmerald else SlateMuted
                    ),
                    modifier = Modifier
                        .size(36.dp)
                        .scale(checkScale)
                        .clip(CircleShape)
                        .clickable { onToggle() }
                        .testTag("habit_toggle_${habit.id}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed checkmark",
                                tint = ForestSoftSage,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
