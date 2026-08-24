package com.example.my_books.compose.importdata

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.my_books.compose.component.BaseAlertDialog
import com.example.my_books.compose.component.BaseButton
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun Import(importViewModel: ImportViewModel){
    val context = LocalContext.current
    var showDialog by remember{mutableStateOf(false)}
    var jsonString by remember { mutableStateOf<String>("") }
    // 1. 创建文件选择 launcher（支持选择任意文本文件）
    val openLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                // 2. 读取文件内容
                val content = context.contentResolver.openInputStream(it)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).readText()
                } ?: ""
                importViewModel.import(content)
            } catch (e: Exception) {
                Toast.makeText(context, "读取失败: ${e.message}", Toast.LENGTH_SHORT).show()
                jsonString = ""
            }
        }
    }
    LaunchedEffect(Unit) {
        importViewModel.importResult.collect {
                (result, message) ->
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        BaseButton(
            onClick = {
                showDialog = true
            },
            text = "导入"
        )
    }
    if (showDialog){
        BaseAlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            onConfirmRequest = {
                openLauncher.launch(arrayOf("application/json"))
                showDialog = false
            },
            title = {Text("提示")}
        ) {
            Text("导入将会覆盖相同id的数据")
        }
    }

}