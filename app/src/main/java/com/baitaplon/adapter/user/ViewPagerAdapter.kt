package com.baitaplon.adapter.user

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.baitaplon.fragment.user.InfoFragment
import com.baitaplon.fragment.user.ShopFragment
import com.baitaplon.model.Book

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    private var book: Book? = null
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return InfoFragment(book)
            1 -> return ShopFragment()
        }
        return InfoFragment(book)
    }

    override fun getCount(): Int {
        return 2
    }

    fun setBook(book : Book){
        this.book = book
    }
}