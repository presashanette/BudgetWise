package com.mobicom.s16.presas.shanette.mco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)

        btnLogin.setOnClickListener {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            this.startActivity(intent)
        }

        btnSignup.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            this.startActivity(intent)
        }
    }
}
