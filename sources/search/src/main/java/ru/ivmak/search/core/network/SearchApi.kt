package ru.ivmak.search.core.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.ivmak.search.core.network.models.ChoiceList
import ru.ivmak.search.core.network.models.Table

interface SearchApi {

    @GET("schedule-api/")
    suspend fun queryTimetables(@Query("query") query: String): ChoiceList

    @GET("schedule-api/")
    suspend fun queryTimetable(@Query("query") query: String): Table

}
