package com.example.webedia_scrumble_game.data.repository

import com.example.webedia_scrumble_game.data.local.IUserDAO
import com.example.webedia_scrumble_game.data.model.UserModel
import com.example.webedia_scrumble_game.utils.performGetOperation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDAO: IUserDAO){
    fun getUser(userName: String) = performGetOperation { userDAO.getUser(userName) }

    suspend fun createUser(userName: String) {
        userDAO.insert(UserModel(userName, 0))
    }

    suspend fun updateUserLevel(userName: String, level: Int) {
        userDAO.update(UserModel(userName, level))
    }
}