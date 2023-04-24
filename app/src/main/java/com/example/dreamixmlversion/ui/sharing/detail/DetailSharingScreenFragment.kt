package com.example.dreamixmlversion.ui.sharing.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.dreamixmlversion.data.api.response.entity.SharingDetailInfo
import com.example.dreamixmlversion.databinding.FragmentSharingDetailBinding
import com.example.dreamixmlversion.ui.sharing.SharingBaseFragment
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSharingScreenFragment: SharingBaseFragment<FragmentSharingDetailBinding>() {

    override fun getViewBinding(): FragmentSharingDetailBinding =
        FragmentSharingDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDetailLiveData()
    }

    private fun initDetailLiveData() {
        sharingViewModel.queriedSharingDetailInfoLiveData.observe(requireActivity()) {
            when (it) {
                SharingDetailUiState.Uninitialized -> callDetailInfo()
                SharingDetailUiState.Loading -> showProgressBar()
                is SharingDetailUiState.SuccessGetDetailInfo -> bindDetailInfo(it.detailInfoItem)
                SharingDetailUiState.Error -> TODO()
            }
        }
    }

    private fun callDetailInfo() {
        sharingViewModel.getSharingDetailInfo(
            "Tester",
            sharingViewModel.detailSharingWritingId.value.toString()
        )
    }

    private fun showProgressBar() {
        _binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        _binding?.progressBar?.isVisible = false
    }

    private fun bindDetailInfo(sharingDetailInfoItem: SharingDetailInfo?) {
        hideProgressBar()
        with(_binding) {
            // image viewPager
            sharingDetailInfoItem?.let { item ->

//                bindViewPagerImage()

                this?.akaTextView?.text = item.userId
                this?.townTextView?.text = item.town
                this?.timeTextView?.text = item.uploadTime
                this?.sharingTitleTextView?.text = item.title
                this?.sharingContentTextView?.text = item.contents
                // todo chat
            }
        }
    }
}