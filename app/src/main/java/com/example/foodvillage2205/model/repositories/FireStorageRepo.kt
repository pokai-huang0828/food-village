package com.example.foodvillage2205.model.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FireStorageRepo() {
    private val imageRef = Firebase.storage.reference
    private val TAG = "Debug"

    fun uploadImageToStorage(context: Context, curFile: Uri, filename: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                curFile?.let {
                    imageRef.child("images/$filename").putFile(it).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Successfully uploaded image",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    suspend fun listFiles(): MutableList<ImageItem> {
        var result = mutableListOf<ImageItem>()

        try {
            CoroutineScope(Dispatchers.IO).launch {
                val images = imageRef.child("images/").listAll().await()
                val imageUrls = mutableListOf<ImageItem>()

                for (image in images.items) {
                    val url = image.downloadUrl.await()
                    val fileName = image.name
                    imageUrls.add(ImageItem(fileName = fileName, url = url.toString()))
                }

                withContext(Dispatchers.Main) {
                    result = imageUrls
                }
            }.join()

        } catch (e: Exception) {
            Log.d(TAG, e.message ?: "Could not list uploaded images")
        }

        return result
    }

    fun getImage(filename: String) : Task<Uri> {
        return imageRef.child("images/$filename").downloadUrl
    }

    fun deleteImage(context: Context, filename: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            imageRef.child("images/$filename").delete().await()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, "Successfully deleted image.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

data class ImageItem(
    val fileName: String,
    val url: String
)