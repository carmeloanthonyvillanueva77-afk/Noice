package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HabitCategory
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.ForestDeepEmerald
import com.example.ui.theme.ForestPastelSage
import com.example.ui.theme.ForestSoftSage
import com.example.ui.theme.ForestUltraLight
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddHabitSection(
    onAddHabit: (title: String, category: String, emoji: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var habitTitle by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(HabitCategory.HEALTH) }
    var selectedEmoji by remember { mutableStateOf(HabitCategory.HEALTH.defaultEmoji) }

    val quickEmojis = listOf("💧", "⚡", "🧘", "🏃", "📚", "🥗", "💤", "☀️", "🎯", "🌿")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("add_habit_container"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CrispWhite),
        border = BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header toggle row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ForestDeepEmerald,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                                contentDescription = null,
                                tint = CrispWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (isExpanded) "Create New Daily Habit" else "Add a Daily Habit",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateDark
                        )
                        if (!isExpanded) {
                            Text(
                                text = "Tap to set title, category & icon",
                                fontSize = 12.sp,
                                color = SlateLight
                            )
                        }
                    }
                }

                if (!isExpanded) {
                    OutlinedButton(
                        onClick = { isExpanded = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ForestUltraLight,
                            contentColor = ForestDeepEmerald
                        ),
                        border = BorderStroke(1.dp, ForestPastelSage),
                        modifier = Modifier.testTag("open_add_habit_button")
                    ) {
                        Text("New", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Expanded input area
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // Habit Title Input Field
                    OutlinedTextField(
                        value = habitTitle,
                        onValueChange = { habitTitle = it },
                        label = { Text("Habit Name") },
                        placeholder = { Text("e.g., 30 Min Morning Reading") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestDeepEmerald,
                            unfocusedBorderColor = SlateBorder,
                            focusedLabelColor = ForestDeepEmerald
                        ),
                        leadingIcon = {
                            Text(text = selectedEmoji, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("habit_title_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Category Selector
                    Text(
                        text = "Category",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = SlateLight
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("add_habit_category_selector"),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        HabitCategory.entries.forEach { category ->
                            val isSelected = selectedCategory == category
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedCategory = category
                                    selectedEmoji = category.defaultEmoji
                                },
                                label = {
                                    Text(
                                        text = "${category.defaultEmoji} ${category.displayName}",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestDeepEmerald,
                                    selectedLabelColor = CrispWhite,
                                    containerColor = ForestUltraLight,
                                    labelColor = SlateDark
                                ),
                                border = if (!isSelected) {
                                    FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = false,
                                        borderColor = SlateBorder
                                    )
                                } else null,
                                modifier = Modifier.testTag("select_category_${category.displayName}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Emoji Selector
                    Text(
                        text = "Choose Icon",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = SlateLight
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickEmojis.take(7).forEach { emoji ->
                            val isSelected = selectedEmoji == emoji
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) ForestPastelSage else ForestUltraLight,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) ForestDeepEmerald else SlateBorder
                                ),
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable { selectedEmoji = emoji }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 18.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                isExpanded = false
                                habitTitle = ""
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = SlateLight)
                        }

                        Button(
                            onClick = {
                                if (habitTitle.isNotBlank()) {
                                    onAddHabit(habitTitle, selectedCategory.displayName, selectedEmoji)
                                    habitTitle = ""
                                    isExpanded = false
                                }
                            },
                            enabled = habitTitle.isNotBlank(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestDeepEmerald,
                                contentColor = CrispWhite,
                                disabledContainerColor = ForestDeepEmerald.copy(alpha = 0.4f),
                                disabledContentColor = CrispWhite.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier
                                .weight(1.6f)
                                .testTag("add_habit_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Habit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
