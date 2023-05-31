package com.zell.submissionsatufundamental

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowAdapter(fragmentManager: Fragment, val username: String) : FragmentStateAdapter(fragmentManager) {

    private val item: Int = 2

    override fun getItemCount(): Int {
        return item
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position)
            putString(FollowFragment.ARG_USERNAME, username)
        }

        return fragment
    }

}