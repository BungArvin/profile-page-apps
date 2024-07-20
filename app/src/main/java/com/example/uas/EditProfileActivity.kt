package com.example.uas

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editPhoneNumber: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        // Bind views
        editName = findViewById(R.id.editName)
        editPhoneNumber = findViewById(R.id.editPhoneNumber)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)

        // Show existing data
        showData()

        // Set click listener for save button
        saveButton.setOnClickListener {
            if (areChangesMade()) {
                saveData()
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }

        // Customize status bar for Lollipop and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        }
    }

    private fun areChangesMade(): Boolean {
        val nameChanged = sharedPreferences.getString("name", null) != editName.text.toString()
        val emailChanged = sharedPreferences.getString("email", null) != editEmail.text.toString()
        val passwordChanged = sharedPreferences.getString("password", null) != editPassword.text.toString()
        val usernameChanged = sharedPreferences.getString("username", null) != editUsername.text.toString()
        val phoneChanged = sharedPreferences.getString("phone", null) != editPhoneNumber.text.toString()

        return nameChanged || emailChanged || passwordChanged || usernameChanged || phoneChanged
    }

    private fun saveData() {
        val editor = sharedPreferences.edit()

        editor.putString("name", editName.text.toString())
        editor.putString("email", editEmail.text.toString())
        editor.putString("password", editPassword.text.toString())
        editor.putString("username", editUsername.text.toString())
        editor.putString("phone", editPhoneNumber.text.toString())

        editor.apply()
    }

    private fun showData() {
        val name = sharedPreferences.getString("name", "")
        val email = sharedPreferences.getString("email", "")
        val username = sharedPreferences.getString("username", "")
        val phone = sharedPreferences.getString("phone", "")
        val password = sharedPreferences.getString("password", "")

        editName.setText(name)
        editEmail.setText(email)
        editUsername.setText(username)
        editPhoneNumber.setText(phone)
        editPassword.setText(password)
    }
}
