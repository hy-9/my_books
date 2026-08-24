package com.example.my_books.compose.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseButton(onClick:()->Unit,text:String,color: Color = Color(0xFF42A5F5)){
    Button(
        shape = RoundedCornerShape(18.dp),
        onClick = {
            onClick()
        }, colors = ButtonDefaults.buttonColors(color)) {
        Text(text, color = Color.White)
    }
}