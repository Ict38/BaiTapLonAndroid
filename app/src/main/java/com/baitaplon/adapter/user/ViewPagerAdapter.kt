package com.baitaplon.adapter.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.ListFragment
import com.baitaplon.fragment.user.CommentFragment
import com.baitaplon.fragment.user.InfoFragment
import com.baitaplon.fragment.user.ShopFragment

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return InfoFragment()
            1 -> return ShopFragment()
            2 -> return CommentFragment()
        }
        return InfoFragment()
    }

    override fun getCount(): Int {
        return 3
    }
}