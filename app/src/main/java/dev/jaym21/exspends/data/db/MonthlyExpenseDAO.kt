package dev.jaym21.exspends.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.jaym21.exspends.data.models.MonthlyExpense
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyExpenseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthExpense(monthlyExpense: MonthlyExpense)

    @Query("SELECT * FROM monthly_table ORDER BY dateTimestamp DESC")
    fun getAllMonthlyExpenses(): Flow<List<MonthlyExpense>>
}