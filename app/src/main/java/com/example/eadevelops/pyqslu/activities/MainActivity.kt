package com.example.eadevelops.pyqslu.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.activities.drawer_activities.AboutUsActivity
import com.example.eadevelops.pyqslu.activities.drawer_activities.MyProfileActivity
import com.example.eadevelops.pyqslu.activities.drawer_activities.NotesActivity
import com.example.eadevelops.pyqslu.activities.drawer_activities.PYQsActivity
import com.example.eadevelops.pyqslu.databinding.ActivityMainBinding
import com.example.eadevelops.pyqslu.fragments.bottom_nav.AddFragment
import com.example.eadevelops.pyqslu.fragments.bottom_nav.HomeFragment
import com.example.eadevelops.pyqslu.fragments.bottom_nav.NoticeFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.homeFragment -> openFragmentBottomNav(HomeFragment())
                R.id.addFragment -> openFragmentBottomNav(AddFragment())
                R.id.reelFragment -> openFragmentBottomNav(NoticeFragment())
            }
            true
        }
        //The following code is to make header clickable
        val header = binding.navDrawer.getHeaderView(0)

        val viewProfileText = header.findViewById<View>(R.id.view_profile)
        val viewProfileImage = header.findViewById<View>(R.id.view_profile_img)
        viewProfileImage.setOnClickListener { startActivity(Intent(this, MyProfileActivity::class.java)) }
        viewProfileText.setOnClickListener { startActivity(Intent(this, MyProfileActivity::class.java)) }

        fragmentManager = supportFragmentManager
        openFragmentBottomNav(HomeFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_pyq -> openActivityDrawer(PYQsActivity())
            R.id.nav_notes -> openActivityDrawer(NotesActivity())
            R.id.nav_about -> openActivityDrawer(AboutUsActivity())
            R.id.nav_uni_web -> openWeb("https://www.lkouniv.ac.in/")
            // to be filled when uploaded on play store
//            R.id.nav_share -> openWeb("")
            R.id.nav_logout -> {
                AlertDialog.Builder(this).setMessage("Are you sure?")
                    .setPositiveButton("Logout") { _, _ ->
                        Firebase.auth.signOut()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }.setNegativeButton("Cancel") { _, _ -> }
                    .create().show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    private fun openActivityDrawer(activity: Activity){
        startActivity(Intent(this, activity::class.java))
    }

    private fun  openFragmentBottomNav(fragment: Fragment){
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun openWeb(url : String){
        val openWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(openWebsiteIntent)
    }
}
