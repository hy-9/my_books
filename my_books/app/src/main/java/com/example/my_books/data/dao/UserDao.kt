package com.example.my_books.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_books.data.entity.CurrentUser
import com.example.my_books.data.entity.User

@Dao
interface UserDao {
    // ==================== User 表操作 (所有用户) ====================
    // 注册用户，返回插入的 id（原 zcyh 方法的插入部分）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long
    // 登录查询方法（原 queryAllUsersByUserName）
    @Query("SELECT * FROM user WHERE user_name = :account")
    suspend fun queryAllUsersByUserName(account: String): List<User>
    @Query("SELECT * FROM user WHERE user_name = :userName AND password = :password LIMIT 1")
    suspend fun queryUserByUserNameAndPassword(userName: String, password: String): User?
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}