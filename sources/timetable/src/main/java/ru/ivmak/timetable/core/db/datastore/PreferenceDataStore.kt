package ru.ivmak.timetable.core.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferenceDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val SELECTED_GROUP = stringPreferencesKey("SELECTED_GROUP")
    }


    val selectedGroupFlow = context.dataStore.data.map { it[SELECTED_GROUP] }.distinctUntilChanged()
    suspend fun getSelectedGroup(): String? = context.dataStore.data.first()[SELECTED_GROUP]
    suspend fun setSelectedGroup(value: String) {
        context.dataStore.edit { settings ->
            settings[SELECTED_GROUP] = value
        }
    }

}