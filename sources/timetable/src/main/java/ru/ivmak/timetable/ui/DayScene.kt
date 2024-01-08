package ru.ivmak.timetable.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import ru.ivmak.timetable.core.models.DayTimetable
import ru.ivmak.timetable.core.mvi.Action
import ru.ivmak.timetable.ui.models.TableState
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DayScene(
    timetable: TableState,
    loadCallback: (Action) -> Unit
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
        is TableState.LoadedTimetable -> {
            isNeedLoad = false
            OneDayLessonsScene(timetable = timetable.table, timetable.isLoading, loadCallback)
        }
    }

    LaunchedEffect(isNeedLoad) {
        isNeedLoad = false
        if (timetable is TableState.LoadedTimetable)
            return@LaunchedEffect

        loadCallback.invoke(Action.LoadTimetable(timetable.week))
    }

}

@Composable
fun OneDayLessonsScene(
    timetable: DayTimetable,
    isLoading: Boolean,
    loadCallback: (Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = timetable.date)
            Spacer(modifier = Modifier
                .width(0.dp)
                .weight(1f))
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.width(24.dp).height(24.dp))
            } else {
                Icon(
                    modifier = Modifier.clickable { loadCallback.invoke(Action.UpdateTimetable(timetable.week)) },
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Обновить"
                )
            }
            val formatter =
                if (System.currentTimeMillis() - timetable.lastUpdated.time > 12*60*60*1000)
                    SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
                else
                    SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = formatter.format(timetable.lastUpdated)
            )
        }
        timetable.lessons.forEach {
            ListItem(
                headlineContent = { Text(text = it.time) },
                supportingContent = { Text(text = it.lesson.trim().ifEmpty { "Окно" }) },
                leadingContent = { Text(text = it.order) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DayScenePreview() {
    DayScene(TableState.LoadedTimetable(1, 0, DayTimetable(
        "134.html",
        1,
        4,
        "Птн,01  сентября",
        listOf(
            DayTimetable.Lesson("1-я", "08:00-09:35", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
            DayTimetable.Lesson("2-я", "09:50-11:25", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
            DayTimetable.Lesson("3-я", "11:55-13:30", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
            DayTimetable.Lesson("4-я", "13:45-15:20", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
            DayTimetable.Lesson("5-я", "15:50-17:25", "пр.Дисциплины ВПК Вакансия ИКТИБ +"),
            DayTimetable.Lesson("6-я", "17:40-19:15", "пр.Дисциплины ВПК Вакансия ИКТИБ +2"),
            DayTimetable.Lesson("7-я", "19:30-21:05", ""),
        ),
        Date()
    ), true)) {}
}
