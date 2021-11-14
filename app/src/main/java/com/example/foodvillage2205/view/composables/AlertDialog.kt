package com.example.foodvillage2205.view.composables

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White

@Composable
fun AlertDialogForProfile() {
    val openDialog = remember { mutableStateOf(true) }
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Text(
                fontFamily = RobotoSlab,
                text = "Warning ⚠️",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp)
        },
        text = {
            Text("Your location are not found, please try again or use postal code.",
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
            )
        },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .fillMaxWidth(0.3f)
                    .background(White),
                onClick = {
                    openDialog.value = false
                }) {
                Text("OK",
                    fontSize = 18.sp,
                    color = PrimaryColor,
                )
            }
        },
        backgroundColor = SecondaryColor,
        contentColor = Color.White
    )
}