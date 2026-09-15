package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // Health, Productivity, Mindset, Fitness, Learning, Other
    val emoji: String,
    val createdAt: Long = System.currentTimeMillis()
)

enum class HabitCategory(
    val displayName: String,
    val defaultEmoji: String,
    val hexColor: String
) {
    HEALTH("Health", "💧", "#2D6A4F"),
    PRODUCTIVITY("Productivity", "⚡", "#1B4332"),
    MINDSET("Mindset", "🧘", "#40916C"),
    FITNESS("Fitness", "🏃", "#52B788"),
    LEARNING("Learning", "📚", "#1E3A8A"),
    OTHER("Other", "✨", "#64748B");

    companion object {
        fun fromString(name: String): HabitCategory {
            return entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: OTHER
        }
    }
}
