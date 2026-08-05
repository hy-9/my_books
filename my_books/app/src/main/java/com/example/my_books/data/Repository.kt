package com.example.my_books.data

import com.example.my_books.data.dao.BookDao
import com.example.my_books.data.dao.SiteDao
import com.example.my_books.data.dao.UserDao
import com.example.my_books.data.entity.User
import kotlinx.coroutines.flow.Flow

class Repository(private val userDao: UserDao) {
    suspend fun queryAllUserByUserName(name:String): List<User>{
        return userDao.queryAllUsersByUserName(name)
    }
}