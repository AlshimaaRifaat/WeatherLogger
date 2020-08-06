package com.example.weatherlogger.ui.weather

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherlogger.util.toast

import com.example.weatherlogger.R
import com.example.weatherlogger.data.db.AppDatabase
import com.example.weatherlogger.data.db.entities.Temperature
import com.example.weatherlogger.data.models.WeatherResponse
import com.example.weatherlogger.data.network.MyApi
import com.example.weatherlogger.data.network.NetworkConnectionInterceptor
import com.example.weatherlogger.data.repositories.ApiException
import com.example.weatherlogger.data.repositories.NoInternetException
import com.example.weatherlogger.data.repositories.WeatherRepository
import com.example.weatherlogger.databinding.FragmentWeatherBinding
import com.example.weatherlogger.util.Coroutines
import com.example.weatherlogger.util.hide
import com.example.weatherlogger.util.show
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class WeatherFragment : Fragment(), WeatherListener {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    var locationLatitude: String? = null
    var locationLongitude: String? = null
    var PERMESSION_ID = 52
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var currentWeather: WeatherResponse
    private lateinit var binding: FragmentWeatherBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        val networkConnectionInterceptor = NetworkConnectionInterceptor(requireContext())
        val myApi = MyApi(networkConnectionInterceptor)
        val appDatabase = AppDatabase(requireContext())
        val weatherRepository = WeatherRepository(myApi, appDatabase)
        weatherViewModelFactory = WeatherViewModelFactory(weatherRepository)
        weatherViewModel =
            ViewModelProvider(this, weatherViewModelFactory).get(WeatherViewModel::class.java)

        weatherViewModel.weatherListener = this
        icSave.setOnClickListener { view ->
            lifecycleScope.launch {
                weatherViewModel.saveTemp(
                    this@WeatherFragment
                        .currentWeather.temperature
                )
            }
        }
        // weatherViewModel.getWeatherList("135.0","138.09","2a2f572ded21654bb03e4880033fb54b")


    }

    private fun getWeatherList() = Coroutines.main {
        progress_bar.show()

        weatherViewModel.tempretures.await().observe(viewLifecycleOwner, Observer { tempList ->
            progress_bar.hide()
            rvWeather.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = WeatherAdapter(tempList)
            }
        })
    }

    private fun getLastLocation() {
        if (checkPermission()) {

            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {

                        this.locationLatitude = location.latitude.toString()
                        this.locationLongitude = location.longitude.toString()

                        getTemp()
                        getWeatherList()
                    }
                }
            } else {
                context?.toast("Please Enable your location service")
            }
        } else {
            requestPermission()
        }
    }

    private fun getTemp() {
        lifecycleScope.launch {
            try {

                val responseData = weatherViewModel.getTemp(
                    locationLatitude!!.toDouble(),
                    locationLongitude!!.toDouble(), "2a2f572ded21654bb03e4880033fb54b"
                )
                onSuccess(responseData)
                this@WeatherFragment.currentWeather = responseData
                tTemp.text = "Temperature: "+responseData.temperature?.temp.toString()
                tDate.text ="Date: "+ responseData.dt.toString()
            } catch (e: ApiException) {
                e.printStackTrace()
                onFailure(e.message.toString())

            } catch (e: NoInternetException) {
                e.printStackTrace()
                onFailure(e.message.toString())
            }
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallBack, Looper.myLooper()
        )
    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            var lastLocation = p0?.lastLocation
            locationLatitude = lastLocation?.latitude.toString()
            locationLatitude = lastLocation?.longitude.toString()

        }
    }

    private fun checkPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMESSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMESSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "You have the permission")
            }
        }
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(weatherResponse: WeatherResponse) {
        progress_bar.hide()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        context?.toast(message)
    }
}
