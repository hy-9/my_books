package com.example.my_books.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.my_books.data.Repository
import com.example.my_books.data.database.AppDatabase.Companion.getInstance


@SuppressLint("RestrictedApi")
class Activity_compose: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val route = intent.getStringExtra("route_name")
        val appDatabase = getInstance(this)
        val repository = Repository(
            appDatabase.userDao(),
            appDatabase.currentUserDao(),
            appDatabase.shelvesDao(),
            appDatabase.bookDao()
        )
        setContent{
            MainCompose(route,repository){
                finish()
            }
        }
    }
}