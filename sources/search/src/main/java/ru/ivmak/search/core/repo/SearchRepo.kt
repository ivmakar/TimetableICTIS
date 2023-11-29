package ru.ivmak.search.core.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.search.core.network.SearchApi
import ru.ivmak.search.core.network.models.ChoiceList
import javax.inject.Inject

class SearchRepo @Inject constructor(private val api: SearchApi) {

    suspend fun queryTimetables(query: String): Flow<DataResponse<List<ChoiceList.Choice>>> {
        return channelFlow {
            if (query.isEmpty()) {
                send(DataResponse.Success(listOf()))
                return@channelFlow
            }
            send(DataResponse.Loading())
            withContext(Dispatchers.IO) {
                val response = try {
                    DataResponse.Success(api.queryTimetables(query).choices?: listOf())
                } catch (e: Exception) {
                    DataResponse.Error(e)
                }
                send(response)
            }
        }
    }

}
