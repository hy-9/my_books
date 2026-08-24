package com.example.my_books.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.my_books.compose.component.BaseTopBar
import com.example.my_books.compose.error.Error
import com.example.my_books.compose.exportdata.Export
import com.example.my_books.compose.exportdata.ExportViewModel
import com.example.my_books.compose.importdata.Import
import com.example.my_books.compose.importdata.ImportViewModel
import com.example.my_books.compose.settings.Settings
import com.example.my_books.data.Repository


@Composable

fun MainCompose(route:String?,repository: Repository,destoryActivity:()-> Unit){
    Scaffold(
        topBar={
            BaseTopBar(route?:"未知标题",{destoryActivity()})
        }
    ){paddingValue->
        Column(modifier = Modifier.padding(paddingValue).background(Color(0xFFF8F9FA))) {
            when(route){
                Routes.SETTINGS.name->{
                    Settings()
                }
                Routes.IMPORT.name->{
                    val importViewModel = ImportViewModel(repository)
                    Import(importViewModel)
                }
                Routes.EXPORT.name->{
                    val exportViewModel = ExportViewModel(repository)
                    Export(exportViewModel)
                }
                else -> {
                    Error(route)
                }
            }
        }
    }
}