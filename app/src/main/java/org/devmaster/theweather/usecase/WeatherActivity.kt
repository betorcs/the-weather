package org.devmaster.theweather.usecase

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.devmaster.theweather.R
import org.devmaster.theweather.WeatherApp
import org.devmaster.theweather.data.WeatherRepository
import org.devmaster.theweather.data.local.WeatherDao
import org.devmaster.theweather.data.network.WeatherApi
import org.devmaster.theweather.databinding.ActivityMainBinding
import org.devmaster.theweather.model.CurrentWeather
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(), WeatherContract.View {

    @Inject
    lateinit var mWeatherDao: WeatherDao

    @Inject
    lateinit var mWeatherApi: WeatherApi

    private lateinit var mBinding: ActivityMainBinding

    private var mSnackbar: Snackbar? = null

    private val mRepository: WeatherRepository by lazy {
        WeatherRepository(mWeatherApi, mWeatherDao)
    }

    private val presenter: WeatherPresenter by lazy {
        WeatherPresenter(mRepository, this)
    }

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val RC = 1001
    private val permission = Manifest.permission.ACCESS_COARSE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WeatherApp.component.inject(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            presenter.refresh()
        }

        getWeatherInCurrentLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather, menu)
        val item = menu.findItem(R.id.act_search)
        val searchView = item.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { location ->
                    presenter.getWeather(location)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?) = false

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            RC -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    getWeatherInCurrentLocation()
                } else {
                    Toast.makeText(this, R.string.location_cannot_be_determinated, Toast.LENGTH_LONG).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getWeatherInCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                AlertDialog.Builder(this)
                        .setTitle(R.string.weather)
                        .setMessage(R.string.location_permission_rationale)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, { _, _ ->
                            ActivityCompat.requestPermissions(this, arrayOf(permission), RC)
                        }).show()

            } else {

                ActivityCompat.requestPermissions(this, arrayOf(permission), RC)
            }

            return
        }

        mFusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let { presenter.getWeather(it.latitude, it.longitude) }
                }

    }

    /* WeatherContract.View */

    override fun showWeathers(weather: CurrentWeather) {
        this.mBinding.currentWeather = weather
        this.mBinding.detail?.currentWeather = weather
    }

    override fun showErrorPlaceholder(error: Throwable) {
        val message = error.message ?: getString(R.string.generic_error_message)
        mSnackbar = Snackbar.make(mBinding.root, message, Snackbar.LENGTH_INDEFINITE)
        mSnackbar?.show()
    }

    override fun setProgressIndicator(active: Boolean) {
        mBinding.swipeRefreshLayout.isRefreshing = active
        mBinding.swipeRefreshLayout.isActivated = !active
    }

    override fun hidePlaceholders() {
        mSnackbar?.dismiss()
    }

}
