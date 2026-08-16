package com.example.my_books.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BaseAlertDialog(onDismissRequest:()-> Unit, onConfirmRequest:()-> Unit, title:@Composable ()->Unit, content:@Composable ()->Unit){
    AlertDialog(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(12.dp),
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            title()
        },
        text = {
            content()
        },
        buttons = {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row() { }
                Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BaseButton({
                        onDismissRequest()
                    },"取消",Color.Gray)
                    BaseButton({
                        onConfirmRequest()
                    },"确定")
                }
            }
        }
    )
}