package com.example.dreamixmlversion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dreamixmlversion.databinding.ActivityMainBinding
import com.example.dreamixmlversion.ui.chat.ChatFragment
import com.example.dreamixmlversion.ui.home.HomeFragment
import com.example.dreamixmlversion.ui.user.UserFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragments()
    }

    private fun initFragments() {
        val homeFragment = HomeFragment()
        val chatFragment = ChatFragment()
        val userFragment = UserFragment()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_dest -> replaceFragment(homeFragment, HomeFragment.TAG)
                R.id.chat_dest -> replaceFragment(chatFragment, ChatFragment.TAG)
                R.id.user_dest -> replaceFragment(userFragment, UserFragment.TAG)
            }
            true
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