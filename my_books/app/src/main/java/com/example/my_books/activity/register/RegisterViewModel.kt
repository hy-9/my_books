package com.example.my_books.activity.register

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.Repository
import com.example.my_books.data.entity.User
import com.example.my_books.sql.sql
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel(){
    private val _isLoginState = MutableStateFlow<Boolean>(true)
    val isLoginState: StateFlow<Boolean> = _isLoginState
    val isLoginLiveData: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username
    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password
    private val _isAcceptTerms = MutableStateFlow<Boolean>(false)
    val isAcceptTerms: StateFlow<Boolean> = _isAcceptTerms
    val isAcceptTermsLiveData = _isAcceptTerms.asLiveData()
    private val _loginResult = MutableSharedFlow<SimpleResponse>(replay = 0)
    val loginResult: SharedFlow<SimpleResponse> = _loginResult
    val loginResultLiveData = _loginResult.asLiveData()
    fun changeLoginState(){
        viewModelScope.launch {
            _isLoginState.value = !_isLoginState.value
            isLoginLiveData.value = _isLoginState.value
        }
    }
    fun onUsernameChange(value:String){
        viewModelScope.launch {
            _username.value = value
        }
    }
    fun onUserPasswordChange(value:String){
        viewModelScope.launch {
            _password.value = value
        }
    }
    fun onCheckTerms(){
        viewModelScope.launch {
            _isAcceptTerms.value = !_isAcceptTerms.value
        }
    }
    fun login(){
        viewModelScope.launch {
            val result = repository.loginByUserNameAndPassword(_username.value,_password.value,_isAcceptTerms.value)
            _loginResult.emit(result)
        }
    }
}