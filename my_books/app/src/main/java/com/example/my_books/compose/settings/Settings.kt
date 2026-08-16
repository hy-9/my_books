package com.example.my_books.compose.settings

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.my_books.compose.Activity_compose
import com.example.my_books.compose.Routes
import com.example.my_books.compose.component.BaseAlertDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Settings(){
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item(content = {})
        item(content = {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().height(42.dp).combinedClickable(onClick = {
                        val intent = Intent(context, Activity_compose::class.java).apply {
                            putExtra("route_name", Routes.IMPORT.name)
                        }
                        context.startActivity(intent)
                    }), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("导入数据")
                        Text(">", color = Color.Gray)
                    }
                    Divider()
                    Row(modifier = Modifier.fillMaxWidth().height(42.dp).combinedClickable(onClick = {
                        val intent = Intent(context, Activity_compose::class.java).apply {
                            putExtra("route_name", Routes.EXPORT.name)
                        }
                        context.startActivity(intent)
                    }), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("导出数据")
                        Text(">", color = Color.Gray)
                    }
                }

            }
        })
    }
//    BaseAlertDialog(
//        onDismissRequest = {},
//        onConfirmRequest = {},
//        title = {Text("提示")}
//    ) {
//        Text("内容")
//    }
}