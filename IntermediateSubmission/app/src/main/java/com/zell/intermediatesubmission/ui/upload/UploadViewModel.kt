package com.zell.intermediatesubmission.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zell.intermediatesubmission.repo.UserRepository
import com.zell.intermediatesubmission.response.UploadResponse
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun getToken(key: String) : LiveData<String> {
        return userRepository.getData(key)
    }

    fun uploadStory(token: String,
                    description: RequestBody,
                    file: MultipartBody.Part,
                    lat: RequestBody? = null,
                    lon: RequestBody? = null) : LiveData<Result<UploadResponse>> {

        return userRepository.uploadStory(token, description, file, lat, lon)
    }
}