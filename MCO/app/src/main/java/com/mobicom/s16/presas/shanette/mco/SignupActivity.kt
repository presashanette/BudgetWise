package com.mobicom.s16.presas.shanette.mco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnCreate = findViewById<Button>(R.id.btnCreate) // create account button
        val btnCancel = findViewById<Button>(R.id.btnCancel) // cancel button

        btnCreate.setOnClickListener {
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
