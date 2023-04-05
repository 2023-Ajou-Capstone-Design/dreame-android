package com.example.dreamixmlversion.ui.map

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.response.entity.DetailInfoItem
import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking
import com.example.dreamixmlversion.data.api.response.entity.StoreDataOnBottomSheetList
import com.example.dreamixmlversion.data.db.entity.CategoryEntity
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.databinding.ActivityMapBinding
import com.example.dreamixmlversion.ui.map.uistate.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        bindMarkingOnMap()
    }

    private fun initSearchEditTextView() {
        binding.searchEditTextView.setOnEditorActionListener { editText, actionId, event ->


            currentFocus?.let { view ->
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)

                // todo 검색 query to server and process

                viewModel.getStoresBySearchingKeyword(editText.text.toString(), dreameLatLng, 5000)
            }
            return@setOnEditorActionListener true
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
        }
        binding.categoryRecyclerView.adapter = categoryAdapter
    }

    private fun initFavoriteButton() {
        binding.favoritesImageButton.setOnClickListener {
            viewModel.getFavoriteStores("") // todo userId 기입
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

    private fun checkLocationPermission() {}

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
    }

    private fun markStoresOnMap(stores: List<StoreDataForMarking>) {
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

                    // todo -> detail Bottom Sheet
                    drawDetailStoreInfo(store.storeId, store.storeType)

                    true
                }
            })
        }
        this.markers = markers
    }

    private fun moveToPos(lat: Double, lng: Double) {
        val cameraUpdate =
            CameraUpdate.scrollTo(LatLng(lat, lng))
                .animate(CameraAnimation.Easing)
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
//        bottomSheetDetailBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//        })

        viewModel.queriedDetailInfoLiveData.observe(this) {
            when (it) {
                is DetailUiState.Uninitialized -> {}
                is DetailUiState.Loading -> showProgressBarInBottomSheetDetail()
                is DetailUiState.SuccessGetDetailInfo -> bindDetailInfo(it.detailInfoItem)
                is DetailUiState.Error -> TODO()
            }
        }
    }

    private fun bindMarkingOnMap() {
        dreameLatLng = DreameLatLng(37.2784, 127.0442) // 위치 임의 선정
//        moveToPos(dreameLatLng.lat, dreameLatLng.lng)

        viewModel.getStoresNearbyUserForMarking(dreameLatLng, 7000)
        viewModel.queriedStoresLiveData.observe(this) {
            when (it) {
                is StoreUiState.Uninitialized -> checkLocationPermission()
                is StoreUiState.SuccessGetStores -> markStoresOnMap(it.stores)
                is StoreUiState.Error -> TODO()
            }
        }
    }

    private fun showProgressBarInBottomSheetDetail() {
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDetailProgressBar.isVisible = true
    }

    private fun bindDetailInfo(detailInfoOfStore: DetailInfoItem) {
        bottomSheetDetailProgressBar.isVisible = false
        findViewById<TextView>(R.id.bottomSheetDetailAddressTextView).text = detailInfoOfStore.address
        findViewById<TextView>(R.id.bottomSheetDetailCategoryTextView).text = detailInfoOfStore.cateName
        findViewById<TextView>(R.id.bottomSheetDetailWorkingTimeTextView).text =
            "${detailInfoOfStore.dayStart}~${detailInfoOfStore.dayFinish}"
        findViewById<TextView>(R.id.bottomSheetDetailPhoneNumberTextView).text = detailInfoOfStore.phone
        findViewById<TextView>(R.id.bottomSheetDetailProvidedSubjectTextView).text =
            "${detailInfoOfStore.provided1}, ${detailInfoOfStore.provided2}"
        findViewById<TextView>(R.id.bottomSheetDetailStoreNameTextView).text = detailInfoOfStore.storeName
        // todo : image
        findViewById<TextView>(R.id.bottomSheetDetailSubCategoryTextView).text = detailInfoOfStore.subCateName
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        naverMap = map
    }
}