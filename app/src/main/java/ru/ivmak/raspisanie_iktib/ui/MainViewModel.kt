package ru.ivmak.raspisanie_iktib.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ivmak.timetable.core.repo.GroupDataSource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val groupDataSource: GroupDataSource
): ViewModel() {

    var group: String? = null
        private set

    var isReady = false
        private set

    init {
        viewModelScope.launch {
            group = groupDataSource.getSelectedGroup()
            isReady = true
        }
    }

    fun setSelectedGroup(group: String) {
        viewModelScope.launch {
            groupDataSource.setSelectedGroup(group)
        }
    }

}
