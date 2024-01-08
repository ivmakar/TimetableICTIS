package ru.ivmak.timetable.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.ivmak.timetable.core.db.models.DayDTO
import ru.ivmak.timetable.core.db.models.DayTimetableDTO
import ru.ivmak.timetable.core.db.models.GroupDTO
import ru.ivmak.timetable.core.db.models.LessonDTO


@Dao
interface TimetableDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDayDTOs(value: List<DayDTO>)

    @Transaction
    @Query("SELECT * FROM day WHERE `group` = :group")
    suspend fun getTimetable(group: String): List<DayTimetableDTO>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLessonDTOs(value: List<LessonDTO>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGroupDTO(value: GroupDTO)

    @Query("SELECT * FROM groups WHERE `group` = :group")
    suspend fun getGroup(group: String): GroupDTO?

    @Transaction
    suspend fun insertTimetableInTransaction(groupDTO: GroupDTO, dayDTOs: List<DayDTO>, lessonDTOs: List<LessonDTO>) {
        saveGroupDTO(groupDTO)
        saveDayDTOs(dayDTOs)
        saveLessonDTOs(lessonDTOs)
    }

}