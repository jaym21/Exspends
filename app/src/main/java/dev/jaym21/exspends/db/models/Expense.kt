package dev.jaym21.exspends.db.models

import androidx.room.PrimaryKey
import java.text.DateFormat

data class Expense(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val createdAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) {
    val createdAtToDate: String
    get() = DateFormat.getDateTimeInstance().format(createdAt)
}
