package com.example.my_books.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_use")
data class CurrentUser(
    @PrimaryKey
    val id: Int, // 这里的id不是自增，是关联User的id
    val user_name: String? = "",
    val password: String? = ""
)