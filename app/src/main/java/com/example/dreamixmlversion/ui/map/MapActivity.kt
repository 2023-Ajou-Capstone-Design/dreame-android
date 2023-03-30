package com.example.dreamixmlversion.ui.map

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.data.api.response.entity.StoreDataForMarking
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.example.dreamixmlversion.ui.map.uistate.CategoryUiState
import com.example.dreamixmlversion.ui.map.uistate.DetailInfoItem
import com.example.dreamixmlversion.ui.map.uistate.DetailUiState
import com.example.dreamixmlversion.ui.map.uistate.StoreUiState
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

    private val searchEditTextView: EditText by lazy {
        findViewById<EditText>(R.id.searchEditTextView)
    }

    @Inject lateinit var categoryAdapter: CategoryAdapter
    private val categoryRecyclerView: RecyclerView by lazy {
        findViewById(R.id.categoryRecyclerView)
    }

    @Inject lateinit var storeAdapter: StoreAdapter
    private lateinit var bottomSheetStoreListBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetStoreList by lazy {
        findViewById<ConstraintLayout>(R.id.bottomSheetStoreList)
    }

    private lateinit var bottomSheetDetailBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetDetail by lazy {
        findViewById<ConstraintLayout>(R.id.bottomSheetDetail)
    }

    private val favoritesImageButton: ImageButton by lazy {
        findViewById(R.id.favoritesImageButton)
    }
    private val bottomSheetStoreListProgressBar: ProgressBar by lazy {
        findViewById(R.id.bottomSheetStoreListProgressBar)
    }
    private val bottomSheetDetailProgressBar: ProgressBar by lazy {
        findViewById(R.id.bottomSheetDetailProgressBar)
    }
    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }
    private lateinit var naverMap: NaverMap

    private val viewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)

        initSearchEditTextView()
        initCategory()
        initBottomSheetStoreList()
        initBottomSheetDetailDialog()
        initFavoritesImageButton()

        val dreameLatLng = DreameLatLng(37.279159, 127.044082) // 위치 임의 선정
//        val cameraUpdate =
//            CameraUpdate.scrollTo(LatLng(dreameLatLng.lat, dreameLatLng.lng))
//                .animate(CameraAnimation.Easing)
//        naverMap.moveCamera(cameraUpdate)

        viewModel.getStores(dreameLatLng, 1)
    }

    private fun initSearchEditTextView() {
        searchEditTextView.setOnEditorActionListener { editText, actionId, event ->
            currentFocus?.let { view ->
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)

                // todo 검색 query to server and process

                view.clearFocus()
            }
            return@setOnEditorActionListener true
        }
    }

    private fun initCategory() {
        categoryAdapter = CategoryAdapter()
        viewModel.getCategories()
        categoryAdapter.setOnCategoryClickListener {
            // todo : viewModel.getStores() 카테고리명 파라미터를 담은 함수로 호출하여 BottomSheetStoreList recyclerView를 갱신한다.

        }
        categoryRecyclerView.adapter = categoryAdapter

        viewModel.queriedCategoriesLiveData.observe(this) {
            when (it) {
                is CategoryUiState.Uninitialized -> {}
                is CategoryUiState.SuccessGetCategories -> updateCategoryRecyclerView(it.categories)
                CategoryUiState.Error -> TODO()
            }
        }
    }

    private fun updateCategoryRecyclerView(categories: List<CategoryItem>) {
        categoryAdapter.submitList(categories)
    }

    private fun initFavoritesImageButton() {
//        favoritesImageButton.setImageResource(R.drawable.bookmark_unchecked)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun initBottomSheetStoreList() {
        storeAdapter = StoreAdapter()

        bottomSheetStoreListBehavior = BottomSheetBehavior.from(bottomSheetStoreList)
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        bottomSheetStoreListBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//
//                    }
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//
//        })

        storeAdapter.setOnStoreClickListener {
            bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            moveToPos(it.storePointLat.toDouble(), it.storePointLng.toDouble())
            viewModel.getDetailStoreInfo(it.storeId)
        }

        viewModel.queriedStoresLiveData.observe(this) {
            when (it) {
                is StoreUiState.Uninitialized -> checkLocationPermission()
//                is StoreUiState.Loading -> showProgressBarInBottomSheetStoreList()
                is StoreUiState.SuccessGetStores -> successGettingStores(it)
                is StoreUiState.Error -> TODO()
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

    private fun successGettingStores(state: StoreUiState.SuccessGetStores) {
        hideProgressBarInBottomSheet()
//        spreadStoresInBottomSheet(state)
        markStoresOnMap(state.stores)
    }

    private fun spreadStoresInBottomSheet(state: StoreUiState.SuccessGetStores) {
        val recyclerView = findViewById<RecyclerView>(R.id.bottomSheetStoreRecyclerView)
        recyclerView.adapter = storeAdapter

        storeAdapter.submitList(state.stores)
    }

    private fun markStoresOnMap(stores: List<StoreDataForMarking>) {
        stores.forEach { store ->
            Marker().apply {
                position = LatLng(store.storePointLat.toDouble(), store.storePointLng.toDouble()) // null 처리 필요
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.locationpin)
                width = 140
                height = 140
//                captionText = store.storeName
//                captionRequestedWidth = 150
                val infoWindow = InfoWindow()
                infoWindow.adapter = object: InfoWindow.DefaultTextAdapter(this@MapActivity) {
                    override fun getText(p0: InfoWindow): CharSequence {
                        return store.storeName
                    }
                }
                infoWindow.open(this)
                setOnClickListener {
                    moveToPos(store.storePointLat.toDouble(), store.storePointLng.toDouble())

                    // todo -> detail Bottom Sheet
                    drawDetailStoreInfo()

                    true
                }
            }
        }
    }

    private fun moveToPos(lat: Double, lng: Double) {
        val cameraUpdate =
            CameraUpdate.scrollTo(LatLng(lat, lng))
                .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun drawDetailStoreInfo() {
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
                is DetailUiState.SuccessGetDetailInfo -> successGettingDetailStoreInfo(it.detailInfoItem)
                is DetailUiState.Error -> TODO()
            }
        }
    }

    private fun showProgressBarInBottomSheetDetail() {
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDetailProgressBar.isVisible = true
    }

    private fun successGettingDetailStoreInfo(detailInfoOfStore: DetailInfoItem) {
        bottomSheetDetailProgressBar.isVisible = false
        bindDetailInfo(detailInfoOfStore)
    }

    private fun bindDetailInfo(detailInfoOfStore: DetailInfoItem) {
        // todo : image
//        findViewById<TextView>(R.id.bottomSheetDetailStoreNameTextView).text = detailInfoOfStore.storeName
//        findViewById<TextView>(R.id.bottomSheetDetailCategoryTextView).text = detailInfoOfStore.categoryName
//        findViewById<TextView>(R.id.bottomSheetDetailWorkingTimeTextView).text = detailInfoOfStore.workingTime
//        findViewById<TextView>(R.id.bottomSheetDetailAddressTextView).text = detailInfoOfStore.address
//        findViewById<TextView>(R.id.bottomSheetDetailPhoneNumberTextView).text = detailInfoOfStore.phoneNumber
//        findViewById<TextView>(R.id.bottomSheetDetailProvidedSubjectTextView).text = detailInfoOfStore.providedSubject
//        findViewById<TextView>(R.id.bottomSheetDetailProvidedItemTextView).text = detailInfoOfStore.providedItem
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        naverMap = map
    }
}