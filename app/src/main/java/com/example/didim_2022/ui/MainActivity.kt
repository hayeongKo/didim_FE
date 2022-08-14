package com.example.didim_2022.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.didim_2022.R
import com.example.didim_2022.databinding.ActivityMainBinding
import com.example.didim_2022.ui.tutorial.*
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

        //sensor


        //bottomnavigation
        supportFragmentManager.beginTransaction().add(frame.id, HomeFragment()).commit()

        bottomNagivationView.selectedItemId = R.id.nav_home

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

        //php url 연결

//        val test = "http://localhost/MediumServer/SelectAllPost.php";
//        val task = URLConnector(test);
//
//        task.start();
//
//        try{
//            task.join();
//            System.out.println("waiting... for result");
//        }
//        catch(InterruptedException e){
//
//        }
//
//        val result = task.getResult();
//
//        System.out.println(result);

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

            6 -> {
                val commit = supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial6Fragment())
                    .commit()
            }

            7 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial7Fragment())
                    .commit()
            }

            8 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial8Fragment())
                    .commit()
            }

            9 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial9Fragment())
                    .commit()
            }

            10 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial10Fragment())
                    .commit()
            }

            11 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial11Fragment())
                    .commit()
            }

            12 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial12Fragment())
                    .commit()
            }

            13 -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, Tutorial13Fragment())
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
