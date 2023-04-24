package com.example.dreamixmlversion.ui.sharing.register

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dreamixmlversion.databinding.ActivityRegisterNewSharingBinding
import com.example.dreamixmlversion.ui.sharing.RegisterImageAdapter
import com.example.dreamixmlversion.ui.sharing.SharingImageItem
import com.example.dreamixmlversion.ui.sharing.detail.SharingDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterNewSharingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterNewSharingBinding
    private lateinit var registerImageAdapter: RegisterImageAdapter
    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList)
        }
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNewSharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.imageRecyclerView.adapter = registerImageAdapter
    }

    private fun initGalleryImageButton() {
        binding.galleryImageButton.setOnClickListener {
            checkExternalStoragePermission()
        }
    }

    private fun checkExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
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
                    this,
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
        AlertDialog.Builder(this).apply {
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun requestReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_EXTERNAL_STORAGE_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE_CODE -> {
                val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_GRANTED
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    private fun updateImages(uriList: List<Uri>) {
        val images = uriList.map { SharingImageItem(it) }
        val updatedImages =
            registerImageAdapter.currentList.toMutableList().apply { addAll(images) }
        registerImageAdapter.submitList(updatedImages)
    }

    private fun initRegisterSharingButton() {
        with(binding) {
            registerNewSharingButton.setOnClickListener {
                val images = registerImageAdapter.currentList.map { it.uri }
                val title = titleEditTextView.text.toString()
                val content = contentEditTextView.text.toString()
                viewModel.registerNewSharing(title, content)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    companion object {
        const val REQUEST_EXTERNAL_STORAGE_CODE = 2000
    }
}