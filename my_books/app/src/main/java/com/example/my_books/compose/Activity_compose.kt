package com.example.my_books.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


@SuppressLint("RestrictedApi")
class Activity_compose: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val route = intent.getStringExtra("route_name")
        setContent{
            MainCompose(route,{finish()})
        }
    }
}