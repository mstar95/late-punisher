package com.mstar.frontend

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SetMeetingPlaceActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager : LocationManager
    private var loc: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_meeting_place)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        prepareLocationListener()
        val pos = LatLng(loc?.latitude?: 52.12, loc?.longitude?: 20.58)
        mMap.addMarker(MarkerOptions().position(pos).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))

        mMap.setOnMarkerClickListener {
            Log.i("XD", it.id.toString())
         true
        }
    }

    private fun prepareLocationListener() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mLocationListener = object : LocationListener {
            override fun onProviderDisabled(p0: String?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onLocationChanged(location: Location) {
                loc = location
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                100F, mLocationListener);
    }
}
