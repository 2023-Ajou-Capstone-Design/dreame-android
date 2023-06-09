package com.example.dreamixmlversion.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.response.model.DetailInfoItem
import com.example.dreamixmlversion.data.api.response.model.StoreDataForMarking
import com.example.dreamixmlversion.data.api.response.model.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.CategoryEntity
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.databinding.ActivityMapBinding
import com.example.dreamixmlversion.ui.map.uistate.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding

    private lateinit var dreameLatLng: DreameLatLng

    lateinit var storeAdapter: StoreAdapter
    lateinit var categoryAdapter: CategoryAdapter

    private lateinit var bottomSheetStoreListBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetStoreList by lazy {
        findViewById<ConstraintLayout>(R.id.bottomSheetStoreList)
    }

    private lateinit var bottomSheetDetailBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetDetail by lazy {
        findViewById<ConstraintLayout>(R.id.bottomSheetDetail)
    }

    private val bottomSheetStoreListProgressBar: ProgressBar by lazy {
        findViewById(R.id.bottomSheetStoreListProgressBar)
    }
    private val bottomSheetDetailProgressBar: ProgressBar by lazy {
        findViewById(R.id.bottomSheetDetailProgressBar)
    }

    private lateinit var naverMap: NaverMap

    private lateinit var markers: List<Marker>

    private val viewModel: StoreViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                checkLocationPermission()
            }
            else -> {
                showPermissionInfoDialog()
            }
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.getMapAsync(this)
        binding.mapView.onCreate(savedInstanceState)

        initSearchEditTextView()
        initCategory()
        initBottomSheetStoreList()
        initBottomSheetDetailDialog()
        initFavoriteButton()
        initTrackCurrentPosButton()
        initMarkingOnMap()
    }

    private fun initSearchEditTextView() {
        binding.searchEditTextView.apply {
            setOnEditorActionListener { editText, _, _ ->

                currentFocus?.let { view ->
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)

                    viewModel.getStoresBySearchingKeyword(editText.text.toString(), dreameLatLng, 5000)
                }

                clearFocus()

                return@setOnEditorActionListener true
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    if (storeAdapter.itemCount != 0) {
                        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        }
    }

    private fun initCategory() {
        categoryAdapter = CategoryAdapter()
        categoryAdapter.submitList(CategoryEntity.getMainCategories())
        categoryAdapter.setOnCategoryClickListener {

            viewModel.getStoresByClickedCategory(
                path = it.path,
                category = it.category,
                subCategory = it.subCategory,
                storeType = it.storeType,
                latLng = dreameLatLng,
                mbr = 5000
            )

            bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initFavoriteButton() {
        binding.favoritesImageButton.setOnClickListener {
            viewModel.getFavoriteStores() // todo userId 기입
        }
    }

    @SuppressLint("MissingPermission")
    private fun initTrackCurrentPosButton() {
        binding.trackCurrentPosButton.setOnClickListener {
            requestStoresByCurrentPos()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    private fun initBottomSheetStoreList() {
        storeAdapter = StoreAdapter()

        storeAdapter.setOnStoreClickListener {
            bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            moveToPos(it.storePointLat.toDouble(), it.storePointLng.toDouble())
            viewModel.getDetailStoreInfo(it.storeId, it.storeType)
        }

        bottomSheetStoreListBehavior = BottomSheetBehavior.from(bottomSheetStoreList)
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.queriedStoresOnBottomSheetListLiveData.observe(this) {
            when (it) {
                is BottomSheetListUiState.Uninitialized -> {}
                is BottomSheetListUiState.Loading -> showProgressBarInBottomSheetStoreList()
                is BottomSheetListUiState.SuccessGetStoresOnBottomSheetList -> spreadStoresInBottomSheet(
                    it.stores
                )
                is BottomSheetListUiState.Error -> {}
            }
        }
    }

    private fun showProgressBarInBottomSheetStoreList() {
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetStoreListProgressBar.isVisible = true
    }

    private fun hideProgressBarInBottomSheet() {
        bottomSheetStoreListProgressBar.isVisible = false
    }


    private fun spreadStoresInBottomSheet(stores: List<StoreDataOnBottomSheetList>) {
        hideProgressBarInBottomSheet()
        clearMarkers()

        val recyclerView = findViewById<RecyclerView>(R.id.bottomSheetStoreRecyclerView)
        recyclerView.adapter = storeAdapter

        storeAdapter.submitList(stores)
        markStoresOnMap(stores.map {
            StoreDataForMarking(
                distance = it.distance,
                storeId = it.storeId,
                storeName = it.storeName,
                storePointLat = it.storePointLat,
                storePointLng = it.storePointLng,
                storeType = it.storeType
            )
        })
        zoomInTo(15.0)
    }

    private fun markStoresOnMap(stores: List<StoreDataForMarking>) {
        naverMap.minZoom = 5.0
        naverMap.maxZoom = 18.0

        zoomInTo(15.0)
        val markers = mutableListOf<Marker>()
        stores.forEach { store ->
            markers.add(Marker().apply {
                position = LatLng(
                    store.storePointLat.toDouble(),
                    store.storePointLng.toDouble()
                ) // null 처리 필요
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.locationpin)
                width = 140
                height = 140
                val infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(this@MapActivity) {
                    override fun getText(p0: InfoWindow): CharSequence {
                        return store.storeName
                    }
                }
                infoWindow.open(this)
                setOnClickListener {
                    moveToPos(store.storePointLat.toDouble(), store.storePointLng.toDouble())
                    zoomInTo(17.0)

                    // todo -> detail Bottom Sheet
                    drawDetailStoreInfo(store.storeId, store.storeType)

                    true
                }
            })
        }
        this.markers = markers
    }

    private fun moveToPos(lat: Double = dreameLatLng.lat, lng: Double = dreameLatLng.lng) {
        val cameraUpdate =
            CameraUpdate.scrollTo(LatLng(lat, lng))
                .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun zoomInTo(zoomValue: Double) {
        val cameraUpdate =
            CameraUpdate.zoomTo(zoomValue)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun clearMarkers() {
        markers.forEach { marker ->
            marker.map = null
        }
    }

    private fun drawDetailStoreInfo(storeId: Int, storeType: String) {
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        viewModel.getDetailStoreInfo(storeId, storeType)
    }

    private fun initBottomSheetDetailDialog() {
        bottomSheetDetailBehavior = BottomSheetBehavior.from(bottomSheetDetail)
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.queriedDetailInfoLiveData.observe(this) {
            when (it) {
                is DetailUiState.Uninitialized -> {}
                is DetailUiState.Loading -> showProgressBarInBottomSheetDetail()
                is DetailUiState.SuccessGetDetailInfo -> bindDetailInfo(it.detailInfoItem)
                is DetailUiState.Error -> TODO()
            }
        }
    }

    private fun initMarkingOnMap() {
        viewModel.queriedStoresLiveData.observe(this) {
            when (it) {
                is StoreUiState.Uninitialized -> checkLocationPermission()
                is StoreUiState.SuccessGetStores -> markStoresOnMap(it.stores)
                is StoreUiState.Error -> TODO()
            }
        }
    }

    private fun checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPositionPermission()
            return
        }
        requestStoresByCurrentPos()
    }

    @SuppressLint("MissingPermission")
    private fun requestStoresByCurrentPos() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                dreameLatLng = DreameLatLng(it.latitude, it.longitude)
                moveToPos(dreameLatLng.lat, dreameLatLng.lng)
                viewModel.getStoresNearbyUserForMarking(dreameLatLng, 3000)
            }
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("위치 정보를 가져오기 위해서, 위치 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의") { _, _ ->
                requestPositionPermission()
            }
        }.show()
    }

    private fun requestPositionPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showProgressBarInBottomSheetDetail() {
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDetailProgressBar.isVisible = true
    }

    private var isBookmarked = false
    private val bookMarkButton by lazy {
        findViewById<ImageButton>(R.id.bottomSheetDetailFavoriteButton)
    }

    private fun bindDetailInfo(detailInfoOfStore: DetailInfoItem?) {
        isBookmarked = false
        detailInfoOfStore?.let { item ->
            bottomSheetDetailProgressBar.isVisible = false
            if (item.isBookmark == 1) {
                isBookmarked = true
            }
            setBookmark()
            findViewById<TextView>(R.id.bottomSheetDetailAddressTextView).text = item.address
            findViewById<TextView>(R.id.bottomSheetDetailCategoryTextView).text = item.cateName
            findViewById<TextView>(R.id.bottomSheetDetailWorkingTimeTextView).text =
                "${item.dayStart}~${item.dayFinish}"
            findViewById<TextView>(R.id.bottomSheetDetailPhoneNumberTextView).text = item.phone
            findViewById<TextView>(R.id.bottomSheetDetailProvidedSubjectTextView).text =
                "${item.provided1}, ${item.provided2}"
            findViewById<TextView>(R.id.bottomSheetDetailStoreNameTextView).text = item.storeName
            // todo : image
            findViewById<TextView>(R.id.bottomSheetDetailSubCategoryTextView).text =
                item.subCateName

            findViewById<ImageButton>(R.id.bottomSheetDetailCallButton).setOnClickListener {
//            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${"031-219-3678".split("-").joinToString(separator = "")}")))
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${item.phone.split("-").joinToString(separator = "")}}")
                    )
                )
            }

            bookMarkButton.setOnClickListener {
                viewModel.updateFavorite(item.storeID, item.storeType, isBookmarked)
                isBookmarked = !isBookmarked
                setBookmark()
            }
        }
    }

    private fun setBookmark() {
        if (isBookmarked) {
            bookMarkButton.setImageResource(R.drawable.book_checked)
        } else {
            bookMarkButton.setImageResource(R.drawable.baseline_bookmark_border_24)
        }
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        naverMap = map
    }
}