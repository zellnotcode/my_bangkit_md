package com.zell.submissionsatufundamental

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel : ViewModel() {

    private val _followingDetail = MutableLiveData<List<UserResponse>>()
    val followingDetail: LiveData<List<UserResponse>> = _followingDetail

    private val _followersDetail = MutableLiveData<List<UserResponse>>()
    val followersDetail: LiveData<List<UserResponse>> = _followersDetail

    private val _userDetail = MutableLiveData<UserResponse>()
    val userDetail: LiveData<UserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGetFailure = MutableLiveData<Boolean>()
    val isGetFailure: LiveData<Boolean> = _isGetFailure

     fun getDetailUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _isGetFailure.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getListFollowing(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailFollowing(username)
        client.enqueue(object : Callback<List<UserResponse>>{
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followingDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure Get Following Data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _isGetFailure.value = true
                Log.e(TAG, "onFailure Get Following Data: ${t.message.toString()}")
            }
        })
    }

    fun getListFollowers(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailFollowers(username)
        client.enqueue(object : Callback<List<UserResponse>>{
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followersDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure Get Following Data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _isGetFailure.value = true
                Log.e(TAG, "onFailure Get Following Data: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}