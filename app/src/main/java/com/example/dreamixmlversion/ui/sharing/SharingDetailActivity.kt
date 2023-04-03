package com.example.dreamixmlversion.ui.sharing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.databinding.ActivitySharingDetailBinding
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState

class SharingDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySharingDetailBinding
    private val viewModel: SharingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDetailLiveData()
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    private fun callDetailInfo() {
        viewModel.getSharingDetailInfo(
            intent.getStringExtra(USER_ID).toString(),
            intent.getStringExtra(WRITING_ID).toString()
        )
    }

    private fun bindDetailInfo(sharingDetailInfoItem: SharingDetailInfo) {
        with(binding) {
            // image viewPager
            akaTextView.text = sharingDetailInfoItem.aka
            townTextView.text = sharingDetailInfoItem.town
            timeTextView.text = sharingDetailInfoItem.uploadTime
            sharingTitleTextView.text = sharingDetailInfoItem.title
            sharingContentTextView.text = sharingDetailInfoItem.content
            // todo chat
        }
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

    companion object {
        const val WRITING_ID = "WRITING_ID"
        const val USER_ID = "USER_ID"
//        const val DETAIL_ENTITY = "DETAIL_ENTITY"
        const val RESULT_CODE = 10101
    }
}