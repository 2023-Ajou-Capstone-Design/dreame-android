package com.example.dreamixmlversion.ui.sharing.detail

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.databinding.ActivitySharingDetailBinding
import com.example.dreamixmlversion.ui.sharing.SharingViewModel
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySharingDetailBinding
    private val viewModel: SharingDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDetailLiveData()
    }

    private fun initDetailLiveData() {
        viewModel.queriedSharingDetailInfoLiveData.observe(this) {
            when (it) {
                SharingDetailUiState.Uninitialized -> callDetailInfo()
                SharingDetailUiState.Loading -> showProgressBar()
                is SharingDetailUiState.SuccessGetDetailInfo -> bindDetailInfo(it.detailInfoItem)
                SharingDetailUiState.Error -> TODO()
            }
        }
    }

    private fun callDetailInfo() {
        viewModel.getSharingDetailInfo(
            intent.getStringExtra(USER_ID).toString(),
            intent.getStringExtra(WRITING_ID).toString()
        )
    }

    private fun bindDetailInfo(sharingDetailInfoItem: SharingDetailInfo?) {
        hideProgressBar()
        with(binding) {
            // image viewPager
            sharingDetailInfoItem?.let { item ->

//                bindViewPagerImage()

                akaTextView.text = item.userId
                townTextView.text = item.town
                timeTextView.text = item.uploadTime
                sharingTitleTextView.text = item.title
                sharingContentTextView.text = item.contents
                // todo chat
            }
        }
    }

    private fun bindViewPagerImage(images: List<DetailImageItem>) {
        val detailFrameAdapter = DetailImageAdapter(images)

        with(binding) {
            imageViewPager.adapter = detailFrameAdapter

            TabLayoutMediator(
                tabLayout,
                imageViewPager
            ) { tab, position ->
                imageViewPager.currentItem = tab.position
            }.attach()
        }
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    companion object {
        const val WRITING_ID = "WRITING_ID"
        const val USER_ID = "USER_ID"
//        const val DETAIL_ENTITY = "DETAIL_ENTITY"
        const val RESULT_CODE = 10101
    }
}