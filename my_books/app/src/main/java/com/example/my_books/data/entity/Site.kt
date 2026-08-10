package com.example.my_books.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity(tableName = "site")
data class Site(
    @PrimaryKey
    var id: Int? = null,
    var user_id: Int? = 0,
    var site: String? = "",
    // 非数据库字段：用于存储该书架下的书籍数量
    @Ignore
    var user_name: Int = 0,
    // 非数据库字段：用于标记是否被选中
    @Ignore
    var p: Boolean = false
)
