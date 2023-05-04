package com.example.dreamixmlversion.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding

abstract class UserBaseFragment<VB: ViewBinding>: Fragment() {

    internal val userViewModel: UserViewModel by activityViewModels()

    internal var _binding: VB? = null
    private val binding get() = _binding!!

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}