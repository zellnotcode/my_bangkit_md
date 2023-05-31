package com.zell.intermediatesubmission.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zell.intermediatesubmission.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getData(key: String) : LiveData<String> {
        return userRepository.getData(key)
    }

    suspend fun clearData() {
        return userRepository.clearData()
    }
}