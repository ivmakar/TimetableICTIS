package ru.ivmak.timetable.core.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.timetable.core.models.TimetableResponse
import javax.inject.Inject

class TimetableRepo @Inject constructor(
    private val dbDataSource: DatabaseDataSource,
    private val networkDataSource: NetworkDataSource
) {

    suspend fun getTimetable(group: String): Flow<DataResponse<TimetableResponse>> {
        val response = dbDataSource.getTimetable(group)
            ?: return networkDataSource.getTimetable(group)

        return flow {
            emit(DataResponse.Success(response))
        }
    }

    suspend fun getTimetable(group: String, week: Int): Flow<Pair<Int, DataResponse<TimetableResponse>>> {
        val response = dbDataSource.getTimetable(group)
            ?: return updateTimetable(group, week)

        if (response.days.none { it.week == week })
            return updateTimetable(group, week)

        return flow {
            emit(week to DataResponse.Success(response))
        }
    }

    suspend fun updateTimetable(group: String, week: Int): Flow<Pair<Int, DataResponse<TimetableResponse>>> {
        return networkDataSource.getTimetable(group, week)
            .onEach {
                if (it is DataResponse.Success) {
                    dbDataSource.saveTimetable(it.data)
                }
            }
            .map { week to it }
    }

}
