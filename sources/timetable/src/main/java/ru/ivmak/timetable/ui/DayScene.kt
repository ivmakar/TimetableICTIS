package ru.ivmak.timetable.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ivmak.timetable.ui.models.TableState
import ru.ivmak.timetable.ui.models.UiDayTimetable

@Composable
fun DayScene(
    timetable: TableState,
    loadCallback: (Int) -> Unit
) {

    var isNeedLoad by remember { mutableStateOf(false) }

    when(timetable) {
        is TableState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error")
            }
        }
        is TableState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is TableState.LoadedFromDb -> {
            OneDayLessonsScene(timetable = timetable.table)
        }
        is TableState.LoadedFromNetwork -> {
            isNeedLoad = false
            OneDayLessonsScene(timetable = timetable.table)
        }
    }

    LaunchedEffect(isNeedLoad) {
        isNeedLoad = false
        if (timetable is TableState.LoadedFromNetwork)
            return@LaunchedEffect

        loadCallback.invoke(timetable.week)
    }

}

@Composable
fun OneDayLessonsScene(
    timetable: UiDayTimetable
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = timetable.date, modifier = Modifier.align(Alignment.CenterHorizontally))
        timetable.lessons.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = it.order)

                Column(
                    modifier = Modifier
                        .width(0.dp)
                        .weight(1f)
                ) {
                    Text(text = it.time)
                    Text(text = it.lesson)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayScenePreview() {
    DayScene(TableState.Loading(1, 0)) {}
}
