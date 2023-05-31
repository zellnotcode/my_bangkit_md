package com.zell.submissionsatufundamental

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class FavoriteEditViewModel(application: Application) : ViewModel() {
    private var mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite() : LiveData<List<FavoriteUser>> = mFavoriteRepository.getAllFavorite()

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteRepository.delete(favoriteUser)
    }

    fun getFavoriteByUsername(login: String): LiveData<FavoriteUser> {
        return mFavoriteRepository.getFavoriteByUsername(login)
    }
}