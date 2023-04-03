package com.example.dreamixmlversion.ui.sharing

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.databinding.ActivitySharingBinding
import com.example.dreamixmlversion.ui.sharing.uiState.SharingUiState

class SharingActivity : AppCompatActivity() {

    lateinit var binding: ActivitySharingBinding
    lateinit var sharingAdapter: SharingAdapter
    private val viewModel: SharingViewModel by viewModels()
//    private val startForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == SharingDetailActivity.RESULT_CODE) {
//
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initFloatingButton()
    }

    private fun initRecyclerView() {
        sharingAdapter = SharingAdapter {
            startActivity(Intent(this, SharingDetailActivity::class.java).apply {
                putExtra(SharingDetailActivity.WRITING_ID, it.writingId)
                putExtra(SharingDetailActivity.USER_ID, it.userId)
            })
        }
        binding.sharingRecyclerView.adapter = sharingAdapter

        viewModel.queriedSharingItemLiveData.observe(this) {
            when (it) {
                SharingUiState.Uninitialized -> callSharing()
                SharingUiState.Loading -> showProgressBar()
                is SharingUiState.SuccessGetSharingItems -> bindSharingDataOnRecyclerView(it.sharingItems)
                SharingUiState.Error -> TODO()
            }
        }
    }

    private fun callSharing() {
        viewModel.getSharingItemsNearbyUser(
            DreameLatLng(13.00, 23.00), 1000
        )
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun bindSharingDataOnRecyclerView(items: List<SharingDataItemEntity>) {
        sharingAdapter.submitList(items)
    }

    private fun initFloatingButton() {
        binding.addSharingFloatingButton.setOnClickListener {

        }
    }
}