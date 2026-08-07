package com.example.my_books.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_books.data.entity.CurrentUser

@Dao
interface CurrentUserDao {
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