package com.example.my_books.fragment.myFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MyFragmentViewModel(private val repository: Repository): ViewModel() {
    private val _myFragmentResult = MutableSharedFlow<SimpleResponse>(replay = 0)
    val myFragmentResultResult: SharedFlow<SimpleResponse> = _myFragmentResult
    val myFragmentResultLiveData = _myFragmentResult.asLiveData()
    fun logout(){
        viewModelScope.launch {
            val result = repository.deleteCurrentUser()
            _myFragmentResult.emit(result)
        }
    }
}