package com.mobicom.budgetwise


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DashboardActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val email = intent.getStringExtra("email")
        val tvUser = findViewById<TextView>(R.id.tvUser)
        val username = email?.substringBefore("@") ?: "User"
        tvUser.text = "Welcome, $username"
    }

    override fun getCurrentNavItemId(): Int = R.id.nav_dashboard


}