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
import java.time.LocalDate
import kotlin.math.roundToInt

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding

    //val localDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.homeCircleIv.setOnClickListener {
            val intent = Intent(context, FootActivity2::class.java)
            startActivity(intent)
        }

        binding.homeCountTv.setText("${getCount(requireContext())}")
        binding.homeComboPerfectTimesTv.setText("${getPerfect(requireContext())}번")
        binding.homeComboGoodTimesTv.setText("${getGood(requireContext())}번")
        binding.homeComboBadTimesTv.setText("${getBad(requireContext())}번")
        binding.homeComboMissTimesTv.setText("${getMiss(requireContext())}번")
        binding.homeRunningdstTv.setText("${getCount(requireContext())*0.74}")
        return binding.root
    }

    private fun changeFragment(fragment: Fragment) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment).commit()
    }
}