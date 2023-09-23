package com.test.lmaxtest.data.di

import com.test.lmaxtest.data.network.LmaxHttpClient
import com.test.lmaxtest.views.mainPage.MainRepository
import com.test.lmaxtest.views.mainPage.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.util.*

@InstallIn(SingletonComponent:: class)
@Module
class AppModule {

    @Provides
    fun getHttpClient(httpClient: LmaxHttpClient): HttpClient = httpClient.getHttpClient()

    @Provides
    fun getMainRepository(impl: MainRepositoryImpl): MainRepository = impl

 }