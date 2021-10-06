package dev.saket.readyweather.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.saket.readyweather.databinding.ActivityMainBinding
import dev.saket.readyweather.fragment.ViewPagerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var connectivityManager: ConnectivityManager


    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var snackbar: Snackbar

    private val fragmentArray = arrayOf(
        "Weather",
        "Next Day",
        "Forecast"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.pager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentArray[position]
        }.attach()


        // Removed for add Tabbed Layout with ViewPager.
        /* binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

             override fun onTabSelected(tab: TabLayout.Tab?) {
                 // Handle tab select
             }

             override fun onTabReselected(tab: TabLayout.Tab?) {
                 // Handle tab reselect
             }

             override fun onTabUnselected(tab: TabLayout.Tab?) {
                 // Handle tab unselect
             }
         })*/

        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        snackbar = Snackbar.make(
            this,
            binding.mainActivityLayout,
            "No Network Connection",
            Snackbar.LENGTH_INDEFINITE
        )

        val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.anchorId = binding.tabLayout.id
        layoutParams.anchorGravity = Gravity.TOP
        layoutParams.gravity = Gravity.TOP

        snackbar.apply()
        {
            view.layoutParams = layoutParams
            setAction("Retry") {
                ActivityCompat.recreate(this@MainActivity)
            }
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        Log.d("dlo", "InternetPermissions")
        requestCurrentLocation()

        mainViewModel.networkStatus.observe(this,
            {
                Log.d("ObservingNetworkStatus", it.toString())
                if (it) {
                    hideSnackBar()
                    requestCurrentLocation()
                } else {
                    showSnackBar()
                }
            })


        // TODO Weather Icon Pack by Ladalle CS
        // TODO <a href="https://iconscout.com/icon-pack/weather-190" target="_blank">Weather Icon Pack</a> by <a href="https://iconscout.com/contributors/ladalle-cs" target="_blank">Ladalle CS</a>

        // TODO Download Calendar Icon by Omar Safaa on Iconscout
        // TODO    <a href="https://iconscout.com/icons/next-date" target="_blank">Next Date Icon</a> by <a href="https://iconscout.com/contributors/omarsafaa" target="_blank">Omar Safaa</a>
    }

    override fun onResume() {
        super.onResume()
        registerNetworkCallback()
    }

    override fun onPause() {
        super.onPause()
        unregisterNetworkCallback()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if (!locationPermissionNotGranted()) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        requestCurrentLocation()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }

                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    @Throws(SecurityException::class)
    private fun requestCurrentLocation() {
        Log.d("mlsa", "Requesting Location...")
        if (locationPermissionNotGranted()) {
            requestLocationPermission()
            return
        }
        Log.d("lhsa", "GetCurrentLocation")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {

                    val lat = it.latitude
                    val lon = it.longitude
                    mainViewModel.setLocation(
                        dev.saket.readyweather.data.remote.Location(
                            lat,
                            lon
                        )
                    )
                    Log.d("CurrentLocation", "$lat,$lon")
                }

            }
    }


    private fun requestLocationPermission() {
        Log.d("ksao", "RequestLocationPermission")
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d("networkCallback", "Network is available")
            mainViewModel.setNetworkStatus(true)
        }

        override fun onLost(network: Network) {
            Log.d("networkCallback", "Network is lost")
            mainViewModel.setNetworkStatus(false)
        }
    }

    private fun registerNetworkCallback() {
        try {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } catch (e: Exception) {
            Log.e(javaClass.canonicalName, e.message, e)
            e.printStackTrace()
            mainViewModel.setNetworkStatus(false)
        }
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun showSnackBar() {
        snackbar.show()
    }

    private fun hideSnackBar() {
        if (snackbar.isShown) {
            snackbar.dismiss()
        }
    }


    private fun locationPermissionNotGranted() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
}