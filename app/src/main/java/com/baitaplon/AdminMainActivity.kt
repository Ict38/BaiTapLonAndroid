package com.baitaplon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.baitaplon.adapter.user.BookItemRecyclerViewAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var image : ImageView
    private lateinit var drawer : DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        image = findViewById(R.id.menuIcon)
        drawer = findViewById(R.id.drawer)

        image.setOnClickListener{
            drawer.openDrawer(GravityCompat.START)
        }

        toggle = ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.syncState()

        navView = findViewById(R.id.navigation_view)
        navView.setNavigationItemSelectedListener(this)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                auth.signOut()
                Toast.makeText(applicationContext, "SIGN OUT", Toast.LENGTH_SHORT).show()
                val mainToLoginIntent = Intent(this@AdminMainActivity, LoginActivity::class.java)
                startActivity(mainToLoginIntent)
                finish()
            }
            R.id.nav_search -> {

            }
            R.id.nav_about -> {}
            R.id.nav_feedback -> {}
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}