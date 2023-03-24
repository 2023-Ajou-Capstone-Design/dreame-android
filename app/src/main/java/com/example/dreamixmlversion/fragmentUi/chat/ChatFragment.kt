package com.example.dreamixmlversion.fragmentUi.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentChatBinding
import com.example.dreamixmlversion.databinding.FragmentHomeBinding

class ChatFragment: Fragment(R.layout.fragment_chat) {

    companion object {
        const val TAG = "ChatFragment"
    }

    private var binding: FragmentChatBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentChatBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root
}