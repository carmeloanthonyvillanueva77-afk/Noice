package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitflow_database"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = getDatabase(context).habitDao()
                    // Seed initial habits for immediate delightful preview
                    val initialHabits = listOf(
                        HabitEntity(title = "Morning Hydration (1L)", category = "Health", emoji = "💧"),
                        HabitEntity(title = "45 Min Deep Work Session", category = "Productivity", emoji = "⚡"),
                        HabitEntity(title = "10 Min Mindful Meditation", category = "Mindset", emoji = "🧘"),
                        HabitEntity(title = "30 Min Brisk Nature Walk", category = "Fitness", emoji = "🏃"),
                        HabitEntity(title = "Read 15 Pages of Book", category = "Learning", emoji = "📚")
                    )
                    dao.insertHabits(initialHabits)
                }
            }
        }
    }
}
