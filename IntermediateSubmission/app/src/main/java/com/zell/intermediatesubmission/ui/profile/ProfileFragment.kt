package com.zell.intermediatesubmission.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                profileViewModel.clearData()
                findNavController().navigate(R.id.authFragment)
            }
        }

        setDataUser()
    }

    private fun setDataUser() {
        profileViewModel.getData("username").observe(viewLifecycleOwner) { username ->
            binding.tvDetailName.text = username
        }

        profileViewModel.getData("email").observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}