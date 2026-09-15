package com.example

import com.example.data.HabitCategory
import com.example.data.HabitRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ExampleUnitTest {
    @Test
    fun streak_calculation_consecutive_days() {
        val today = LocalDate.of(2026, 9, 14)
        val completedDates = setOf(
            "2026-09-14",
            "2026-09-13",
            "2026-09-12",
            "2026-09-11"
        )
        val streak = HabitRepository.calculateStreak(completedDates, today)
        assertEquals(4, streak)
    }

    @Test
    fun streak_calculation_keeps_alive_from_yesterday() {
        val today = LocalDate.of(2026, 9, 14)
        // Today is not completed yet, but yesterday and previous days were
        val completedDates = setOf(
            "2026-09-13",
            "2026-09-12",
            "2026-09-11"
        )
        val streak = HabitRepository.calculateStreak(completedDates, today)
        assertEquals(3, streak)
    }

    @Test
    fun category_lookup_by_string() {
        val cat = HabitCategory.fromString("Health")
        assertEquals(HabitCategory.HEALTH, cat)
        assertEquals("💧", cat.defaultEmoji)
    }
}
