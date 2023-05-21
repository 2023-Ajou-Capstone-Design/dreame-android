package com.example.dreamixmlversion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dreamixmlversion.ui.chat.chatlist.ChatListFragment
import com.example.dreamixmlversion.ui.home.HomeFragment
import com.example.dreamixmlversion.ui.login.LoginActivity
import com.example.dreamixmlversion.ui.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLogin()
        initNavigation()
    }

    private fun checkLogin() {

        if (viewModel.checkLogin() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home_dest || destination.id == R.id.chat_dest || destination.id == R.id.user_dest) {
                bottomNavigationView.visibility = View.VISIBLE
            } else {
                bottomNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainNavigationHostContainer)

//        val currentDestinationId =  navController.currentDestination?.id
//        val navGraph = navController.graph
//        val navDestination = navGraph.findNode(currentDestinationId!!)
//        val label = navDestination?.label
//
//        return when(label) {
//            "Chat" -> {
//                navController.popBackStack()
//                true
//            }
//            "Sharing" -> {
//                navController.popBackStack()
//                true
//            }
//            else -> navController.navigateUp(appBarConfiguration)
//        }

        return navController.navigateUp(appBarConfiguration)
    }
}