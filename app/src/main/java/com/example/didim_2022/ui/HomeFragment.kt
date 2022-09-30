package com.example.didim_2022.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.example.didim_2022.R
import com.example.didim_2022.databinding.FragmentHomeBinding
import com.example.didim_2022.ui.tutorial.Tutorial2Fragment

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding

    private var count: Int? = null
    private var score: String? = null
    private var ajudge: String? = null
    private var miss: Int? = null
    private var bad: Int? = null
    private var good: Int? = null
    private var perfect: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTutorialBgIv.setOnClickListener {
            changeFragment(Tutorial2Fragment())
        }
        binding.homeCountTv.setText("${count}")
        binding.homeComboTv.setText("${score}")
        binding.homeAjudgeTv.setText("${ajudge}")
        binding.homeComboPerfectTimesTv.setText("${perfect}")
        binding.homeComboGoodTimesTv.setText("${good}")
        binding.homeComboBadTimesTv.setText("${bad}")
        binding.homeComboMissTimesTv.setText("${miss}")
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            count = it.getInt("count")
            score = it.getString("score")
            ajudge = it.getString("ajudge")
            miss = it.getInt("miss")
            bad = it.getInt("bad")
            good = it.getInt("good")
            perfect = it.getInt("perfect")
        }
    }

    private fun changeFragment(fragment: Fragment) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment).commit()
    }
}