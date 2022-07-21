package com.example.didim_2022

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import com.example.didim_2022.databinding.FragmentFootBinding
import com.example.didim_2022.databinding.FragmentHomeBinding

class FootFragment: Fragment() {
    lateinit var binding: FragmentFootBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFootBinding.inflate(inflater, container, false)

        return binding.root
    }

}