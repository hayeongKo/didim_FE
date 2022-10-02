package com.example.didim_2022.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.didim_2022.R
import com.example.didim_2022.databinding.FragmentHomeBinding
import com.example.didim_2022.ui.tutorial.Tutorial2Fragment
import getBad
import getCount
import getGood
import getMiss
import getPerfect
import kotlin.math.roundToInt

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding

    //private val sharedManager : SharedManager by lazy { SharedManager(this) }

    private var count: Int? = null
    private var miss: Int? = null
    private var bad: Int? = null
    private var good: Int? = null
    private var perfect: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sharedManager.getCurrentSensor()
        arguments?.let {
            count = it.getInt("count")
            miss = it.getInt("miss")
            bad = it.getInt("bad")
            good = it.getInt("good")
            perfect = it.getInt("perfect")
        }
        Log.d("spf", "spf: " + getCount(requireContext()) +"/"+ getPerfect(requireContext()) +"/"+ getGood(requireContext()) +"/"+ getBad(requireContext()) +"/"+ getMiss(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTutorialBgIv.setOnClickListener {
            changeFragment(Tutorial2Fragment())
        }

        binding.homeWalkingStartTv.setOnClickListener {
            val intent = Intent(context, FootActivity2::class.java)
            startActivity(intent)
        }

        binding.homeCountTv.setText("${getCount(requireContext())}")
        binding.homeComboPerfectTimesTv.setText("${getPerfect(requireContext())}")
        binding.homeComboGoodTimesTv.setText("${getGood(requireContext())}")
        binding.homeComboBadTimesTv.setText("${getBad(requireContext())}")
        binding.homeComboMissTimesTv.setText("${getMiss(requireContext())}")
        val toInt = getCount(requireContext()).toInt()*0.74
        binding.homeRunningdstTv.setText("${toInt.roundToInt()}")
        return binding.root
    }

    private fun changeFragment(fragment: Fragment) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment).commit()
    }
}