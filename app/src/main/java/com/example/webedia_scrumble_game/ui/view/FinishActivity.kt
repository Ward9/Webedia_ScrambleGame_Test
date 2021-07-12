package com.example.webedia_scrumble_game.ui.view


import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.webedia_scrumble_game.R
import com.example.webedia_scrumble_game.databinding.ActivityFinishBinding


class FinishActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use Databinding to bind the view
        val binding: ActivityFinishBinding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start animation using animated vector drawable
        (binding.ivAnimated.drawable as? Animatable)?.start()

        binding.ivAnimated.setOnClickListener {
            animateCheckView(binding.ivAnimated)
        }

        Toast.makeText(this, getString(R.string.message_checkmark), Toast.LENGTH_SHORT).show()
    }

    /**
     * Launch vector drawable animation
     */
    private fun animateCheckView(view: ImageView) {
        when (val drawable = view.drawable) {
            is AnimatedVectorDrawableCompat -> {
                drawable.start()
            }
            is AnimatedVectorDrawable -> {
                drawable.start()
            }
        }
    }

    override fun onBackPressed() {
        // Hack to disable backPressed on GameActivity
        //super.onBackPressed()
    }
}