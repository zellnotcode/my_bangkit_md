package com.zell.submissionsatufundamental

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user ORDER BY login ASC")
    fun getAllFavorite(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favorite_user WHERE login = :login")
    fun getFavoriteUserByUsername(login: String): LiveData<FavoriteUser>
}