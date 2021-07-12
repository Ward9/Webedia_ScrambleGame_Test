package com.example.webedia_scrumble_game.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.webedia_scrumble_game.data.model.GameModel
import com.example.webedia_scrumble_game.data.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Use Room db to build local db (in case we have no network connection)
 */
@Database(entities = [UserModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): IUserDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "user.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}