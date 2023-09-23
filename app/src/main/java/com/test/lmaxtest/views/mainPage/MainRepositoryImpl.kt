package com.test.lmaxtest.views.mainPage

import com.test.lmaxtest.data.Resource
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.Parameters
import org.json.JSONObject
import javax.inject.Inject

private const val DATA_URL = "https://md.lmaxglobal.io/fixprof/instruments/prices"

class MainRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : MainRepository {
    override suspend fun getData(): Resource<String> {
        return try {
            Resource.Success<String>(
                httpClient.get {
                    url(DATA_URL)
                }

            )
        } catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}