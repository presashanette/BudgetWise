package com.mobicom.budgetwise

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mainFab: FloatingActionButton
    protected lateinit var btnAddExpense: FloatingActionButton
    protected lateinit var btnScanReceipt: FloatingActionButton
    protected lateinit var addExpenseContainer: LinearLayout
    protected lateinit var scanReceiptContainer: LinearLayout

    private var clicked = false

    // Animations for the FABs
    private val rotateOpen by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    // Override default setContentView so we can inject our base layout
    override fun setContentView(layoutResID: Int) {
        val baseLayout = layoutInflater.inflate(R.layout.activity_base, null)
        val container = baseLayout.findViewById<FrameLayout>(R.id.container)
        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(baseLayout)

        initFab()
        initBottomNav()
    }

     // Sets up FAB behavior and click listeners
    private fun initFab() {
        mainFab = findViewById(R.id.mainFab)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnScanReceipt = findViewById(R.id.btnScanReceipt)
        addExpenseContainer = findViewById(R.id.addExpenseContainer)
        scanReceiptContainer = findViewById(R.id.scanReceiptContainer)

        addExpenseContainer.visibility = View.INVISIBLE
        scanReceiptContainer.visibility = View.INVISIBLE

        mainFab.setOnClickListener { onAddButtonClicked() }

        // Add Expense clicked
        btnAddExpense.setOnClickListener {
            if (clicked) onAddButtonClicked()

            val userId = getUserIdFromPrefs()
            if (userId.isNullOrEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return@setOnClickListener
            }

            // Go to Add Expense screen
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        // Scan Receipt clicked
        btnScanReceipt.setOnClickListener {
            if (clicked) onAddButtonClicked()

            val userId = getUserIdFromPrefs()
            if (userId.isNullOrEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return@setOnClickListener
            }

            // Go to Scan Receipt screen
            val intent = Intent(this, UploadReceiptActivity::class.java)
            startActivity(intent)
        }

        if (!shouldShowScanReceipt()) {
            scanReceiptContainer.visibility = View.GONE
        }
    }

    // Bottom nav setup and page switching logic
    private fun initBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            val currentUserId = getUserIdFromPrefs()
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    if (this !is DashboardActivity) {
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                R.id.nav_expenses -> {
                    if (this !is ExpenseHistoryActivity) {
                        val intent = Intent(this, ExpenseHistoryActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                R.id.nav_account -> {
                    if (this !is AccountActivity) {
                        val intent = Intent(this, AccountActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }

        // Highlight current nav item if applicable
        getCurrentNavItemId()?.let {
            val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
            bottomNavView.selectedItemId = it
        }
    }

    // Toggles FAB animations and visibility
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    // Show/hide the action buttons
    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addExpenseContainer.visibility = View.VISIBLE
            if (shouldShowScanReceipt()) scanReceiptContainer.visibility = View.VISIBLE
        } else {
            addExpenseContainer.visibility = View.INVISIBLE
            if (shouldShowScanReceipt()) scanReceiptContainer.visibility = View.INVISIBLE
        }
    }

    // Play animations for FAB toggle
    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            addExpenseContainer.startAnimation(fromBottom)
            if (shouldShowScanReceipt()) scanReceiptContainer.startAnimation(fromBottom)
            mainFab.startAnimation(rotateOpen)
        } else {
            addExpenseContainer.startAnimation(toBottom)
            if (shouldShowScanReceipt()) scanReceiptContainer.startAnimation(toBottom)
            mainFab.startAnimation(rotateClose)
        }
    }

    open fun shouldShowScanReceipt(): Boolean = true
    open fun getCurrentNavItemId(): Int? = null

    // Helper to get user ID from shared prefs
    protected fun getUserIdFromPrefs(): String? {
        val sharedPrefs = getSharedPreferences("BudgetWisePrefs", MODE_PRIVATE)
        return sharedPrefs.getString("userId", null)
    }

    // Helper to get email from shared prefs
    protected fun getEmailFromPrefs(): String? {
        val sharedPrefs = getSharedPreferences("BudgetWisePrefs", MODE_PRIVATE)
        return sharedPrefs.getString("email", null)
    }

}

