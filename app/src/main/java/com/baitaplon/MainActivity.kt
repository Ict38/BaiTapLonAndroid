package com.baitaplon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.baitaplon.adapter.user.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    lateinit var viewPager : ViewPager
    lateinit var viewPagerAdapter :ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mappingVariable()
        setupViewPager()

    }

    private fun setupViewPager() {
        viewPagerAdapter  = ViewPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = viewPagerAdapter
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNav.menu.findItem(R.id.info).isChecked = true
                    1 -> bottomNav.menu.findItem(R.id.shop).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.info -> viewPager.currentItem = 0
                R.id.shop -> viewPager.currentItem = 1
            }
            false
        })
    }

    private fun mappingVariable() {
        bottomNav = findViewById(R.id.bottomnav)
        viewPager = findViewById(R.id.viewPager)
    }
}