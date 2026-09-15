package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddHabitSection
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.DailyProgressCard
import com.example.ui.components.DateSelectorStrip
import com.example.ui.components.HabitItemCard
import com.example.ui.components.ResetDayDialog
import com.example.ui.components.WebPreviewDialog
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.ForestDeepEmerald
import com.example.ui.theme.ForestSoftSage
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitFlowScreen(
    viewModel: HabitViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }
    var showWebPreviewDialog by remember { mutableStateOf(false) }

    val formattedSelectedDate = remember(uiState.selectedDate) {
        val today = LocalDate.now()
        when (uiState.selectedDate) {
            today -> "Today, ${uiState.selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
            today.minusDays(1) -> "Yesterday, ${uiState.selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
            today.plusDays(1) -> "Tomorrow, ${uiState.selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
            else -> uiState.selectedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ForestDeepEmerald,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = ForestSoftSage,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "HabitFlow",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ForestDeepEmerald,
                                letterSpacing = (-0.3).sp
                            )
                            Text(
                                text = formattedSelectedDate,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = SlateLight
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showWebPreviewDialog = true },
                        modifier = Modifier.testTag("open_web_preview_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Single-Page Web Preview",
                            tint = ForestDeepEmerald
                        )
                    }

                    IconButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier.testTag("reset_day_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Clean slate for today",
                            tint = SlateLight
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Dynamic Daily Progress Card
                item(key = "progress_card") {
                    DailyProgressCard(
                        completedCount = uiState.completedCount,
                        totalCount = uiState.totalCount,
                        progress = uiState.progress,
                        isToday = uiState.selectedDate == LocalDate.now()
                    )
                }

                // 2. Date Navigation / Consistency Strip
                item(key = "date_strip") {
                    DateSelectorStrip(
                        days = uiState.recentDays,
                        selectedDate = uiState.selectedDate,
                        onSelectDate = { viewModel.selectDate(it) }
                    )
                }

                // 3. Category Filter Tabs
                item(key = "category_tabs") {
                    CategoryFilterRow(
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = { viewModel.selectCategory(it) }
                    )
                }

                // 4. Add Daily Habit Section
                item(key = "add_habit_section") {
                    AddHabitSection(
                        onAddHabit = { title, category, emoji ->
                            viewModel.addHabit(title, category, emoji)
                        }
                    )
                }

                // 5. Habits List Section Header
                item(key = "habits_header") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.selectedCategory == "All") "TODAY'S HABITS" else "${uiState.selectedCategory.uppercase()} HABITS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = SlateLight
                        )

                        Text(
                            text = "${uiState.completedCount}/${uiState.totalCount} completed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestDeepEmerald
                        )
                    }
                }

                // 6. Habit Cards List
                if (uiState.filteredHabits.isEmpty()) {
                    item(key = "empty_habits") {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = CrispWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "🌿", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (uiState.habits.isEmpty()) "No habits yet" else "No habits in ${uiState.selectedCategory}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Add a new daily habit above to cultivate your rhythm.",
                                    fontSize = 13.sp,
                                    color = SlateLight
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = uiState.filteredHabits,
                        key = { it.id }
                    ) { habit ->
                        HabitItemCard(
                            habit = habit,
                            onToggle = { viewModel.toggleHabit(habit.id, habit.isCompleted) },
                            onDelete = { viewModel.deleteHabit(habit.id) }
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (showResetDialog) {
        ResetDayDialog(
            onConfirm = { viewModel.resetTodayCompletions() },
            onDismiss = { showResetDialog = false }
        )
    }

    if (showWebPreviewDialog) {
        WebPreviewDialog(
            onDismiss = { showWebPreviewDialog = false }
        )
    }
}
