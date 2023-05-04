package com.example.dreamixmlversion.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentHomeBinding
import com.example.dreamixmlversion.ui.map.MapActivity
import com.example.dreamixmlversion.ui.sharing.SharingActivity

class HomeFragment: Fragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            openSpotDataButton.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, MapActivity::class.java))
                }
            }
            openFoodShareButton.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, SharingActivity::class.java))
                }
            }
        }
    }
}