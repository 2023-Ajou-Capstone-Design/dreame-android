package com.example.dreamixmlversion.ui.sharing.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.response.entity.SharingDataItemEntity
import com.example.dreamixmlversion.databinding.FragmentSharingListBinding
import com.example.dreamixmlversion.ui.sharing.SharingAdapter
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

        initSwipeRefreshLayout()
        initRecyclerView()
        initFloatingButton()
    }

    private fun initSwipeRefreshLayout() = with(_binding) {
        this?.refreshLayout?.setOnRefreshListener {
            sharingViewModel.getSharingItemsNearbyUser("수원시 영통구 원천동")
        }
    }

    private fun initRecyclerView() {
        sharingAdapter = SharingAdapter {
            sharingViewModel.setDetailSharingWritingId(it.writingId)
            findNavController().navigate(R.id.action_sharing_list_screen_to_detail_screen)
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
        sharingViewModel.getSharingItemsNearbyUser("수원시 영통구 원천동")
    }

    private fun showProgressBar() {
        _binding?.refreshLayout?.isRefreshing = true
    }

    private fun hideProgressBar() {
        _binding?.refreshLayout?.isRefreshing = false
    }

    private fun bindSharingDataOnRecyclerView(items: List<SharingDataItemEntity>) {
        hideProgressBar()
        sharingAdapter.submitList(items)
    }

    private fun initFloatingButton() {
        _binding?.addSharingFloatingButton?.setOnClickListener {
            findNavController().navigate(R.id.action_sharing_list_screen_to_register_new_sharing_screen)
        }
    }
}