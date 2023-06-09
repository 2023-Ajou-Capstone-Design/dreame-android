package com.example.dreamixmlversion.ui.sharing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.dreamixmlversion.R

abstract class SharingBaseFragment<VB: ViewBinding>: Fragment() {

    internal val sharingViewModel: SharingViewModel by activityViewModels()

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
        Log.d("destroyed!!", "destroyed!!")
    }
}