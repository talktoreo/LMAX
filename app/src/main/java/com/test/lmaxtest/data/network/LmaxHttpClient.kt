package com.test.lmaxtest.data.network

import android.util.Log
import com.test.lmaxtest.data.network.JSONObjectSerializer
import com.test.lmaxtest.utils.Constants
import io.ktor.client.*
import io.ktor.client.call.receive
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.cio.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import org.json.JSONObject
import javax.inject.Inject
import kotlin.reflect.KClass

class LmaxHttpClient @Inject constructor() {

    private val json = kotlinx.serialization.json.Json {
        encodeDefaults = true
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(JSONObject::class, JSONObjectSerializer)
        }
    }

    fun getHttpClient() = HttpClient(Android){
        install(JsonFeature){
            serializer = KotlinxSerializer(json)
        }

        install(Logging){
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i(TAG_KTOR_LOGGER, message)
                }
            }
        }

        install(ResponseObserver){
            onResponse { response ->
                Log.i(TAG_HTTP_STATUS_LOGGER, "${response.status.value}")
                when (response.status.value) {
                    HttpStatusCode.Unauthorized.value -> {
                        // Handle unauthorized response
                        Log.e("ERROR_TAG1", "addressList: ${response.status.value}" )
                        throw ResponseException(response,"Unauthorized")
                    }
                    else -> {
                        // Throw an HttpResponseException for any other status codes

                    }
                }
            }
        }

        install(DefaultRequest) {
//            contentType(ContentType.Application.Json)
//            accept(ContentType.Application.Json)
        }

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }

    companion object {
        private const val TIME_OUT = 10000
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
        private const val TAG_HTTP_STATUS_LOGGER = "http_status:"
    }

}