package com.zell.intermediatesubmission.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.databinding.FragmentRegisterBinding
import com.zell.intermediatesubmission.helper.isLoading
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        binding.edRegisterEmail.addOnValidChangedListener { validEmail ->
            binding.edRegisterPassword.addOnValidChangedListener { validPass ->
                binding.btnRegister.isEnabled = validEmail && validPass
            }
        }

        binding.btnRegister.setOnClickListener { registerUser() }
        playAnimation()
    }

    private fun playAnimation() {

        val imageLogo = ObjectAnimator.ofFloat(binding.ivDicoding, View.ALPHA, 1f).setDuration(500)
        val titleOne = ObjectAnimator.ofFloat(binding.tvHalo, View.ALPHA, 1f).setDuration(500)
        val titleTwo = ObjectAnimator.ofFloat(binding.tvJoin, View.ALPHA, 1f).setDuration(500)
        val registerName =ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val registerEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val registerPassword = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val buttonRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(imageLogo, titleOne, titleTwo, registerName, registerEmail, registerPassword, buttonRegister)
            start()
        }
    }

    private fun registerUser() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        authViewModel.userRegister(name, email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    isLoading(true, binding.progressBar)
                    binding.btnRegister.isEnabled = false
                }
                is Result.Success -> {
                    isLoading(false, binding.progressBar)
                    Toast.makeText(
                        context,
                        resources.getString(R.string.success),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToAuthFragment())
                }
                is Result.Error -> {
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(
                        context,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}