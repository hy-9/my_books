package com.example.my_books.activity.register

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.my_books.data.Repository
import com.example.my_books.data.entity.User
import com.example.my_books.sql.sql
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel(){
    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUser: StateFlow<List<User>> = _allUsers
    val allUserLiveData: MutableLiveData<List<User>> = MutableLiveData()
    fun queryAllUsers(name:String){
        viewModelScope.launch {
            _allUsers.value = repository.queryAllUserByUserName(name)
            allUserLiveData.value = _allUsers.value
        }
    }
    fun clearAllUsersState(){
        viewModelScope.launch {
            _allUsers.value = emptyList()
        }
    }
}