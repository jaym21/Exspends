package dev.jaym21.exspends.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_table")
data class MonthlyExpense(
    val month: String,
    val year: String,
    val totalAmount: Double,
    val dateTimestamp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
