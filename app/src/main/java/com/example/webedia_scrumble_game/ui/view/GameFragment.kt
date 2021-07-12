package com.example.webedia_scrumble_game.ui.view

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.webedia_scrumble_game.R
import com.example.webedia_scrumble_game.databinding.FragmentGameBinding
import com.example.webedia_scrumble_game.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {
    private lateinit var binding: FragmentGameBinding
    private var imageSplit: ArrayList<Bitmap>? = null
    private var answer: String? = null
    private var userName: String? = null
    private var userLevel: Int? = null
    private lateinit var adapter: SplitImageAdapter
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments
        // Get ArrayList<Bitmap> with scrambled image from activity
        imageSplit = args?.getParcelableArrayList(GameActivity.KEY_IMAGE)
        answer = args?.getString(GameActivity.KEY_ANSWER)
        userName = args?.getString(GameActivity.KEY_USER_NAME)
        userLevel = args?.getInt(GameActivity.KEY_USER_LEVEL)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.btnValidate.setOnClickListener {
            if (binding.etAnswer.text.isNullOrEmpty()) {
                Toast.makeText(context, getString(R.string.message_no_answer), Toast.LENGTH_SHORT).show()
            } else {
                // Compare with answer from proper level
                if (binding.etAnswer.text.toString() == answer) {
                    Toast.makeText(context, getString(R.string.message_good_answer), Toast.LENGTH_SHORT).show()

                    // Clear EditText
                    binding.etAnswer.setText("")

                    // Trigger victory animation
                    imageSplit?.let { it1 -> adapter.setOrderedDataList(it1) }
                    binding.rvScramble.adapter = adapter

                    // Recycler view animation
                    val layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
                    binding.rvScramble.layoutAnimation = layoutAnimation
                    binding.rvScramble.startLayoutAnimation()

                    // Call Handler to make delay to see the animation then update the user with next level
                    Handler().postDelayed({
                        // Clear previous data list
                        adapter.clearDataList()
                        // Update user
                        userName?.let { it1 -> userLevel?.let { it2 -> viewModel.updateUser(it1, it2.plus(1)) } }
                    }, 1500)
                } else {
                    Toast.makeText(context, getString(R.string.message_wrong_answer), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Init the recyclerview with scrambled images and trigger animation
     */
    private fun setupRecyclerView() {
        //Get the grid view and set an adapter to it
        adapter = context?.let { SplitImageAdapter(it) }!!
        imageSplit?.let { adapter.setScrambleDataList(it) }

        // Set the Layout Manager with 3 columns grid
        binding.rvScramble.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvScramble.adapter = adapter

        // Recycler view animation
        val layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_random)
        binding.rvScramble.layoutAnimation = layoutAnimation
        binding.rvScramble.startLayoutAnimation()
    }
}