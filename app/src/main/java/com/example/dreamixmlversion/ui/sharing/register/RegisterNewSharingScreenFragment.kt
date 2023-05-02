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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentRegisterNewSharingBinding
import com.example.dreamixmlversion.ui.sharing.SharingBaseFragment
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

        initRecyclerView()
        initGalleryImageButton()
        initRegisterSharingButton()
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
        val images = uriList.map { SharingImageItem(convertToBitmap(it)) }
        val updatedImages =
            registerImageAdapter.currentList.toMutableList().apply { addAll(images) }
        registerImageAdapter.submitList(updatedImages)
    }

    private fun convertToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        }
    }

    private fun initRegisterSharingButton() {
        with(_binding) {
            this?.registerNewSharingButton?.setOnClickListener {
                if (registerImageAdapter.currentList.size == 0) {
                    alertByToast("사진을 최소 1장 첨부해주세요.")
                    return@setOnClickListener
                }
                if (titleEditTextView.text.toString().isEmpty()) {
                    alertByToast("제목을 적어주세요.")
                    return@setOnClickListener
                }
                if (contentEditTextView.text.toString().isEmpty()) {
                    alertByToast("내용을 적어주세요.")
                    return@setOnClickListener
                }
                sharingViewModel.registerNewSharing(
                    titleEditTextView.text.toString(),
                    contentEditTextView.text.toString()
                )
                findNavController().navigate(R.id.action_popup_to_sharing_list_screen)
            }
        }
    }

    private fun alertByToast(content: String) =
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()

    private fun checkRegister() {

    }

    companion object {
        const val REQUEST_EXTERNAL_STORAGE_CODE = 10112
    }
}