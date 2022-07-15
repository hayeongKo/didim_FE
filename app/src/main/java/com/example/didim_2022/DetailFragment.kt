package com.example.didim_2022

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.didim_2022.databinding.FragmentDetailBinding
import com.example.didim_2022.databinding.FragmentHomeBinding
import com.example.didim_2022.ui.detail_viewpager.DetailViewpagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment: Fragment() {

    lateinit var binding: FragmentDetailBinding

    val tabTextList = arrayListOf("일", "주", "월")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val detailAdapter = DetailViewpagerAdapter(this)
        binding.detailContentVp.adapter = detailAdapter
        TabLayoutMediator(binding.detailContentTb, binding.detailContentVp){
            tab, position ->
            tab.text = tabTextList[position]
        }.attach()


        return binding.root
    }

}