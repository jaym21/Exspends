package dev.jaym21.exspends.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses_table")
data class Expense(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val dateTimestamp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
