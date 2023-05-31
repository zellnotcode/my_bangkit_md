package com.zell.intermediatesubmission.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zell.intermediatesubmission.repo.UserRepository
import com.zell.intermediatesubmission.response.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getToken(key: String) : LiveData<String> {
        return userRepository.getData(key)
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        userRepository.getStories(token).cachedIn(viewModelScope)
}