package com.test.lmaxtest.views.mainPage

import com.test.lmaxtest.data.Resource
import org.json.JSONObject

interface MainRepository {
    suspend fun getData(): Resource<String>
}