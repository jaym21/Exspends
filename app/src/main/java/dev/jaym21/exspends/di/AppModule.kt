package dev.jaym21.exspends.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jaym21.exspends.data.db.AppDatabase
import dev.jaym21.exspends.data.repository.ExpenseRepository
import dev.jaym21.exspends.data.repository.MonthlyExpenseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "app_database")
            .build()

    @Provides
    @Singleton
    fun provideExpenseRepository(appDatabase: AppDatabase): ExpenseRepository {
        return ExpenseRepository(appDatabase)
    }

    @Provides
    @Singleton
    fun provideMonthlyExpenseRepository(appDatabase: AppDatabase): MonthlyExpenseRepository {
        return MonthlyExpenseRepository(appDatabase)
    }
}