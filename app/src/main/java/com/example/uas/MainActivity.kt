package com.example.uas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var profileImg: ImageView
    private lateinit var profileName: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var editButton: Button
    private lateinit var togglePasswordVisibility: ImageView

    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImg = findViewById(R.id.profileImg)
        profileName = findViewById(R.id.profileName)
        phoneNumber = findViewById(R.id.phoneNumber)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        profilePassword = findViewById(R.id.profilePassword)
        editButton = findViewById(R.id.editButton)
        togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility)

        titleName = findViewById(R.id.titleName)
        titleUsername = findViewById(R.id.titleUsername)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                if (selectedImageUri != null) {
                    val savedImagePath = saveImageToInternalStorage(selectedImageUri)
                    if (savedImagePath != null) {
                        profileImg.setImageURI(Uri.parse(savedImagePath))
                        saveImagePath(savedImagePath)
                    }
                }
            }
        }

        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        }

        profileImg.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (allPermissionsGranted()) {
                    openGallery()
                } else {
                    ActivityCompat.requestPermissions(this, storagePermissions, 1)
                }
            } else {
                openGallery()
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, 1)
        }

        togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            updatePasswordVisibility()
        }

        loadData()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun allPermissionsGranted() = storagePermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "name")
        val phone = sharedPreferences.getString("phone", "phone")
        val email = sharedPreferences.getString("email", "email")
        val username = sharedPreferences.getString("username", "username")
        val password = sharedPreferences.getString("password", "password")
        val imagePath = sharedPreferences.getString("profileImagePath", null)

        profileName.text = name
        phoneNumber.text = phone
        profileEmail.text = email
        profileUsername.text = username
        profilePassword.text = password // Set the password text

        titleName.text = name
        titleUsername.text = username

        Log.d("MainActivity", "Loaded imagePath: $imagePath")
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            profileImg.setImageBitmap(bitmap)
        }
    }

    private fun updatePasswordVisibility() {
        if (isPasswordVisible) {
            profilePassword.inputType = android.text.InputType.TYPE_CLASS_TEXT
            togglePasswordVisibility.setImageResource(R.drawable.ic_eye_open) // Icon for visible
        } else {
            profilePassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            togglePasswordVisibility.setImageResource(R.drawable.ic_eye_closed) // Icon for hidden
        }
        profilePassword.text = profilePassword.text // Refresh text to apply changes
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val fileName = "profile_image.jpg"
        val file = File(filesDir, fileName)
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun saveImagePath(path: String) {
        val sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("profileImagePath", path)
        editor.apply()
        Log.d("MainActivity", "Saved imagePath: $path")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadData()
        }
    }
}
