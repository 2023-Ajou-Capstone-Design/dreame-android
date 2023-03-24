package com.example.dreamixmlversion.fragmentUi.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentHomeBinding
import com.example.dreamixmlversion.databinding.FragmentUserBinding

class UserFragment: Fragment(R.layout.fragment_user) {

    companion object {
        const val TAG = "UserFragment"
    }

    private var binding: FragmentUserBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentUserBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

}