package com.example.dreamixmlversion

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dreamixmlversion.data.api.FirebaseKey
import com.example.dreamixmlversion.data.api.FirebaseKey.FCM_TOKEN
import com.example.dreamixmlversion.data.api.FirebaseKey.USER_INFO
import com.example.dreamixmlversion.extension.convertStrToBase64
import com.example.dreamixmlversion.ui.chat.chatlist.ChatListFragment
import com.example.dreamixmlversion.ui.home.HomeFragment
import com.example.dreamixmlversion.ui.login.LoginActivity
import com.example.dreamixmlversion.ui.sharing.register.RegisterNewSharingScreenFragment
import com.example.dreamixmlversion.ui.user.UserFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var keyHash = Utility.getKeyHash(this)

        if (checkLogin()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            initNavigation()
//            checkExternalStoragePermission()
            askNotificationPermission()
        }
    }

    private fun checkLogin(): Boolean {
        Log.d("userId", viewModel.getEmailAddress().toString())
        return viewModel.getEmailAddress() == null
    }

    private fun initNavigation() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.mainNavigationHostContainer
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest, R.id.chat_dest, R.id.user_dest)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home_dest || destination.id == R.id.chat_dest || destination.id == R.id.user_dest) {
                bottomNavigationView.visibility = View.VISIBLE
            } else {
                bottomNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainNavigationHostContainer)

//        val currentDestinationId =  navController.currentDestination?.id
//        val navGraph = navController.graph
//        val navDestination = navGraph.findNode(currentDestinationId!!)
//        val label = navDestination?.label
//
//        return when(label) {
//            "Chat" -> {
//                navController.popBackStack()
//                true
//            }
//            "Sharing" -> {
//                navController.popBackStack()
//                true
//            }
//            else -> navController.navigateUp(appBarConfiguration)
//        }

        return navController.navigateUp(appBarConfiguration)
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) || permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                checkLocationPermission()
            }
            else -> {
//                showPermissionInfoDialog()
            }
        }
    }

    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.size > 3) {
                Toast.makeText(this, "최대 3장까지 첨부 가능합니다.", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
//            updateImages(uriList)
        }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPositionPermission()
        }
    }

    private fun requestPositionPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun checkExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
//                    loadImage()
                }
                shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_MEDIA_IMAGES
                ) -> {
//                    showPermissionInfoDialog()
                }
                else -> {
                    requestReadExternalStorage()
                }
            }
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
//                    loadImage()
                }
                shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> {
//                    showPermissionInfoDialog()
                }
                else -> {
                    requestReadExternalStorage()
                }
            }
        }
    }

    private fun requestReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                RegisterNewSharingScreenFragment.REQUEST_EXTERNAL_STORAGE_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RegisterNewSharingScreenFragment.REQUEST_EXTERNAL_STORAGE_CODE
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            getFcmTokenAndRegisterToRTDB()
        } else {
            Toast.makeText(this, "채팅 알림을 받을 수 없습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationalDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        else {
            getFcmTokenAndRegisterToRTDB()
        }
    }

    private fun showPermissionRationalDialog() {
        AlertDialog.Builder(this).setMessage("알림 권한이 없으면 알림을 받을 수 없습니다.")
            .setPositiveButton("권한 허용하기") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }.setNegativeButton("취소") { dialogInterface, _ -> dialogInterface.cancel() }.show()
    }

    private fun getFcmTokenAndRegisterToRTDB() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val token = task.result
            val tokenMap = mutableMapOf<String, Any>()
            tokenMap[FCM_TOKEN] = token
            Firebase.database.reference.child(USER_INFO)
                .child(viewModel.getNickname().toString().convertStrToBase64())
                .updateChildren(tokenMap)
        }
    }
}