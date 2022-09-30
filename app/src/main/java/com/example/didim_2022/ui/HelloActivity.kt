package com.example.didim_2022.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.didim_2022.databinding.ActivityHelloBinding

class HelloActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHelloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}