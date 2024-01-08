package ru.ivmak.raspisanie_iktib.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ivmak.raspisanie_iktib.core.db.AppDatabase
import ru.ivmak.timetable.core.db.dao.TimetableDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule{

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(applicationContext, AppDatabase::class.java,"AppDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTimetableDAO(database: AppDatabase): TimetableDAO {
        return database.getTimetableDAO()
    }
}
