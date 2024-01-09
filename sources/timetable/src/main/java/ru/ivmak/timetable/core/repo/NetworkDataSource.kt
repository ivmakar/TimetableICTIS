package ru.ivmak.timetable.core.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.timetable.core.models.TimetableResponse
import ru.ivmak.timetable.core.network.TimetableApi
import ru.ivmak.timetable.core.network.models.toTimetable
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val api: TimetableApi
) {

    suspend fun getTimetable(group: String, week: Int? = null): Flow<DataResponse<TimetableResponse>> {
        return channelFlow {
            send(DataResponse.Loading())
            withContext(Dispatchers.IO) {
                val response = try {
                    val res = if (week != null)
                        api.queryTimetable(group, week)
                    else
                        api.queryTimetable(group)
                    val timetable = res.table.toTimetable()
                    DataResponse.Success(TimetableResponse(timetable.group, timetable.type, timetable.name, res.weeks, timetable.parseTimeTable()))
                } catch (e: Exception) {
                    DataResponse.Error(e)
                }
                send(response)
            }
        }
    }

}
