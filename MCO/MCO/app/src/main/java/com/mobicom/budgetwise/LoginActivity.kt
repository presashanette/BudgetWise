package com.mobicom.budgetwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<EditText>(R.id.etvEmail)
        val passwordField = findViewById<EditText>(R.id.etvPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)

        database = FirebaseDatabase.getInstance().reference.child("Users")

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var foundUser = false
                        for (userSnap in snapshot.children) {
                            val user = userSnap.getValue(User::class.java)
                            if (user?.email == email && user.password == password) {
                                foundUser = true
                                break
                            }
                        }
                        if (foundUser) {
                            Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

//            val intent = Intent(applicationContext, DashboardActivity::class.java)
//            this.startActivity(intent)
        }

        btnSignup.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            this.startActivity(intent)
        }
    }
}

