package com.example.virtuousvoice.Adaptors

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdaptor(supportFragmentManager: FragmentManager): FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val localFragmentList = ArrayList<Fragment>()
    private val localFragmentTitles = ArrayList<String>()
    override fun getCount(): Int {
        return localFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return localFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return localFragmentTitles[position]
    }

    fun addFragment(fragment: Fragment, title: String){
        localFragmentList.add(fragment)
        localFragmentTitles.add(title)
    }
}