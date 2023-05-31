package com.zell.submissionsatufundamental

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<UserResponse>>()
    val listUser: LiveData<List<UserResponse>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGetFailure = MutableLiveData<Boolean>()
    val isGetFailure: LiveData<Boolean> = _isGetFailure

    init {
        getAllUser()
    }

    private fun getAllUser() {
        _isLoading.value = true
        val client =  ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _isGetFailure.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUser(username: String) {
           _isLoading.value = true
           val client = ApiConfig.getApiService().searchUser(username)
           client.enqueue(object : Callback<SearchUser>{
               override fun onResponse(
                   call: Call<SearchUser>,
                   response: Response<SearchUser>
               ) {
                   _isLoading.value = false
                   if (response.isSuccessful) {
                       _listUser.value = response.body()?.items
                   } else {
                       Log.e(TAG, "onFailure: ${response.message()}")
                   }
               }

               override fun onFailure(call: Call<SearchUser>, t: Throwable) {
                   _isLoading.value = false
                   _isGetFailure.value = true
                   Log.e(TAG, "onFailure: ${t.message.toString()}")
               }
           })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }


}