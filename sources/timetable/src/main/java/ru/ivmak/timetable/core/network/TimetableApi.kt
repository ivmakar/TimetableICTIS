package ru.ivmak.timetable.core.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.ivmak.timetable.core.network.models.ResponseDTO

interface TimetableApi {

    @GET("schedule-api/")
    suspend fun queryTimetable(@Query("group") group: String): ResponseDTO

    @GET("schedule-api/")
    suspend fun queryTimetable(@Query("group") group: String, @Query("week") week: Int): ResponseDTO

}
