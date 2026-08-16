package com.example.my_books.compose.exportdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExportViewModel(private val repository: Repository): ViewModel() {
    private val _exportResult = MutableSharedFlow<SimpleResponse>(replay = 0)
    private val _allTables = MutableStateFlow(listOf("用户","登录用户","图书","书架"))
    private val _selectedTables = MutableStateFlow(listOf<String>())
    val exportResult = _exportResult.asSharedFlow()
    val allTables = _allTables.asStateFlow()
    val selectedTables = _selectedTables.asStateFlow()
    fun export(){
        viewModelScope.launch {
            val result = repository.exportAllTablesData()
            _exportResult.emit(result)
        }
    }
}