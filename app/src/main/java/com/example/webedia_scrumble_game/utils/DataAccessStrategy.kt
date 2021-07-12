package com.example.webedia_scrumble_game.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import com.example.webedia_scrumble_game.utils.Resource.Status.*

/**
 * Launch a new IO coroutine to let use suspend functions from repository
 * And we store result in a LiveData holder observed by the user view model
 */
fun <T> performGetOperation(databaseQuery: () -> LiveData<T>): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)
    }

