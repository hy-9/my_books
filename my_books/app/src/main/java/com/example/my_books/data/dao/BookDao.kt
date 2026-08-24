package com.example.my_books.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_books.data.entity.Book
import com.example.my_books.data.entity.User

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    suspend fun getAllBooks():List<Book>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long
}