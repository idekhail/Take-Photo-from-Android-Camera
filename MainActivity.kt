package com.aaa_idekhail.aaa_takephoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var openCamera: Button
    private lateinit var clickImage: ImageView
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private var CAMERA_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        openCamera = findViewById(R.id.camera_open)
        clickImage = findViewById(R.id.click_image)

        openCamera.setOnClickListener {
            photoFile = createImageFile()

            photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }
    private fun createImageFile(): File{
        val imageDir = File(cacheDir, "images").apply { mkdirs() }
        return File.createTempFile("captured_", ".jpg", imageDir)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Load the captured image from the URI into the ImageView using Glide
            Glide.with(this).load(photoUri).into(clickImage)
        }
    }
}