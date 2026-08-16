package com.example.my_books.common

import android.annotation.SuppressLint
import com.example.my_books.data.dao.ShelvesDao
import com.example.my_books.data.entity.Book
import com.example.my_books.data.entity.Site
import com.example.my_books.data.entity.User
import kotlinx.serialization.Serializable

@Serializable
data class AllData(
    val users:List<User>,
    val books: List<Book>,
    val shelves:List<Site>
)