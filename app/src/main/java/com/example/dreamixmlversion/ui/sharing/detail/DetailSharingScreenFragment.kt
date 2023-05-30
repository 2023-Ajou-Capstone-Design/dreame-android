package com.example.dreamixmlversion.ui.sharing.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.response.model.SharingDetailInfo
import com.example.dreamixmlversion.databinding.FragmentSharingDetailBinding
import com.example.dreamixmlversion.ui.sharing.SharingBaseFragment
import com.example.dreamixmlversion.ui.sharing.uiState.SharingDetailUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailSharingScreenFragment : SharingBaseFragment<FragmentSharingDetailBinding>() {

    override fun getViewBinding(): FragmentSharingDetailBinding =
        FragmentSharingDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackButtonClicked()
        initDetailLiveData()
    }

    private fun onBackButtonClicked() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }
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
            sharingViewModel.detailSharingUserId,
            sharingViewModel.detailSharingWritingId
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

                this?.userIdTextView?.text = item.userId
                this?.akaTextView?.text = item.aka
                initBottomButton(item.userId)
                this?.townTextView?.text = item.town
                this?.timeTextView?.text = item.uploadTime
                this?.sharingTitleTextView?.text = item.title
                this?.sharingContentTextView?.text = item.contents
            }
        }
    }

    private fun initBottomButton(sharingUserId: String) {
        if (sharingViewModel.isSameWriter(sharingUserId)) {
            _binding?.modifySharingLayout?.visibility = View.VISIBLE
            _binding?.chatButton?.visibility = View.INVISIBLE
            initModifySharingButton()
            initDeleteSharingButton()
        } else {
            _binding?.modifySharingLayout?.visibility = View.INVISIBLE
            _binding?.chatButton?.visibility = View.VISIBLE
            initChatButton()
        }
    }

    private fun initChatButton() {
        _binding?.chatButton?.setOnClickListener {
            val otherNickname = _binding?.akaTextView?.text.toString()
            val action = DetailSharingScreenFragmentDirections.actionDetailSharingScreenToChatDetailScreen(otherNickname)
            findNavController().navigate(action)
        }
    }

    private fun initModifySharingButton() {
        _binding?.modifySharingButton?.setOnClickListener {
            sharingViewModel.modifyMode = true
            val sharingTitle = _binding?.sharingTitleTextView?.text.toString()
            val sharingContent = _binding?.sharingContentTextView?.text.toString()
            val action = DetailSharingScreenFragmentDirections.actionDetailSharingScreenToRegisterSharing(sharingTitle, sharingContent)
            findNavController().navigate(action)
        }
    }

    private fun initDeleteSharingButton() {
        _binding?.deleteSharingButton?.setOnClickListener {
            sharingViewModel.deleteSharing()
            findNavController().navigate(R.id.action_popup_from_detail_sharing_screen_to_sharing_list_screen)
        }
    }
}