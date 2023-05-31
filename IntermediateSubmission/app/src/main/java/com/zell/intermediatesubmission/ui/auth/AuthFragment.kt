package com.zell.intermediatesubmission.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.databinding.FragmentAuthBinding
import com.zell.intermediatesubmission.helper.isLoading
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToRegisterFragment())
        }

        binding.edLoginEmail.addOnValidChangedListener { validEmail ->
            binding.edLoginPassword.addOnValidChangedListener { validPass ->
                binding.btnLogin.isEnabled = validEmail && validPass
            }
        }

        binding.btnLogin.setOnClickListener { loginUser() }

        playAnimation()
    }

    private fun playAnimation() {

        val imageLogo = ObjectAnimator.ofFloat(binding.ivDicoding, View.ALPHA, 1f).setDuration(500)
        val titleOne = ObjectAnimator.ofFloat(binding.tvHalo, View.ALPHA, 1f).setDuration(500)
        val titleTwo = ObjectAnimator.ofFloat(binding.tvLoginBegin, View.ALPHA, 1f).setDuration(500)
        val loginEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val loginPassword = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val textRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val textNeedAccount = ObjectAnimator.ofFloat(binding.tvDontHaveAccount, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(textRegister, textNeedAccount)
        }

        AnimatorSet().apply {
            playSequentially(imageLogo, titleOne, titleTwo, loginEmail, loginPassword, buttonLogin,
            together)
            start()
        }
    }

    private fun loginUser() {
        authViewModel.userLogin(
            binding.edLoginEmail.text.toString(),
            binding.edLoginPassword.text.toString()
        ).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    isLoading(true, binding.progressBar)
                    binding.btnLogin.isEnabled = false
                }

                is Result.Success -> {
                    isLoading(false, binding.progressBar)
                    val data = mapOf(
                        "username" to result.data.loginResult?.name.toString(),
                        "email" to binding.edLoginEmail.text.toString(),
                        "token" to result.data.loginResult?.token.toString()
                    )
                    authViewModel.saveData(data).observe(viewLifecycleOwner) { resultData ->
                        when (resultData) {
                            is Result.Loading -> {}
                            is Result.Success -> {
                                isLoading(false, binding.progressBar)
                                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToHomeFragment())
                            }
                            is Result.Error -> {
                                Toast.makeText(
                                    context,
                                    "${resources.getString(R.string.error_standard_message)}, ${result.data.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                is Result.Error -> {
                    isLoading(false, binding.progressBar)
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(
                        context,
                        "${resources.getString(R.string.error_standard_message)}, ${result.error}",
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