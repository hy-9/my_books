package com.example.my_books.activity.shelves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.Repository
import com.example.my_books.data.entity.Site
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShelvesViewModel(private val repository: Repository): ViewModel() {
    private val _inputShelfName = MutableStateFlow<String>("")
    val inputShelfName = _inputShelfName.asStateFlow()
    val inputShelfNameLiveData = _inputShelfName.asLiveData()
    private val _selectedShelfId = MutableStateFlow<Int?>(null)
    val selectedShelfId = _selectedShelfId.asStateFlow()
    val selectedShelfIdLiveData = _selectedShelfId.asLiveData()
    private val _allShelves = MutableStateFlow<List<Site>>(emptyList())
    val allShelves = _allShelves.asStateFlow()
    val allShelvesLiveData = _allShelves.asLiveData()
    private val _shelvesResult = MutableSharedFlow<SimpleResponse>(replay = 0)
    val shelvesResult: SharedFlow<SimpleResponse> = _shelvesResult
    val shelvesResultLiveData = _shelvesResult.asLiveData()
    fun updateInputShelfName(text: String){
            _inputShelfName.value = text
    }
    fun addOneShelf(){
        viewModelScope.launch {
            val result = repository.addOneShelf(_inputShelfName.value)
            _shelvesResult.emit(result)
        }
    }
    fun deleteOneShelf(){
        viewModelScope.launch {
            val result = repository.deleteOneShelf(_selectedShelfId.value)
            _shelvesResult.emit(result)
        }
    }
    fun updateOneShelfById(){
        viewModelScope.launch {
            val result = repository.updateOneShelfById(_selectedShelfId.value,_inputShelfName.value)
            _shelvesResult.emit(result)
        }
    }

    fun updateSelectedShelfId(id: Int) {
        _selectedShelfId.value = id
    }
}