/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: Contains FireStorageRepo class for storing User's and Posts' images in FireStorage
 */

package com.v2205.foodvillage3.model.repositories

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
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that take care of storing User's and Posts' images in FireStorage
 */
class FireStorageRepo() {
    private val imageRef = Firebase.storage.reference
    private val TAG = "Debug"

    /**
     * Upload Image to Firestorage
     * @param [context] [application context]
     * @param [curFile] [the uri of the image]
     * @param [filename] [file name of the image]
     * @param [onImageUploaded] [callback when the image has been successfully uploaded to
     * firestorage]
     */
    fun uploadImageToStorage(
        context: Context,
        curFile: Uri,
        filename: String,
        onImageUploaded: (Uri) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
            try {
                curFile?.let {
                    // Create a timestamp
                    val s = SimpleDateFormat("ddMMyyyyhhmmss")
                    val format: String = s.format(Date())

                    // Upload the image
                    imageRef.child("images/$filename-$format").putFile(it).await()

                    // Retrieve the Download Url and return it with the onImageUploaded callback
                    val imageUrl = imageRef.child("images/$filename-$format").downloadUrl.await()
                    onImageUploaded(imageUrl)

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

    /**
     * Get an image from firestorage
     * @param [filename] [file name of the image]
     * firestorage]
     */
    fun getImage(filename: String) : Task<Uri> {
        return imageRef.child("images/$filename").downloadUrl
    }

    /**
     * Delete an image from firestorage
     * @param [context] [application context]
     * @param [filename] [file name of the image]
     * firestorage]
     */
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

    /**
     * Lists all the objects in firestorage
     */
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
}

data class ImageItem(
    val fileName: String,
    val url: String
)