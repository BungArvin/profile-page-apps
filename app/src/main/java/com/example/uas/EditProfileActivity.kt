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

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        editName = findViewById(R.id.editName)
        editPhoneNumber = findViewById(R.id.editPhoneNumber)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)

        showData()

        saveButton.setOnClickListener {
            if (saveData()) {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        }
    }

    private fun saveData(): Boolean {
        val editor = sharedPreferences.edit()

        val name = editName.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        val username = editUsername.text.toString()
        val phone = editPhoneNumber.text.toString()

        var isChanged = false

        if (sharedPreferences.getString("name", null) != name) {
            editor.putString("name", name)
            isChanged = true
        }

        if (sharedPreferences.getString("email", null) != email) {
            editor.putString("email", email)
            isChanged = true
        }

        if (sharedPreferences.getString("password", null) != password) {
            editor.putString("password", password)
            isChanged = true
        }

        if (sharedPreferences.getString("username", null) != username) {
            editor.putString("username", username)
            isChanged = true
        }

        if (sharedPreferences.getString("phone", null) != phone) {
            editor.putString("phone", phone)
            isChanged = true
        }

        if (isChanged) {
            editor.apply()
        }

        return isChanged
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
