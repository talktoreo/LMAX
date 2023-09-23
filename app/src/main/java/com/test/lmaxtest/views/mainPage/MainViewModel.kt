package com.test.lmaxtest.views.mainPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.lmaxtest.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _dataResponse = MutableStateFlow<Resource<String>?>(null)
    val dataResponse: StateFlow<Resource<String>?> = _dataResponse
    var isAscendingSort  = false

    init {
        viewModelScope.launch {
            _dataResponse?.value = Resource.Loading
            _dataResponse?.value = repository.getData()
        }
    }
}