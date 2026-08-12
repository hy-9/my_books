package com.example.my_books.activity.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import com.example.my_books.activity.compose.component.MainTopBar
import com.example.my_books.activity.compose.error.Error
import com.example.my_books.activity.compose.settings.Settings


@Composable

fun MainCompose(route:String?,destoryActivity:()-> Unit){
    Scaffold(
        topBar={
            MainTopBar(route?:"未知标题",{destoryActivity()})
        }
    ){paddingValue->
        Column(modifier = Modifier.padding(paddingValue).background(Color(0xFFF8F9FA))) {
            when(route){
                Routes.SETTINGS.name->{
                    Settings()
                }
                else -> {
                    Error(route)
                }
            }
        }
    }
}