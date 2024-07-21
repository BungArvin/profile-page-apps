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
            if (isNameChanged() || isEmailChanged() || isPasswordChanged() || isUserNameChanged() || isPhoneNumberChanged()) {
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

    private fun isNameChanged(): Boolean {
        val name = sharedPreferences.getString("name", null)
        return if (name != editName.text.toString()) {
            sharedPreferences.edit().putString("name", editName.text.toString()).apply()
            true
        } else {
            false
        }
    }

    private fun isEmailChanged(): Boolean {
        val email = sharedPreferences.getString("email", null)
        return if (email != editEmail.text.toString()) {
            sharedPreferences.edit().putString("email", editEmail.text.toString()).apply()
            true
        } else {
            false
        }
    }

    private fun isPasswordChanged(): Boolean {
        val password = sharedPreferences.getString("password", null)
        return if (password != editPassword.text.toString()) {
            sharedPreferences.edit().putString("password", editPassword.text.toString()).apply()
            true
        } else {
            false
        }
    }

    private fun isUserNameChanged(): Boolean {
        val username = sharedPreferences.getString("username", null)
        return if (username != editUsername.text.toString()) {
            sharedPreferences.edit().putString("username", editUsername.text.toString()).apply()
            true
        } else {
            false
        }
    }

    private fun isPhoneNumberChanged(): Boolean {
        val phone = sharedPreferences.getString("phone", null)
        return if (phone != editPhoneNumber.text.toString()) {
            sharedPreferences.edit().putString("phone", editPhoneNumber.text.toString()).apply()
            true
        } else {
            false
        }
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
