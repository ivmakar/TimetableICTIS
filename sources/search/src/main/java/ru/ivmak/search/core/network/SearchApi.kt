package ru.ivmak.search.core.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.ivmak.search.core.network.models.ChoiceList

interface SearchApi {

    @GET("schedule-api/")
    suspend fun queryTimetables(@Query("query") query: String): ChoiceList

}
