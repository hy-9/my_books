package com.example.my_books.compose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.my_books.compose.Routes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseTopBar(name:String,navBack:()->Unit){
    val currentRoute = Routes.getAllRoutes().find {
        it.name == name
    }
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).background(Color.Transparent).background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.height(12.dp)) { }
        Box(modifier = Modifier.fillMaxWidth().height(32.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(32.dp), contentAlignment = Alignment.Center){
                Column(modifier = Modifier.height(32.dp)) {
                    Text(modifier = Modifier, text = currentRoute?.description?:"未知标题")
                }
            }
            Icon(Icons.Default.ArrowBack,"back", modifier = Modifier.combinedClickable(onClick = {
                navBack()
            }))
        }
    }
}