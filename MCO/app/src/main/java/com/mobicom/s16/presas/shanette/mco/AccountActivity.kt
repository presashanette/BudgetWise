package com.mobicom.s16.presas.shanette.mco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
//      Uncomment nalang when u make account modelss
//        val userName = intent.getStringExtra("USER_NAME") ?: "User"
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val logoutButton = findViewById<Button>(R.id.logOutBtn)

//        welcomeText.text = "Welcome back, $userName!"
        welcomeText.text = "Welcome back, Sir Oliver!"
        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun getCurrentNavItemId(): Int = R.id.nav_account
}
