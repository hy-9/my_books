package com.example.my_books.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int? = 0,
    val user_name: String? = "",
    val password: String? = ""
)
