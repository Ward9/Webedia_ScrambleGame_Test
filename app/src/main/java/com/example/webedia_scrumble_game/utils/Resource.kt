package com.example.webedia_scrumble_game.utils

/**
 * It helps us encapsulate our repository responses according to their state (which is always success for RoomDb)
 * making it easy for our views to display information accordingly.
 * This is how our fragments observes a LiveData value and updates itself accordingly.
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
    }
}