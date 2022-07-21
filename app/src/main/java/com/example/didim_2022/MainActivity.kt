package com.example.didim_2022

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.didim_2022.databinding.ActivityMainBinding
import com.example.didim_2022.ui.tutorial.Tutorial2Fragment
import com.example.didim_2022.ui.tutorial.Tutorial3Fragment
import com.example.didim_2022.ui.tutorial.Tutorial4Fragment
import com.example.didim_2022.ui.tutorial.Tutorial5Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val frame: ConstraintLayout by lazy {
        findViewById(R.id.main_container)
    }
    private val bottomNagivationView: BottomNavigationView by lazy {
        findViewById(R.id.bottom_nav)
    }

//    private val tutorialbtn: ImageView by lazy {
//        findViewById(R.id.main_tutorial_bg_iv)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //bottomnavigation
        supportFragmentManager.beginTransaction().add(frame.id, HomeFragment()).commit()

        bottomNagivationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_foot -> {
                    replaceFragment(FootFragment())
                    true
                }
                R.id.nav_detail -> {
                    replaceFragment(DetailFragment())
                    true
                }
                else -> false
            }
        }

    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()
    }

    fun changeFragment(index: Int){
        when(index){
            0 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial2Fragment())
            }
            3 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial3Fragment())
                    .commit()
            }

            4 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial4Fragment())
                    .commit()
            }

            5 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial5Fragment())
                    .commit()
            }

            else -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, HomeFragment())
                    .commit()
            }
        }
    }
}
