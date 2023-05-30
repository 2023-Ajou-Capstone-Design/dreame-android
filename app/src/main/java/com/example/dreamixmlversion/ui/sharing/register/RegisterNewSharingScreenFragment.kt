package com.example.dreamixmlversion.ui.sharing.register

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentRegisterNewSharingBinding
import com.example.dreamixmlversion.extension.convertToBitmap
import com.example.dreamixmlversion.extension.toToast
import com.example.dreamixmlversion.ui.sharing.SharingBaseFragment
import com.example.dreamixmlversion.ui.sharing.SharingViewModel
import com.example.dreamixmlversion.ui.sharing.detail.DetailSharingScreenFragmentDirections
import com.example.dreamixmlversion.ui.user.UserFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterNewSharingScreenFragment : SharingBaseFragment<FragmentRegisterNewSharingBinding>() {

    private lateinit var registerImageAdapter: RegisterImageAdapter

    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.size > 3) {
                Toast.makeText(requireActivity(), "최대 3장까지 첨부 가능합니다.", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            updateImages(uriList)
        }

    override fun getViewBinding(): FragmentRegisterNewSharingBinding =
        FragmentRegisterNewSharingBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackButtonClicked()
        initRecyclerView()
        initGalleryImageButton()
        initRegisterSharingButton()
        bindData()
    }

    private fun onBackButtonClicked() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().popBackStack()
        }
    }

    private fun initRecyclerView() {
        registerImageAdapter = RegisterImageAdapter(
            deleteImageClickListener = {
                val list = arrayListOf<SharingImageItem>()
                list.addAll(registerImageAdapter.currentList)
                list.remove(it)
                registerImageAdapter.submitList(list)
            })
        _binding?.imageRecyclerView?.adapter = registerImageAdapter
    }

    private fun initGalleryImageButton() {
        _binding?.galleryImageButton?.setOnClickListener {
            checkExternalStoragePermission()
        }
    }

    private fun checkExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
                    loadImage()
                }
                shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_MEDIA_IMAGES
                ) -> {
                    showPermissionInfoDialog()
                }
                else -> {
                    requestReadExternalStorage()
                }
            }
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    loadImage()
                }
                shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
                    showPermissionInfoDialog()
                }
                else -> {
                    requestReadExternalStorage()
                }
            }
        }
    }

    private fun loadImage() {
        imageLoadLauncher.launch("image/*")
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                loadImage()
            }
        }.show()
    }

    private fun requestReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_EXTERNAL_STORAGE_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE_CODE
            )
        }
    }

    private fun updateImages(uriList: List<Uri>) {
        val images = uriList.map { SharingImageItem(it.convertToBitmap(requireActivity())) }
        val updatedImages =
            registerImageAdapter.currentList.toMutableList().apply { addAll(images) }
        registerImageAdapter.submitList(updatedImages)
    }

    private fun initRegisterSharingButton() {
        with(_binding) {
            this?.registerNewSharingButton?.setOnClickListener {
//                if (registerImageAdapter.currentList.size == 0) {
//                    ("사진을 최소 1장 첨부해주세요.").toToast(requireActivity())
//                    return@setOnClickListener
//                }
                if (titleEditTextView.text.toString().isEmpty()) {
                    ("제목을 적어주세요.").toToast(requireActivity())
                    return@setOnClickListener
                }
                if (contentEditTextView.text.toString().isEmpty()) {
                    ("내용을 적어주세요.").toToast(requireActivity())
                    return@setOnClickListener
                }

                if (sharingViewModel.modifyMode) {
                    sharingViewModel.modifySharing(
                        _binding?.titleEditTextView?.text.toString(),
                        _binding?.contentEditTextView?.text.toString(),
                        registerImageAdapter.currentList.toList(),
                        sharingViewModel.detailSharingWritingId
                    )
                    findNavController().navigate(R.id.action_popup_from_register_sharing_to_detail_sharing_screen)
                } else {
                    sharingViewModel.registerNewSharing(
                        titleEditTextView.text.toString(),
                        contentEditTextView.text.toString(),
                        registerImageAdapter.currentList.toList()
                    )
                    findNavController().navigate(R.id.action_pop_up_from_register_sharing_to_sharing_list)
                }
            }
        }
    }

    private fun bindData() {
        val sharingTitle = arguments?.getString("sharingTitle").toString()
        val sharingContent = arguments?.getString("sharingContent").toString()

        _binding?.titleEditTextView?.setText(sharingTitle)
        _binding?.contentEditTextView?.setText(sharingContent)
    }

    companion object {
        const val REQUEST_EXTERNAL_STORAGE_CODE = 10112
    }
}