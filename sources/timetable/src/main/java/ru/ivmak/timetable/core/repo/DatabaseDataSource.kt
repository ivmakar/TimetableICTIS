package ru.ivmak.timetable.core.repo

import ru.ivmak.timetable.core.models.TimetableResponse
import javax.inject.Inject

class DatabaseDataSource @Inject constructor(

) {

    suspend fun getTimetable(group: String): TimetableResponse {
        // TODO
        return TimetableResponse(
            "",
            "",
            "",
                listOf(),
            listOf()
        )
    }

    suspend fun saveTimetable(timetable: TimetableResponse) {
        // TODO
    }

}
