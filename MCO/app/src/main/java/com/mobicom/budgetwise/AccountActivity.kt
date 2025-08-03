package com.mobicom.budgetwise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class AccountActivity : BaseActivity() {
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val logoutButton = findViewById<Button>(R.id.logOutBtn)
        val editPwBtn = findViewById<Button>(R.id.editPwBtn)

        val sharedPrefs = getSharedPreferences("BudgetWisePrefs", MODE_PRIVATE)
        val userId = sharedPrefs.getString("userId", null)
        val email = sharedPrefs.getString("email", null)

        val username = email?.substringBefore("@")
        welcomeText.text = "Welcome back, $username!"
        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            with(sharedPrefs.edit()) {
                clear()
                apply()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val editPasswordBtn = findViewById<Button>(R.id.editPwBtn)
        editPasswordBtn.setOnClickListener {
            val intent = Intent(this, EditPasswordActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }


    }

    override fun getCurrentNavItemId(): Int = R.id.nav_account


}
