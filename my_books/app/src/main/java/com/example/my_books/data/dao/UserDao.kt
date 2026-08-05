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
    @Insert
    suspend fun insertUser(user: User): Long
    // 登录查询方法（原 queryAllUsersByUserName）
    @Query("SELECT * FROM user WHERE user_name = :account")
    suspend fun queryAllUsersByUserName(account: String): List<User>
    // ==================== CurrentUser 表操作 (当前登录用户) ====================
    // 查询当前登录用户（原 queryLoginUser）
    // 返回可为空，业务层判断 null 即可知道是否有登录记录
    @Query("SELECT * FROM user_use LIMIT 1")
    suspend fun queryLoginUser(): CurrentUser?
    // 插入/更新当前登录用户（原 gxbj 方法的插入部分）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUser(currentUser: CurrentUser)
    // 删除本机用户数据（原 scbj 方法）
    @Query("DELETE FROM user_use")
    suspend fun deleteCurrentUser()
}