/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This file contains functions for obtain permissions from app users
 */

package com.example.foodvillage2205.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodvillage2205.view.screens.RobotoSlab
import com.example.foodvillage2205.view.theme.SecondaryColor
import com.example.foodvillage2205.view.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

/**
 * A generic function for obtaining permissions from users.
 * @param [permission] [permission type]
 * @param [rationale] [reason for asking the permission]
 * @param [permissionNotAvailableContent] [composable to display when permission is not obtained]
 * @param [content] [composable to display when permission is obtained]
 */
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

/**
 * Obtaining fine location permission from users.
 * @param [permission] [permission type]
 * @param [rationale] [reason for asking the permission]
 * @param [content] [composable to display when permission is obtained]
 */
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

/**
 * A Alert Dialog to display when asking for permissions.
 * @param [text] [text message]
 * @param [onRequestPermission] [callback when requests have been granted]
 */
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