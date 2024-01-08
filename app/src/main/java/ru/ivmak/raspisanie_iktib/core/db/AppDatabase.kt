package ru.ivmak.raspisanie_iktib.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ivmak.core.utils.DateTypeConverter
import ru.ivmak.core.utils.ListIntTypeConverter
import ru.ivmak.timetable.core.db.dao.TimetableDAO
import ru.ivmak.timetable.core.db.models.DayDTO
import ru.ivmak.timetable.core.db.models.GroupDTO
import ru.ivmak.timetable.core.db.models.LessonDTO
import javax.inject.Singleton

@Singleton
@Database(entities = [
        LessonDTO::class,
        DayDTO::class,
        GroupDTO::class
    ],
    autoMigrations = [],
    version = AppDatabase.VERSION, exportSchema = true)
@TypeConverters(DateTypeConverter::class, ListIntTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val VERSION: Int = 1
    }

    abstract fun getTimetableDAO(): TimetableDAO
}
