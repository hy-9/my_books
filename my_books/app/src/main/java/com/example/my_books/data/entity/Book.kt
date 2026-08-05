package com.example.my_books.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class Book(
    @PrimaryKey
    var id: Int? = 0,
    var user_id: Int? = 0,
    var book_name: String? = "",
    var author: String? = "",
    var nationality: String? = "",
    var king: Int? = 0,
    var comment: String? = "",
    var site: Int? = 0,
    var date: String? = "",
    var likes: Int? = 0,
    // 非数据库字段：用于存储匹配到的书架名称
    @Ignore
    var site_name: String? = ""
)
