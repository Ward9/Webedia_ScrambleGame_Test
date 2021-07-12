package com.example.webedia_scrumble_game.ui.view

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.webedia_scrumble_game.R
import com.example.webedia_scrumble_game.databinding.ActivityGameBinding
import com.example.webedia_scrumble_game.databinding.ActivityWelcomeBinding
import com.example.webedia_scrumble_game.ui.viewmodel.UserViewModel
import com.example.webedia_scrumble_game.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity: AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()

    companion object {
        const val KEY_USER = "USERNAME"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use Databinding to bind the view
        val binding: ActivityWelcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get username and store it into database
        binding.btnValidate.setOnClickListener {
            if (binding.etUsername.text.isNullOrEmpty()) {
                Toast.makeText(baseContext, "You must write your username", Toast.LENGTH_SHORT).show()
            } else {
                // Insert new user to database
                val userName = binding.etUsername.text.toString()

                // Init user with entered username to know if we need to create it into db or retrieve data from existing user
                viewModel.initUser(userName)

                viewModel.user.observe(this, Observer {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            if (it.data == null) {
                                // User doesn't not exist, create it into database
                                viewModel.createUser(userName)
                            } else {
                                if (it.data.level > 2) {
                                    // User already finished the game, start finish activity
                                    val intent = Intent(this, FinishActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // User exists launch GameActivity
                                    val intent = Intent(this, GameActivity::class.java).apply {
                                        putExtra(KEY_USER, userName)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                })
            }
        }
    }
}