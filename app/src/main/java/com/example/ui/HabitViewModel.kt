package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.HabitCategory
import com.example.data.HabitCompletionEntity
import com.example.data.HabitEntity
import com.example.data.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class HabitItemUi(
    val id: Long,
    val title: String,
    val category: String,
    val emoji: String,
    val isCompleted: Boolean,
    val streak: Int
)

data class DayItemUi(
    val date: LocalDate,
    val dateString: String,
    val dayName: String,
    val dayNumber: String,
    val isToday: Boolean,
    val isSelected: Boolean
)

data class HabitUiState(
    val habits: List<HabitItemUi> = emptyList(),
    val filteredHabits: List<HabitItemUi> = emptyList(),
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val progress: Float = 0f,
    val progressPercentage: Int = 0,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedCategory: String = "All",
    val recentDays: List<DayItemUi> = emptyList(),
    val isAllCompletedToday: Boolean = false
)

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    private val selectedDateFlow = MutableStateFlow(LocalDate.now())
    private val selectedCategoryFlow = MutableStateFlow("All")

    init {
        val database = AppDatabase.getDatabase(application)
        repository = HabitRepository(database.habitDao())
    }

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val uiState: StateFlow<HabitUiState> = combine(
        repository.allHabits,
        repository.allCompletions,
        selectedDateFlow,
        selectedCategoryFlow
    ) { habits, completions, selectedDate, categoryFilter ->
        val selectedDateStr = selectedDate.format(dateFormatter)
        val completionsByHabit = completions.groupBy { it.habitId }

        val habitUiItems = habits.map { habit ->
            val habitCompletions = completionsByHabit[habit.id].orEmpty()
            val completedDatesSet = habitCompletions.map { it.date }.toSet()
            val isCompleted = completedDatesSet.contains(selectedDateStr)
            val streak = HabitRepository.calculateStreak(completedDatesSet, selectedDate)

            HabitItemUi(
                id = habit.id,
                title = habit.title,
                category = habit.category,
                emoji = habit.emoji,
                isCompleted = isCompleted,
                streak = streak
            )
        }

        val filtered = if (categoryFilter == "All") {
            habitUiItems
        } else {
            habitUiItems.filter { it.category.equals(categoryFilter, ignoreCase = true) }
        }

        val total = habitUiItems.size
        val completed = habitUiItems.count { it.isCompleted }
        val progress = if (total > 0) completed.toFloat() / total.toFloat() else 0f
        val percentage = (progress * 100).toInt()

        val today = LocalDate.now()
        val daysList = (-4..2).map { offset ->
            val date = today.plusDays(offset.toLong())
            DayItemUi(
                date = date,
                dateString = date.format(dateFormatter),
                dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                dayNumber = date.dayOfMonth.toString(),
                isToday = date == today,
                isSelected = date == selectedDate
            )
        }

        HabitUiState(
            habits = habitUiItems,
            filteredHabits = filtered,
            totalCount = total,
            completedCount = completed,
            progress = progress,
            progressPercentage = percentage,
            selectedDate = selectedDate,
            selectedCategory = categoryFilter,
            recentDays = daysList,
            isAllCompletedToday = total > 0 && completed == total
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HabitUiState()
    )

    fun toggleHabit(habitId: Long, isCurrentlyCompleted: Boolean) {
        viewModelScope.launch {
            val dateStr = selectedDateFlow.value.format(dateFormatter)
            repository.toggleHabitCompletion(habitId, dateStr, isCurrentlyCompleted)
        }
    }

    fun addHabit(title: String, category: String, emoji: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insertHabit(title, category, emoji)
        }
    }

    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }

    fun selectDate(date: LocalDate) {
        selectedDateFlow.value = date
    }

    fun selectCategory(category: String) {
        selectedCategoryFlow.value = category
    }

    fun resetTodayCompletions() {
        viewModelScope.launch {
            val dateStr = selectedDateFlow.value.format(dateFormatter)
            repository.resetDayCompletions(dateStr)
        }
    }
}
