package com.zell.submissionsatufundamental

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getAllUser() : Call<List<UserResponse>>

    @GET("search/users")
    fun searchUser(@Query("q") username: String) : Call<SearchUser>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String) : Call<UserResponse>

    @GET("users/{username}/followers")
    fun getDetailFollowers(@Path("username") username: String) : Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getDetailFollowing(@Path("username") username: String) : Call<List<UserResponse>>
}