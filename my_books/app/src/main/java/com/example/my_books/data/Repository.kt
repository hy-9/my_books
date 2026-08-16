package com.example.my_books.data

import android.util.Log
import com.example.my_books.common.AllData
import com.example.my_books.common.SimpleResponse
import com.example.my_books.data.dao.BookDao
import com.example.my_books.data.dao.CurrentUserDao
import com.example.my_books.data.dao.ShelvesDao
import com.example.my_books.data.dao.UserDao
import com.example.my_books.data.entity.CurrentUser
import com.example.my_books.data.entity.Site
import com.example.my_books.data.entity.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Repository(
    private val userDao: UserDao,
    private val currentUserDao: CurrentUserDao,
    private val shelvesDao: ShelvesDao,
    private val bookDao: BookDao
    ) {
    private val TAG = "Repository"
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
    suspend fun addOneShelf(shelfName: String): SimpleResponse{
        return try {
            val userId:Int? = currentUserDao.queryLoginUser()?.id
            if (userId == null)return SimpleResponse(false,"用户id不存在")
            if (shelfName.isNullOrBlank())return SimpleResponse(false,"书架名不能为空")
            shelvesDao.insertShelves(Site(null,userId,shelfName))
            SimpleResponse(true,"添加成功")
        }catch (e: Exception){
            SimpleResponse(false,"错误${e.message}")
        }

    }
    suspend fun deleteOneShelf(id:Int?): SimpleResponse{
        return try{
            id?.let {
                shelvesDao.deleteOneShelfById(id)
            }?: SimpleResponse(false,"书架id不存在")
            SimpleResponse(false, "删除成功")
        }catch (e: Exception){
            SimpleResponse(false,"错误${e.message}")
        }
    }
    suspend fun updateOneShelfById(id:Int?,shelfName:String): SimpleResponse{
        return try {
            id?.let {
                if (shelfName.isNullOrBlank())SimpleResponse(false,"书架名不能为空")
                shelvesDao.updateOneShelfById(it,shelfName)
            }?: SimpleResponse(false,"书架id不存在")
            SimpleResponse(false, "修改成功")
        }catch (e: Exception){
            SimpleResponse(false,"错误${e.message}")
        }
    }
    suspend fun deleteCurrentUser(): SimpleResponse{
        return try {
            currentUserDao.deleteCurrentUser()
            SimpleResponse(true,"已退出登录")
        }catch (e: Exception){
            SimpleResponse(false,"错误${e.message}")
        }
    }
    suspend fun exportAllTablesData(): SimpleResponse{
        return try {
            val allUser = userDao.getAllUsers()
            val allBooks = bookDao.getAllBooks()
            val allShelves = shelvesDao.getAllShelves()
            val allData = AllData(allUser,allBooks,allShelves)
            val jsonValueString = Json.encodeToString(allData)
            SimpleResponse(true,"$jsonValueString")
        }catch (e: Exception){
            SimpleResponse(false,"错误${e.message}")
        }
    }
    suspend fun importAllTablesData(jsonString: String): SimpleResponse{
        return try {
            val allData = Json.decodeFromString<AllData>(jsonString)
            allData.users.forEach {
                userDao.insertUser(it)
            }
            allData.books.forEach {
                bookDao.insertBook(it)
            }
            allData.shelves.forEach {
                shelvesDao.insertShelves(it)
            }
            currentUserDao.deleteCurrentUser()
            SimpleResponse(true,"导入成功")
        }catch (e: Exception){
            SimpleResponse(false,"错误${e.message}")
        }
    }
}