package com.example.webedia_scrumble_game.utils

import android.content.Context
import java.io.IOException

class Utils {
    companion object {
        /**
         * Reading json file from assets folder
         */
        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                // Open the json file and set data into a string
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                // If it doesn't work, throw an IOException error
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }

}