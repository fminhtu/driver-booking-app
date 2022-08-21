package com.example.driver_booking_app

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.driver_booking_app.ultils.Information
import com.example.driver_booking_app.views.activities.SignInActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView:NavigationView
    lateinit var usernameTextView: TextView
    lateinit var emailTextView: TextView
    private  lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initComponent()
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_history, R.id.nav_profile, R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener { _ ->
            logoutBuilder()
            true
        }
    }

    private fun initComponent(){
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        navView = findViewById(R.id.nav_view)

        val headerView = navView.getHeaderView(0)
        usernameTextView = headerView.findViewById(R.id.username_text_view)
        emailTextView = headerView.findViewById(R.id.email_text_view)
        usernameTextView.text = Information.username
        emailTextView.text = Information.email
    }


    private fun logoutBuilder(){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("SIGN OUT")
            .setMessage("Do you want to sign out?")
            .setNegativeButton("Cancle") { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton("Sign out") { dialogInterface, _ ->
                startActivity(Intent(this, SignInActivity::class.java))
                Information.token = ""
                Information.email = ""
                Information.phone = ""
                Information.username = ""
                Information.seed = ""
                Information.licencePlate = ""
                val prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().clear().commit()
                dialogInterface.dismiss()
                finish()
            }
            .create()
            .show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}