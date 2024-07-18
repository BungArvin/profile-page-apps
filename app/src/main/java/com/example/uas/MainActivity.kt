package com.example.uas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var editButton: Button

    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        profileName = findViewById(R.id.profileName)
        phoneNumber = findViewById(R.id.phoneNumber)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        profilePassword = findViewById(R.id.profilePassword)
        editButton = findViewById(R.id.editButton)

        titleName = findViewById(R.id.titleName)
        titleUsername = findViewById(R.id.titleUsername)

        loadData()

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadData()
        }
    }

    private fun loadData() {
        val name = sharedPreferences.getString("name", "name")
        val phone = sharedPreferences.getString("phone", "phone")
        val email = sharedPreferences.getString("email", "email")
        val username = sharedPreferences.getString("username", "username")
        val password = sharedPreferences.getString("password", "password")

        profileName.text = name
        phoneNumber.text = phone
        profileEmail.text = email
        profileUsername.text = username
        profilePassword.text = password

        titleName.text = name
        titleUsername.text = username
    }
}
