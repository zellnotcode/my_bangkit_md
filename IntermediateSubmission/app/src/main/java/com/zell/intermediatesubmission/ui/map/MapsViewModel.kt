package com.zell.intermediatesubmission.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zell.intermediatesubmission.repo.UserRepository
import com.zell.intermediatesubmission.response.StoriesResponse
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getAllStories(token: String) : LiveData<Result<StoriesResponse>> {
        return userRepository.getMapStories(token)
    }

    fun getToken(key: String) : LiveData<String> {
        return userRepository.getData(key)
    }

}