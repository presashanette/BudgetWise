package com.mobicom.budgetwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class SignupActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailField = findViewById<EditText>(R.id.etvEmail)
        val passwordField = findViewById<EditText>(R.id.etvPassword)
        val cPasswordField = findViewById<EditText>(R.id.etvCPassword)
        val btnCreate = findViewById<Button>(R.id.btnCreate) // create account button
        val btnCancel = findViewById<Button>(R.id.btnCancel) // cancel button

        database = FirebaseDatabase.getInstance().reference.child("Users")

        btnCreate.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val cPassword = cPasswordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || cPassword.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else if (password != cPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(email, password)
                val userKey = database.push().key!!

                database.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // Email already exists
                                Toast.makeText(
                                    this@SignupActivity,
                                    "Email is already registered!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Email is available — create new account
                                val user = User(email, password)
                                val userKey = database.push().key!!

                                database.child(userKey).setValue(user).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(
                                            this@SignupActivity,
                                            "Signup Successful!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Move to Dashboard
                                        val intent = Intent(
                                            this@SignupActivity,
                                            DashboardActivity::class.java
                                        )
                                        intent.putExtra("email", email)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@SignupActivity,
                                            "Signup failed. Try again.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@SignupActivity,
                                "Database error: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}

