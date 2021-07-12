package com.example.webedia_scrumble_game.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.webedia_scrumble_game.data.model.UserModel
import kotlinx.coroutines.flow.Flow

/**
 * Store and get data from local db using Room and LiveData (Observe values from db)
 * Kotlin Coroutines make it easier by letting us suspend our Room functions
 */
@Dao
interface IUserDAO {
    @Query("select * FROM user WHERE userName = :name")
    fun getUser(name: String) : LiveData<UserModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserModel)

    @Update
    suspend fun update(user: UserModel)
}