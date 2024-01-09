package ru.ivmak.timetable.core.repo

import ru.ivmak.timetable.core.db.datastore.PreferenceDataStore
import javax.inject.Inject

class GroupDataSource @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) {

    val selectedGroupFlow = preferenceDataStore.selectedGroupFlow

    suspend fun getSelectedGroup(): String? = preferenceDataStore.getSelectedGroup()

    suspend fun setSelectedGroup(value: String) {
        preferenceDataStore.setSelectedGroup(value)
    }

}
