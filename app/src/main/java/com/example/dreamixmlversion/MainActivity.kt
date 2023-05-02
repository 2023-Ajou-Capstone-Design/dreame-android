package com.example.dreamixmlversion

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dreamixmlversion.ui.chat.ChatFragment
import com.example.dreamixmlversion.ui.home.HomeFragment
import com.example.dreamixmlversion.ui.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

//    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_main)

//        startActivity(Intent(this, LoginActivity::class.java))
//        initFragments()
        initNavigation()
    }

    private fun initNavigation() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.mainNavigationHostContainer
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest, R.id.chat_dest, R.id.user_dest)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
//        return findNavController(R.id.mainNavigationHostContainer).navigateUp(appBarConfiguration)
    }

    private fun initFragments() {
        val homeFragment = HomeFragment()
        val chatFragment = ChatFragment()
        val userFragment = UserFragment()

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_dest -> {
                    replaceFragment(homeFragment, HomeFragment.TAG)
                    return@setOnItemSelectedListener true
                }
                R.id.chat_dest -> {
                    replaceFragment(chatFragment, ChatFragment.TAG)
                    return@setOnItemSelectedListener true
                }
                R.id.user_dest -> {
                    replaceFragment(userFragment, UserFragment.TAG)
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }

        replaceFragment(HomeFragment(), HomeFragment.TAG)
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainNavigationHostContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}