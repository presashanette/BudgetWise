package com.mobicom.s16.presas.shanette.mco

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.Manifest
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import android.view.animation.AnimationUtils
import android.widget.FrameLayout

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mainFab: FloatingActionButton
    protected lateinit var btnAddExpense: FloatingActionButton
    protected lateinit var btnScanReceipt: FloatingActionButton

    private var clicked = false

    private val rotateOpen by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private val CAMERA_REQUEST_CODE = 1001

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val scannedAmount = "550.00"
            val scannedCategory = "Dining"
            val intent = Intent(this, AddExpenseActivity::class.java)
            intent.putExtra("amount", scannedAmount)
            intent.putExtra("category", scannedCategory)
            startActivity(intent)
        }
    }

    override fun setContentView(layoutResID: Int) {
        val baseLayout = layoutInflater.inflate(R.layout.activity_base, null)
        val container = baseLayout.findViewById<FrameLayout>(R.id.container)
        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(baseLayout)

        initFab()
        initBottomNav()
    }

    private fun initFab() {
        mainFab = findViewById(R.id.mainFab)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnScanReceipt = findViewById(R.id.btnScanReceipt)

        mainFab.setOnClickListener { onAddButtonClicked() }

        btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        btnScanReceipt.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            } else {
                openCamera()
            }
        }

        if (!shouldShowScanReceipt()) {
            btnScanReceipt.visibility = View.GONE
        }
    }

    private fun initBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    if (this !is DashboardActivity) {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }
                    true
                }

                R.id.nav_expenses -> {
                    if (this !is ExpenseHistoryActivity) {
                        startActivity(Intent(this, ExpenseHistoryActivity::class.java))
                        finish()
                    }
                    true
                }
                R.id.nav_account -> {
                    if (this !is AccountActivity) {
                        startActivity(Intent(this, AccountActivity::class.java))
                        finish()
                    }
                    true
                }
                else -> false
            }
        }

        getCurrentNavItemId()?.let {
            bottomNav.selectedItemId = it
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        btnAddExpense.visibility = if (!clicked) View.VISIBLE else View.INVISIBLE
        if (shouldShowScanReceipt()) {
            btnScanReceipt.visibility = if (!clicked) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            btnAddExpense.startAnimation(fromBottom)
            if (shouldShowScanReceipt()) btnScanReceipt.startAnimation(fromBottom)
            mainFab.startAnimation(rotateOpen)
        } else {
            btnAddExpense.startAnimation(toBottom)
            if (shouldShowScanReceipt()) btnScanReceipt.startAnimation(toBottom)
            mainFab.startAnimation(rotateClose)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    /** Override this in child activities to hide scan FAB **/
    open fun shouldShowScanReceipt(): Boolean = true
    open fun getCurrentNavItemId(): Int? = null // override in child activities
}
