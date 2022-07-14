package com.example.didim_2022.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.didim_2022.MainActivity
import com.example.didim_2022.R
import com.example.didim_2022.databinding.FragmentTutorial2Binding

class Tutorial2Fragment: Fragment() {

    lateinit var binding: FragmentTutorial2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorial2Binding.inflate(inflater, container, false)

        val mActivity = activity as MainActivity

        binding.tutorialFootIv.setOnClickListener {
            mActivity.changeFragment(3)
        }

        return binding.root
    }

}