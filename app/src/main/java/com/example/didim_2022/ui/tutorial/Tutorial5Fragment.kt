package com.example.didim_2022.ui.tutorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.didim_2022.MainActivity
import com.example.didim_2022.databinding.FragmentTutorial5Binding

class Tutorial5Fragment: Fragment(){
    lateinit var binding: FragmentTutorial5Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorial5Binding.inflate(inflater, container, false)

        val mActivity = activity as MainActivity

        binding.tutorialFootIv.setOnClickListener {
            mActivity.changeFragment(6)
        }

        return binding.root
    }
}