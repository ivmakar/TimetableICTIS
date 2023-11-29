package ru.ivmak.search.core.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.ivmak.core.utils.DataResponse
import ru.ivmak.search.core.network.SearchApi
import ru.ivmak.search.core.network.models.ChoiceList
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class SearchRepo @Inject constructor(private val api: SearchApi) {

    private val random = Random(5)

    suspend fun queryTimetables(query: String): Flow<DataResponse<List<ChoiceList.Choice>>> {
        return channelFlow {
            if (query.isEmpty()) {
                send(DataResponse.Success(listOf()))
                return@channelFlow
            }

            if (random.nextBoolean()) {
                send(DataResponse.Loading())
                withContext(Dispatchers.IO) {
                    val response = try {
                        DataResponse.Success(api.queryTimetables(query).choices ?: listOf())
                    } catch (e: Exception) {
                        DataResponse.Error(e)
                    }
                    send(response)
                }
            } else {
                send(DataResponse.Loading())
                delay(2000)
                send(DataResponse.Error(IOException()))
            }
        }
    }

}
