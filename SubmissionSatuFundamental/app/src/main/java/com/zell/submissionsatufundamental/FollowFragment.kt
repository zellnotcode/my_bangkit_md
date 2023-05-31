package com.zell.submissionsatufundamental

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zell.submissionsatufundamental.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by viewModels<DetailViewModel>()
    private var position: Int = 0
    private var username: String = ""
    private lateinit var adapter: ListFollowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }

        detailViewModel.isGetFailure.observe(viewLifecycleOwner) {
            showFailure(it)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        adapter = ListFollowAdapter()
        binding.rvFollow.adapter = adapter

        when (position) {
            0 -> setListFollowers()
            1 -> setListFollowing()
            else -> Snackbar.make(requireView(), "Invalid Position", Snackbar.LENGTH_SHORT).show()
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager
        binding.rvFollow.setHasFixedSize(true)
    }

    private fun setListFollowing() {
        detailViewModel.getListFollowing(username)
        detailViewModel.followingDetail.observe(viewLifecycleOwner) { listFollowing ->
            adapter.setData(listFollowing)
        }
    }

    private fun setListFollowers() {
        detailViewModel.getListFollowers(username)
        detailViewModel.followersDetail.observe(viewLifecycleOwner) { listFollowers ->
            adapter.setData(listFollowers)
        }
    }

    private fun showFailure(isGetFailure: Boolean) {
        if (isGetFailure) {
            Snackbar.make(requireView(), "Failed to get data!", Snackbar.LENGTH_SHORT).show()
        } else {
            Log.d("HomeFragment", "Get data success!")
        }
    }

    private fun showLoading (isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "username"
    }
}