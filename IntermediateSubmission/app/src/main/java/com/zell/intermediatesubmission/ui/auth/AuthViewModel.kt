package com.zell.intermediatesubmission.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zell.intermediatesubmission.repo.UserRepository
import com.zell.intermediatesubmission.response.AuthResponse
import com.zell.intermediatesubmission.response.RegisterResponse
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun userRegister(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        return userRepository.userRegister(name, email, password)
    }

    fun userLogin(email: String, password: String): LiveData<Result<AuthResponse>> {
        return userRepository.userLogin(email, password)
    }

    fun saveData(data: Map<String, String>) = userRepository.saveData(data)

    fun getData(key: String) : LiveData<String> {
        return userRepository.getData(key)
    }

}