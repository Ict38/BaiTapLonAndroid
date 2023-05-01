package com.baitaplon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.baitaplon.adapter.user.ViewPagerAdapter
import com.baitaplon.model.Book
import com.google.android.material.bottomnavigation.BottomNavigationView

class ItemActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    lateinit var viewPager : ViewPager
    lateinit var viewPagerAdapter : ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        mappingVariable()
        setupViewPager()

        val book = intent.getSerializableExtra("book") as? Book
        if (book != null) {
            Log.d("Recieved","$book")
        }
    }

    private fun setupViewPager() {
        viewPagerAdapter  = ViewPagerAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = viewPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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