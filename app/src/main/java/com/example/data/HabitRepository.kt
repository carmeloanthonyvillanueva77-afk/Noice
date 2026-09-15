package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitRepository(private val habitDao: HabitDao) {

    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()
    val allCompletions: Flow<List<HabitCompletionEntity>> = habitDao.getAllCompletions()

    fun getCompletionsForDate(date: String): Flow<List<HabitCompletionEntity>> {
        return habitDao.getCompletionsForDate(date)
    }

    suspend fun insertHabit(title: String, category: String, emoji: String) = withContext(Dispatchers.IO) {
        val habit = HabitEntity(
            title = title.trim(),
            category = category,
            emoji = emoji
        )
        habitDao.insertHabit(habit)
    }

    suspend fun deleteHabit(habitId: Long) = withContext(Dispatchers.IO) {
        habitDao.deleteHabitById(habitId)
        habitDao.deleteAllCompletionsForHabit(habitId)
    }

    suspend fun toggleHabitCompletion(habitId: Long, date: String, isCurrentlyCompleted: Boolean) = withContext(Dispatchers.IO) {
        if (isCurrentlyCompleted) {
            habitDao.deleteCompletion(habitId, date)
        } else {
            habitDao.insertCompletion(HabitCompletionEntity(habitId = habitId, date = date))
        }
    }

    suspend fun resetDayCompletions(date: String) = withContext(Dispatchers.IO) {
        habitDao.deleteAllCompletionsForDate(date)
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE

        fun calculateStreak(completedDates: Set<String>, referenceDate: LocalDate = LocalDate.now()): Int {
            if (completedDates.isEmpty()) return 0

            var streak = 0
            var checkDate = referenceDate

            // If today is not completed, check if yesterday was completed to keep streak alive
            val todayStr = checkDate.format(DATE_FORMATTER)
            if (!completedDates.contains(todayStr)) {
                checkDate = checkDate.minusDays(1)
            }

            // Count consecutive completed days backwards
            while (completedDates.contains(checkDate.format(DATE_FORMATTER))) {
                streak++
                checkDate = checkDate.minusDays(1)
            }

            return streak
        }
    }
}
