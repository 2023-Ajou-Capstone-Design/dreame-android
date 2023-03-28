package com.example.dreamixmlversion.ui.map

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
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
import com.example.dreamixmlversion.data.api.response.entity.StoreDataEntityItem
import com.example.dreamixmlversion.data.db.entity.DreameLatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var bottomSheetStoreListBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetStoreList by lazy {
        findViewById<ConstraintLayout>(R.id.bottomSheetStoreList)
    }
    private lateinit var bottomSheetDetailBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetDetail by lazy {
        findViewById<ConstraintLayout>(R.id.bottomSheetDetail)
    }
    private val searchEditTextView: EditText by lazy {
        findViewById<EditText>(R.id.searchEditTextView)
    }
    private val favoritesImageButton: ImageButton by lazy {
        findViewById(R.id.favoritesImageButton)
    }
    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.bottomSheetProgressBar)
    }
    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }
    private lateinit var naverMap: NaverMap

    @Inject
    lateinit var storeAdapter: StoreAdapter

    private val viewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView.getMapAsync(this)
        mapView.onCreate(savedInstanceState)

        initSearchEditTextView()
        initCategoryLiveData()
        initFavoritesImageButton()
        initStoreAdapter()
        initBottomSheetStoreListDialog()
        initBottomSheetDetailDialog()
        initStoreLiveData()

        val dreameLatLng = DreameLatLng(37.279159, 127.044082) // 위치 임의 선정
        viewModel.getSpots(dreameLatLng)
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

    private fun initCategoryLiveData() {
        viewModel.getCategories()

//        viewModel.queriedStoresLiveData.observe(this) {
//            when (it) {
//                is StoreUiState.Uninitialized -> checkLocationPermission()
//                is StoreUiState.Loading -> showProgressBarInBottomSheet()
//                is StoreUiState.SuccessGetStores -> successGettingStores(it)
//                is StoreUiState.Error -> TODO()
//            }
//        }
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

    private fun initBottomSheetStoreListDialog() {
        bottomSheetStoreListBehavior = BottomSheetBehavior.from(bottomSheetStoreList)
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetStoreListBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

    }

    private fun initBottomSheetDetailDialog() {
        bottomSheetDetailBehavior = BottomSheetBehavior.from(bottomSheetDetail)
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetDetailBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun initStoreLiveData() {
        viewModel.queriedStoresLiveData.observe(this) {
            when (it) {
                is StoreUiState.Uninitialized -> checkLocationPermission()
                is StoreUiState.Loading -> showProgressBarInBottomSheet()
                is StoreUiState.SuccessGetStores -> successGettingStores(it)
                is StoreUiState.Error -> TODO()
            }
        }
    }

    private fun initStoreAdapter() {
        storeAdapter = StoreAdapter()
        storeAdapter.setStoreClickListener {

        }
    }

    private fun checkLocationPermission() {

    }

    private fun showProgressBarInBottomSheet() {
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        progressBar.isVisible = true
    }

    private fun hideProgressBarInBottomSheet() {
        progressBar.isVisible = false
    }

    private fun successGettingStores(state: StoreUiState.SuccessGetStores) {
        hideProgressBarInBottomSheet()
        spreadStoresInBottomSheet(state)
        markStoresOnMap(state.stores)
    }

    private fun spreadStoresInBottomSheet(state: StoreUiState.SuccessGetStores) {
        val recyclerView = findViewById<RecyclerView>(R.id.bottomSheetStoreRecyclerView)
        recyclerView.adapter = storeAdapter

        storeAdapter.submitList(state.stores)
//        storeAdapter.notifyDataSetChanged()
    }

    private fun markStoresOnMap(stores: List<StoreDataEntityItem>) {
        stores.forEach { store ->
            Marker().apply {
                position = LatLng(store.storePoitLat!!, store.storePoitLng!!) // null 처리 필요
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.locationpin)
                width = 140
                height = 140
                setOnClickListener {
                    val cameraUpdate =
                        CameraUpdate.scrollTo(LatLng(store.storePoitLat, store.storePoitLng))
                            .animate(CameraAnimation.Easing)
                    naverMap.moveCamera(cameraUpdate)

                    // todo -> detail Bottom Sheet
                    drawDetailStoreInfo()

                    true
                }
            }
        }
    }

    private fun drawDetailStoreInfo() {
        bottomSheetStoreListBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetDetailBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        naverMap = map
    }


}