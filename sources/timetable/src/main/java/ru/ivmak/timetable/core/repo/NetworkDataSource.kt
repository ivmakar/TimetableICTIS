package ru.ivmak.timetable.core.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.timetable.core.models.Response
import ru.ivmak.timetable.core.models.Source
import ru.ivmak.timetable.core.network.TimetableApi
import ru.ivmak.timetable.core.network.models.toTimetable
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val api: TimetableApi
) {

    suspend fun getTimetable(group: String): Flow<DataResponse<Response>> {
        return channelFlow {
            send(DataResponse.Loading())
            withContext(Dispatchers.IO) {
                val response = try {
                    val res = api.queryTimetable(group)
                    DataResponse.Success(Response(res.table.toTimetable(), res.weeks, Source.NETWORK))
                } catch (e: Exception) {
                    DataResponse.Error(e)
                }
                send(response)
            }
        }
    }

    suspend fun getTimetable(group: String, week: Int): Flow<Pair<Int, DataResponse<Response>>> {
        return channelFlow {
            send(week to DataResponse.Loading())
            withContext(Dispatchers.IO) {
                val response = try {
                    val res = api.queryTimetable(group, week)
                    DataResponse.Success(Response(res.table.toTimetable(), res.weeks, Source.NETWORK))
                } catch (e: Exception) {
                    DataResponse.Error(e)
                }
                send(week to response)
            }
        }
    }

}
