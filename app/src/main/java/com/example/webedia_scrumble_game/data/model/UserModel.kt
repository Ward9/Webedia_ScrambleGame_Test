package com.example.webedia_scrumble_game.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserModel (@PrimaryKey val userName: String, var level: Int){
}