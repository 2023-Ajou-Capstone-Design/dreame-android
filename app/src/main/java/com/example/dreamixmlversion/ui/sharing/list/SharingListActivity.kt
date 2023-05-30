package com.example.dreamixmlversion.ui.sharing.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamixmlversion.databinding.ActivitySharingListBinding
import com.example.dreamixmlversion.ui.map.StoreViewModel
import com.example.dreamixmlversion.ui.sharing.SharingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingListActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySharingListBinding
    private val viewModel: SharingViewModel by viewModels()
    private lateinit var sharingAdapter: SharingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        onBackButtonClicked()
//        setSharingType()
//        initSwipeRefreshLayout()
//        initRecyclerView()
//        initFloatingButton()
    }
}