package com.example.didim_2022

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.didim_2022.databinding.FragmentHomeBinding
import com.example.didim_2022.ui.tutorial.Tutorial2Fragment

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTutorialBgIv.setOnClickListener {
            changeFragment(Tutorial2Fragment())
        }

        return binding.root
    }

    private fun changeFragment(fragment: Fragment) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment).commit()
    }
}