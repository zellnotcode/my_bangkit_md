package com.zell.intermediatesubmission.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zell.intermediatesubmission.repo.UserRepository
import com.zell.intermediatesubmission.response.DetailResponse
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getDetailStory(id: String, token: String) : LiveData<Result<DetailResponse>> {
        return userRepository.getDetailStory(id, token)
    }

    fun getToken(key: String) : LiveData<String> {
        return userRepository.getData(key)
    }
}