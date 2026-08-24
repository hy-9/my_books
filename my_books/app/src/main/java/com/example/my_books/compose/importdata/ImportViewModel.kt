package com.example.my_books.compose.importdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImportViewModel(val repository: Repository): ViewModel() {
    private val _importResult = MutableSharedFlow<SimpleResponse>(replay = 0)
    val importResult = _importResult.asSharedFlow()
    fun import(jsonString:String){
        viewModelScope.launch {
            val result = repository.importAllTablesData(jsonString)
            _importResult.emit(result)
        }
    }
}