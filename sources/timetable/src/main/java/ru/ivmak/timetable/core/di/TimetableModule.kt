package ru.ivmak.timetable.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import ru.ivmak.timetable.core.network.TimetableApi

@Module
@InstallIn(ViewModelComponent::class)
class TimetableModule {

    @Provides
    fun provideAuthService(retrofit : Retrofit): TimetableApi = retrofit.create(TimetableApi::class.java)

}
