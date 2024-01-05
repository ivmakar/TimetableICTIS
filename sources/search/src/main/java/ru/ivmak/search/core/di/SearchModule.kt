package ru.ivmak.search.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import ru.ivmak.search.core.network.SearchApi

@Module
@InstallIn(ViewModelComponent::class)
class SearchModule {

    @Provides
    fun provideAuthService(retrofit : Retrofit): SearchApi = retrofit.create(SearchApi::class.java)

}
