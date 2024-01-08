package ru.ivmak.timetable.core.repo

import ru.ivmak.timetable.core.db.dao.TimetableDAO
import ru.ivmak.timetable.core.db.models.GroupDTO
import ru.ivmak.timetable.core.models.TimetableResponse
import javax.inject.Inject

class DatabaseDataSource @Inject constructor(
    private val timetableDAO: TimetableDAO
) {

    suspend fun getTimetable(group: String): TimetableResponse? {
        val groupDTO = timetableDAO.getGroup(group)?: return null
        val days = timetableDAO.getTimetable(group)
            .mapNotNull { it.toDayTimetable() }

        if (days.isEmpty())
            return null

        return TimetableResponse(
            group = group,
            type = groupDTO.type,
            name = groupDTO.name,
            weeks = groupDTO.weeks,
            days = days
        )
    }

    suspend fun saveTimetable(timetable: TimetableResponse) {
        val groupDTO = GroupDTO(
            group = timetable.group,
            type = timetable.type,
            name = timetable.name,
            weeks = timetable.weeks
        )

        val dayDTOsPairs = timetable.days
            .map {
                it.toDayDTO() to it.lessons
            }
            .map { dtoListPair ->
                dtoListPair.first to dtoListPair.second.map { it.toLessonDTO(dtoListPair.first.id) }
            }

        val dayDTOs = dayDTOsPairs.map { it.first }
        val lessonDTOs = dayDTOsPairs.flatMap { it.second }

        timetableDAO.insertTimetableInTransaction(groupDTO, dayDTOs, lessonDTOs)
    }

}
