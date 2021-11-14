package com.example.foodvillage2205.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.PrimaryColor
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun Permission(
    permission: String = Manifest.permission.CAMERA,
    rationale: String = "This permission is important for this app. Please grant the permission.",
    permissionNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val permissionState = rememberPermissionState(permission)

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                text = rationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        permissionNotAvailableContent = permissionNotAvailableContent,
        content = content
    )
}

@ExperimentalPermissionsApi
@Composable
fun PermissionForLocation(
    permission: String = Manifest.permission.ACCESS_FINE_LOCATION,
    rationale: String = "Greetings\uD83D\uDC4B !To give you a better services, please check the permission for the location.",
    content: @Composable () -> Unit = { },
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission)

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                text = rationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        permissionNotAvailableContent = {
            Column {
                Text("Thank you")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }) {
                    Text("Open Settings")
                }
            }
        },
        content = content
    )
}

@Composable
private fun Rationale(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(
                fontFamily = RobotoSlab,
                text = "Permission request",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
            )
        },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(0.3f),
                onClick = onRequestPermission
                ) {
                Text("OK",
                    fontSize = 24.sp,
                    color = White,
                    fontWeight = FontWeight.W500
                )
            }
        },
        backgroundColor = SecondaryColor,
        contentColor = Color.White
    )
}