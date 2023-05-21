package com.example.dreamixmlversion.ui.sharing.list

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.response.model.SharingDataItemEntity
import com.example.dreamixmlversion.databinding.FragmentSharingListBinding
import com.example.dreamixmlversion.ui.sharing.SharingBaseFragment
import com.example.dreamixmlversion.ui.sharing.uiState.SharingUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingListScreenFragment: SharingBaseFragment<FragmentSharingListBinding>() {

    private lateinit var sharingAdapter: SharingAdapter

    override fun getViewBinding(): FragmentSharingListBinding =
        FragmentSharingListBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            sharingListFragment = this@SharingListScreenFragment
            viewModel = sharingViewModel
        }

        onBackButtonClicked()
        setSharingType()
        initSwipeRefreshLayout()
        initRecyclerView()
        initFloatingButton()
    }

    private fun onBackButtonClicked() {
        requireActivity().onBackPressedDispatcher.addCallback {
//            clearSharingData()
            findNavController().popBackStack()
        }
    }

//    private fun setSharingUiStateTo(uiState: SharingUiState) {
//        sharingViewModel.setSharingUiStateTo(uiState)
//    }

    private fun clearSharingData() {
        sharingViewModel.initSharingListLivedata()
    }

    private fun setSharingType() {
        val sharingType = arguments?.getString("sharingType").toString()
        sharingViewModel.setSharingType(sharingType)
    }

    private fun initSwipeRefreshLayout() = with(_binding) {
        this?.refreshLayout?.setOnRefreshListener {
            clearSharingData()
            sharingViewModel.getSharingItems()
        }
    }

    private fun initRecyclerView() {

        sharingAdapter = SharingAdapter {
            sharingViewModel.setDetailSharingInfo(it.userId, it.writingId)
            findNavController().navigate(R.id.action_sharing_screen_to_detail_sharing)
        }
        _binding?.sharingRecyclerView?.adapter = sharingAdapter

        sharingViewModel.queriedSharingItemLiveData.observe(requireActivity()) {
            when (it) {
                SharingUiState.Uninitialized -> callSharingInTown()
                SharingUiState.Loading -> showProgressBar()
                is SharingUiState.SuccessGetSharingItems -> bindSharingDataOnRecyclerView(it.sharingItems)
                SharingUiState.Error -> TODO()
            }
        }
    }

    private fun callSharingInTown() {
        sharingViewModel.getSharingItems()
    }

    private fun showProgressBar() {
        _binding?.refreshLayout?.isRefreshing = true
    }

    private fun hideProgressBar() {
        _binding?.refreshLayout?.isRefreshing = false
    }

    private fun bindSharingDataOnRecyclerView(items: List<SharingDataItemEntity>) {
        hideProgressBar()
        if (sharingAdapter.itemCount != 0) {
            sharingAdapter.submitList(null)
        }
        sharingAdapter.submitList(items)
    }

    private fun initFloatingButton() {
        _binding?.addSharingFloatingButton?.setOnClickListener {
            findNavController().navigate(R.id.action_sharing_list_screen_to_register_new_sharing_screen)
        }
    }
}