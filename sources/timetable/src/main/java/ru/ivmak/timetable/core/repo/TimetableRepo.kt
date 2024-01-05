package ru.ivmak.timetable.core.repo

import kotlinx.coroutines.flow.Flow
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.timetable.core.models.Response
import javax.inject.Inject

class TimetableRepo @Inject constructor(
    private val dbDataSource: DatabaseDataSource,
    private val networkDataSource: NetworkDataSource
) {

    suspend fun getTimetable(group: String): Flow<DataResponse<Response>> {
        return networkDataSource.getTimetable(group)
    }

    suspend fun getTimetable(group: String, week: Int): Flow<Pair<Int, DataResponse<Response>>> {
        return networkDataSource.getTimetable(group, week)
    }

}
