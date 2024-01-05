package ru.ivmak.raspisanie_iktib.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): ViewModel() {

    val group = "134.html"

    var isReady = false
        private set

    init {
        viewModelScope.launch {
            delay(3000)
            isReady = true
        }
    }

}
