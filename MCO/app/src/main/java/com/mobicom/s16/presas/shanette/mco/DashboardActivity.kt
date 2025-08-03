package com.mobicom.s16.presas.shanette.mco


import android.os.Bundle


class DashboardActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun getCurrentNavItemId(): Int = R.id.nav_dashboard


}