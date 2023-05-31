package com.zell.intermediatesubmission.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionTokenCheck()
    }

    private fun sessionTokenCheck() {
        authViewModel.getData("token").observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(2000L)

                if(it.isNotEmpty()) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                } else {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthFragment())
                }
            }
        }
    }
}