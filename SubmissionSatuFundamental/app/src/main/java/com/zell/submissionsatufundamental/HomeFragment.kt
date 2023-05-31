package com.zell.submissionsatufundamental

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zell.submissionsatufundamental.MainActivity.Companion.dataStore
import com.zell.submissionsatufundamental.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {

    private val homeViewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListUserAdapter by lazy {
        ListUserAdapter { username ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(username)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvListUser.layoutManager = layoutManager

        val pref = SettingPreferences.getInstance(requireActivity().dataStore)
        val settingViewModel = ViewModelProvider(requireActivity(), SettingModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.isGetFailure.observe(viewLifecycleOwner) {
            showFailure(it)
        }

        binding.ivSettings.setOnClickListener(this)
        binding.ivMoveFavorite.setOnClickListener(this)

        setListUser()
        setSearchUser()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_settings -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
            }
            R.id.iv_move_favorite -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
            }
        }
    }

    private fun setSearchUser() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    homeViewModel.searchUser(query)
                    homeViewModel.listUser.observe(viewLifecycleOwner) {listUser ->
                        adapter.setData(listUser)
                        binding.rvListUser.adapter = adapter
                    }
                }
                return false
            }
        })
    }

    private fun setListUser() {
        homeViewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            adapter.setData(listUser)
            binding.rvListUser.adapter = adapter
        }
    }

    private fun showLoading (isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showFailure (isGetFailure: Boolean) {
        if (isGetFailure) {
            Snackbar.make(requireView(), "Failed to get data!", Snackbar.LENGTH_SHORT).show()
        } else {
            Log.d("HomeFragment", "Get data success!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}