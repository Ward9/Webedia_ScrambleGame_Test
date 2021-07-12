package com.example.webedia_scrumble_game.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.webedia_scrumble_game.R
import com.example.webedia_scrumble_game.data.model.GameModel
import com.example.webedia_scrumble_game.databinding.ActivityGameBinding
import com.example.webedia_scrumble_game.ui.viewmodel.UserViewModel
import com.example.webedia_scrumble_game.utils.Resource
import com.example.webedia_scrumble_game.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by viewModels()
    private var userLevel: Int = 0
    private lateinit var userName: String

    companion object {
        const val KEY_IMAGE = "SPLIT_IMG_KEY"
        const val KEY_ANSWER = "ANSWER"
        const val KEY_USER_NAME = "USERNAME"
        const val KEY_USER_LEVEL = "USER_LEVEL"
        lateinit var gamesList: MutableList<GameModel>

        // Use newInstance to pass data to GameFragment
        fun newInstance(
            imageSplit: MutableList<Bitmap>,
            answer: String,
            userName: String,
            userLevel: Int
        ): GameFragment {
            val args = Bundle()
            args.putParcelableArrayList(KEY_IMAGE, ArrayList(imageSplit))
            args.putString(KEY_ANSWER, answer)
            args.putString(KEY_USER_NAME, userName)
            args.putInt(KEY_USER_LEVEL, userLevel)
            val fragment = GameFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use Databinding to bind the view
        val binding: ActivityGameBinding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Username through intent
        val userNameExtra = intent.getStringExtra(WelcomeActivity.KEY_USER)

        // Associate drawable image with data model
        addResDrawableToGameModel(getJsonFromString())

        // Set the username in view model so we can get the proper user
        if (!userNameExtra.isNullOrEmpty() && viewModel.user.value?.data?.userName.isNullOrEmpty()) {
            userName = userNameExtra
            viewModel.initUser(userNameExtra)
        }

        // Observe any change in UserModel
        viewModel.user.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    // Get user current level using viewModel
                    userLevel = it.data?.level ?: 0

                    if (userLevel > 2) {
                        // User finish the game, call finish activity
                        val intent = Intent(this, FinishActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Get the proper image to split regarding UserModel level (0 = cat, 1 = droid, 2 = coffee)
                        val fullImageRes: Int =
                            gamesList[userLevel].resDrawable // Index --> User level (0, 1, 2)

                        // Open GameFragment passing MutableList<Bitmap> and the corresponding answer
                        supportFragmentManager.beginTransaction().add(
                            R.id.game_fragment,
                            newInstance(splitImage(fullImageRes), getAnswerFromLevel(userLevel), userName, userLevel)
                        ).addToBackStack(null).commit()
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        // Hack to disable backPressed on GameActivity
        //super.onBackPressed()
    }

    /**
     * Get data from data.json using gson
     */
    private fun getJsonFromString(): MutableList<GameModel> {
        val jsonFileString = Utils.getJsonDataFromAsset(applicationContext, "data.json")
        if (jsonFileString != null) {
            Log.i("data", jsonFileString)
        }

        val gson = Gson()
        val listGameType = object : TypeToken<List<GameModel>>() {}.type

        return gson.fromJson(jsonFileString, listGameType)
    }

    /**
     * Associate image from drawable res into each game model
     */
    private fun addResDrawableToGameModel(gamesList: MutableList<GameModel>) {
        for (item in gamesList) {
            when (item.iName) {
                "cat" -> item.resDrawable = R.drawable.cat
                "droid" -> item.resDrawable = R.drawable.droid
                "coffee" -> item.resDrawable = R.drawable.coffee
            }
        }

        GameActivity.gamesList = gamesList
    }

    /**
     * Get the proper answer associate to the level
     */
    private fun getAnswerFromLevel(level: Int): String {
        return gamesList[level].a
    }

    /**
     * Get image from drawable then split it in 9 bitmaps then return a MutableList<Bitmap>
     */
    private fun splitImage(imageRes: Int): MutableList<Bitmap> {
        val rows = 3
        val columns = 3

        // Size of split image part
        val splitImgHeight: Int
        val splitImgWidth: Int

        // Store in a list<Bitmap> split image
        val splitImages: MutableList<Bitmap> = arrayListOf()

        // Get image as bitmap
        val imgBitmap: Bitmap = BitmapFactory.decodeResource(baseContext.resources, imageRes)

        // Get image as bitmap but scale this time
        val imgBitmapScale: Bitmap =
            Bitmap.createScaledBitmap(imgBitmap, imgBitmap.width, imgBitmap.height, true)

        // Get imgHeight by dividing by num of rows 3 same for width
        splitImgHeight = imgBitmap.height / rows
        splitImgWidth = imgBitmap.width / columns

        // x & y coordinate are pixel positions of split image
        var yCoord = 0
        for (i in 0 until rows) {
            var xCoord = 0
            for (y in 0 until columns) {
                //if (xCoord > imgBitmapScale.width) xCoord = imgBitmapScale.width
                splitImages.add(
                    Bitmap.createBitmap(
                        imgBitmapScale,
                        xCoord,
                        yCoord,
                        splitImgWidth,
                        splitImgHeight
                    )
                )
                xCoord += splitImgWidth
            }
            yCoord += splitImgHeight
        }

        return splitImages
    }
}