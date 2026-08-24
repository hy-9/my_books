package com.example.my_books.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.my_books.data.entity.Site

@Dao
interface ShelvesDao {
    @Query("SELECT * FROM site WHERE user_id = :id")
    suspend fun queryShelves(id:Int): List<Site>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShelves(shelf: Site)
    @Query("DELETE FROM site WHERE id = :id")
    suspend fun deleteOneShelfById(id:Int)
    @Query("UPDATE site SET site = :name WHERE id = :id")
    suspend fun updateOneShelfById(id:Int,name:String)
    @Query("SELECT * FROM site")
    suspend fun getAllShelves(): List<Site>
}