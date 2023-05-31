package com.zell.intermediatesubmission.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.zell.intermediatesubmission.api.ApiServices
import com.zell.intermediatesubmission.local.DataStoreManager
import com.zell.intermediatesubmission.response.*
import com.zell.intermediatesubmission.utils.Result
import com.zell.intermediatesubmission.utils.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiServices: ApiServices, private val dataStoreManager: DataStoreManager) {

    fun userRegister(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        val registerResponse = MutableLiveData<Result<RegisterResponse>>()
        registerResponse.value = Result.Loading
        apiServices.userRegister(name, email, password).enqueue(object :Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    registerResponse.value = Result.Success(response.body()!!)
                } else {
                    registerResponse.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerResponse.value = Result.Error(t.message ?: "Unknown Error")
            }
        })
        return registerResponse
    }

    fun userLogin(email: String, password: String): LiveData<Result<AuthResponse>> {
        val authResponse = MutableLiveData<Result<AuthResponse>>()
        authResponse.value = Result.Loading
        apiServices.userLogin(email, password).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    authResponse.value = Result.Success(response.body()!!)
                } else {
                    authResponse.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                authResponse.value = Result.Error(t.message ?: "Unknown Error")
            }
        })
        return authResponse
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiServices, token, 0)
            }
        ).liveData
    }

    fun getMapStories(token: String): LiveData<Result<StoriesResponse>> = liveData {
        emit(Result.Loading)
         try {
            val response = apiServices.getAllStory(token, size = 100, location = 1)
             if (response.error == false) {
                 emit(Result.Success(response))
             }


        } catch (e: Exception) {
            emit(Result.Error("${e.message}"))
        }
    }

    fun getDetailStory(id: String, token: String) : LiveData<Result<DetailResponse>> {
        val detailResponse = MutableLiveData<Result<DetailResponse>>()
        detailResponse.value = Result.Loading
        apiServices.getDetailStory(id, token).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    detailResponse.value = Result.Success(response.body()!!)
                } else {
                    detailResponse.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                detailResponse.value = Result.Error(t.message ?: "Unknown Error")
            }
        })
        return detailResponse
    }

    fun uploadStory(token: String,
                    description: RequestBody,
                    file: MultipartBody.Part,
                    lat: RequestBody? = null,
                    lon: RequestBody? = null) : LiveData<Result<UploadResponse>> {
        val uploadResponse = MutableLiveData<Result<UploadResponse>>()
        uploadResponse.value = Result.Loading
        try {
            apiServices.uploadStory(token, description, file, lat, lon).enqueue(object : Callback<UploadResponse>{
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    if (response.isSuccessful) {
                        uploadResponse.value = Result.Success(response.body()!!)
                    } else {
                        uploadResponse.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    uploadResponse.value = Result.Error(t.message ?: "Unknown Error")
                }
            })
        } catch (e: Exception) {
            uploadResponse.value = Result.Error(e.message ?: "Unknown Error")
        }
        return uploadResponse
    }

    fun saveData(data: Map<String, String>): LiveData<Result<Boolean>> = liveData {
        emit(Result.Loading)
        try {
            dataStoreManager.saveData(data)
            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getData(key: String): LiveData<String> {
        return dataStoreManager.getData(key).asLiveData()
    }

    suspend fun clearData() {
        dataStoreManager.clearData()
    }
}