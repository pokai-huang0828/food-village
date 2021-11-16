/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This is a file to format timestamp to readable string.
 */

package com.example.foodvillage2205.util

import androidx.compose.runtime.Composable
import com.example.foodvillage2205.model.entities.Post
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

@Composable
fun TimestampToFormatedString(timestamp: Timestamp?): String {
    timestamp?.let {
        val pattern = "yyyy-MM-dd  HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(timestamp?.toDate())
        return date
    }
    return ""
}