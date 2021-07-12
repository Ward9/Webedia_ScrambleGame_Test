package com.example.webedia_scrumble_game.ui.viewmodel

import androidx.lifecycle.*
import com.example.webedia_scrumble_game.data.model.UserModel
import com.example.webedia_scrumble_game.data.repository.UserRepository
import com.example.webedia_scrumble_game.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Use this class to get GameModel associated to user
 */
@HiltViewModel
class UserViewModel @Inject constructor (private val repository: UserRepository) : ViewModel(){
    private val _userName = MutableLiveData<String>()

    private val userMap = _userName.switchMap { id -> repository.getUser(id) }
    val user: LiveData<Resource<UserModel>> = userMap

    fun initUser(userName: String) {
        // Init the user name
        _userName.value =  userName
    }

    fun createUser(username: String) {
        // Set current user and create it into data base
        viewModelScope.launch { repository.createUser(username) }
    }

    fun updateUser(username: String, level: Int) {
        // Update level from current user
        viewModelScope.launch { repository.updateUserLevel(username, level) }
    }
}