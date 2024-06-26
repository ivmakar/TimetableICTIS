package ru.ivmak.raspisanie_iktib.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import ru.ivmak.raspisanie_iktib.ui.theme.AppTheme
import ru.ivmak.timetable.core.mvi.Action
import ru.ivmak.timetable.ui.TimetableScene
import ru.ivmak.timetable.ui.TimetableViewModel
import ru.ivmak.timetable.ui.datepicker.SelectDatePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScene(
    navController: NavController
) {

    val timetableViewModel: TimetableViewModel = hiltViewModel()
    val state by timetableViewModel.observableState.collectAsStateWithLifecycle()

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Поиск"
                        )
                        Text(
                            text = state.name,
                            modifier = Modifier
                                .clickable {
                                    val options = NavOptions.Builder()
                                        .setLaunchSingleTop(true)
                                        .setPopUpTo("${Screen.Timetable.route}/{group}", true)
                                        .build()
                                    navController.navigate(Screen.Search.route, options)
                                },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Настройки"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Календарь"
                        )
                    }
                },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            TimetableScene(timetableViewModel)
        }
    }

    if (showDatePicker) {
        SelectDatePickerDialog(
            onDateSelected = { timetableViewModel.dispatch(Action.SelectDate(it)) },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScenePreview() {
    AppTheme {
        MainScene(rememberNavController())
    }
}
