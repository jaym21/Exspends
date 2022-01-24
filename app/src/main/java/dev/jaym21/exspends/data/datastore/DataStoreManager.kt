package dev.jaym21.exspends.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dev.jaym21.exspends.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {

    companion object {
        private val FIRST_STARTUP = booleanPreferencesKey("FIRST_STARTUP")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE)
    }

    suspend fun saveIsFirstStartUp(isFirstStartUp: Boolean) {
        context.dataStore.edit {
            it[FIRST_STARTUP] = isFirstStartUp
        }
    }

    val isFirstStartUp: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[FIRST_STARTUP] ?: true
        }
}