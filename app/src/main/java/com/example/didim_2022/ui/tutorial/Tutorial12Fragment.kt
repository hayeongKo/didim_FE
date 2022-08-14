package com.example.didim_2022.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.didim_2022.databinding.FragmentTutorial12Binding
import com.example.didim_2022.ui.MainActivity
import com.example.didim_2022.databinding.FragmentTutorial5Binding
import com.example.didim_2022.databinding.FragmentTutorial7Binding

class Tutorial12Fragment: Fragment(){
    lateinit var binding: FragmentTutorial12Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorial12Binding.inflate(inflater, container, false)

        val mActivity = activity as MainActivity

        binding.tutorialFootIv.setOnClickListener {
            mActivity.changeFragment(13)
        }

        return binding.root
    }
}