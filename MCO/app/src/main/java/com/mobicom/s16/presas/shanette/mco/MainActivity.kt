package com.mobicom.s16.presas.shanette.mco

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
