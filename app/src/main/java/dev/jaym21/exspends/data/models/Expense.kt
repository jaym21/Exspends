package dev.jaym21.exspends.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "expenses_table")
data class Expense(
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
    val dateTimestamp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
): Parcelable
