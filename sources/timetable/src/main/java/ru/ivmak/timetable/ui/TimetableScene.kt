package ru.ivmak.timetable.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.ivmak.timetable.core.mvi.State

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimetableScene(
    navController: NavController,
    timetableViewModel: TimetableViewModel = hiltViewModel()
) {

    val state: State by timetableViewModel.observableState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        pageCount = {
            state.dayTables.size
        }
    )

    if (state.isLoading || state.name.isEmpty() || state.type.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Error")
            }
        }

    } else {
        Column {
            HorizontalPager(state = pagerState) { page ->
                DayScene(timetable = state.dayTables[page]) {
                    timetableViewModel.dispatch(it)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TimetableScenePreview() {
    TimetableScene(rememberNavController())
}
