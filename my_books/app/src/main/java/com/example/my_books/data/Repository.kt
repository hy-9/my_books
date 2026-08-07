package com.example.my_books.data

import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.dao.BookDao
import com.example.my_books.data.dao.CurrentUserDao
import com.example.my_books.data.dao.SiteDao
import com.example.my_books.data.dao.UserDao
import com.example.my_books.data.entity.CurrentUser
import com.example.my_books.data.entity.User
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class Repository(private val userDao: UserDao, private val currentUserDao: CurrentUserDao) {
    suspend fun queryAllUserByUserName(name: String): List<User> {
        return userDao.queryAllUsersByUserName(name)
    }

    suspend fun loginByUserNameAndPassword(
        name: String,
        pwd: String,
        termsState: Boolean
    ): SimpleResponse {
        val users: List<User> = userDao.queryAllUsersByUserName(name)
        return if (!termsState) SimpleResponse(false, "请先同意用户协议") else {
            if (name.isNullOrEmpty() || pwd.isNullOrEmpty()) return SimpleResponse(false, "用户名或密码不能为空")
            if (users.isEmpty()) return SimpleResponse(false, "用户不存在")
            val user = userDao.queryUserByUserNameAndPassword(name, pwd)
            if (user != null) {
                currentUserDao.insertCurrentUser(
                    CurrentUser(
                        user.id as Int,
                        user.user_name,
                        user.password
                    )
                )
                SimpleResponse(true, "登录成功")
            } else {
                SimpleResponse(false, "密码错误")
            }

        }
    }
    suspend fun register(
        name: String,
        pwd: String,
        termsState: Boolean
    ): SimpleResponse{
        val users: List<User> = userDao.queryAllUsersByUserName(name)
        return if (!termsState) SimpleResponse(false, "请先同意用户协议") else {
            if (name.isNullOrEmpty() || pwd.isNullOrEmpty()) return SimpleResponse(false, "用户名或密码不能为空")
            if (users.isNotEmpty()) return SimpleResponse(false, "用户已存在")
            try {
                userDao.insertUser(User( user_name = name, password = pwd))
                return loginByUserNameAndPassword(name,pwd,termsState)
            }catch (e: Exception){
                return SimpleResponse(false,"未知错误${e.message}")
            }

        }
    }
}