package com.example.ispadmin.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.isp.viewmodel.ServerViewModelFactory
import com.example.ispadmin.adapters.ServerAdapter
import com.example.ispadmin.databinding.ActivityServerBinding
import com.example.ispadmin.repository.ServerRepository
import com.example.ispadmin.utils.Constants
import com.example.ispadmin.viewmodel.ServerViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ServerActivity : AppCompatActivity() {

    lateinit var binding: ActivityServerBinding
    lateinit var serverRepository: ServerRepository
    lateinit var serverViewModelFactory: ServerViewModelFactory
    lateinit var serverViewModel: ServerViewModel
    lateinit var locationManager: LocationManager
    var curLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (ActivityCompat.checkSelfPermission(
                this@ServerActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@ServerActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@ServerActivity,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                println("Location: "+location.latitude+", "+location.longitude)
                curLocation = location
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            5f,
            locationListener
        )
        curLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        serverRepository = ServerRepository()
        serverViewModelFactory = ServerViewModelFactory(serverRepository)
        serverViewModel = ViewModelProvider(this,serverViewModelFactory)[ServerViewModel::class.java]

        binding.apply {

            if(Constants.currentServer.id!=-1){
                edtTxtServerId.setText(Constants.currentServer.id.toString())
                edtTxtServerLoad.setText("${Constants.currentServer.loadOnServer.toString()}%")
                edtTxtServerType.setText(Constants.currentServer.type)
                toggleIsActive.isChecked = (Constants.currentServer.isActive==1)

                btnSubmit.setOnClickListener {
                    val id = edtTxtServerId.text.toString().toRequestBody(MultipartBody.FORM)
                    val load = edtTxtServerLoad.text.toString().removeSuffix("%").toRequestBody(MultipartBody.FORM)
                    val type = edtTxtServerType.text.toString().toRequestBody(MultipartBody.FORM)
                    val isActive = if(toggleIsActive.isChecked) {
                        "1".toRequestBody(MultipartBody.FORM)
                    }
                    else{
                        "0".toRequestBody(MultipartBody.FORM)
                    }

                    if(!toggleUpdateLocation.isChecked){
                        val fields = HashMap<String,RequestBody>()
                        fields["id"] = id
                        fields["loadOnServer"] = load
                        fields["type"] = type
                        println("Lat: ${Constants.currentServer.latitude}")
                        fields["latitude"] = Constants.currentServer.latitude.toString().toRequestBody(MultipartBody.FORM)
                        fields["longitude"] = Constants.currentServer.longitude.toString().toRequestBody(MultipartBody.FORM)
                        fields["isActive"] = isActive

                        serverViewModel.updateServer(fields)
                    }
                    else{
                        if(curLocation!=null){
                            val fields = HashMap<String,RequestBody>()
                            fields["id"] = id
                            fields["loadOnServer"] = load
                            fields["type"] = type
                            fields["latitude"] = curLocation?.latitude.toString().toRequestBody(MultipartBody.FORM)
                            fields["longitude"] = curLocation?.longitude.toString().toRequestBody(MultipartBody.FORM)
                            fields["isActive"] = isActive

                            serverViewModel.updateServer(fields)
                        }
                        else{
                            Toast.makeText(this@ServerActivity,"Can't get your location, try again in few seconds",Toast.LENGTH_LONG).show()
                        }
                    }

                }

                serverViewModel.myResponseString.observe(this@ServerActivity){responseString->
                    Toast.makeText(this@ServerActivity,responseString,Toast.LENGTH_SHORT).show()
                }

            }
            else{
                edtTxtServerId.visibility = View.INVISIBLE
                layoutUpdateLocation.visibility = View.INVISIBLE

                btnSubmit.setOnClickListener {
                    val load = edtTxtServerLoad.text.toString().toRequestBody(MultipartBody.FORM)
                    val type = edtTxtServerType.text.toString().toRequestBody(MultipartBody.FORM)
                    val isActive = if(toggleIsActive.isChecked) {
                        "1".toRequestBody(MultipartBody.FORM)
                    }
                    else{
                        "0".toRequestBody(MultipartBody.FORM)
                    }

                    if(curLocation!=null){

                        val fields = HashMap<String,RequestBody>()
                        fields["loadOnServer"] = load
                        fields["type"] = type
                        fields["latitude"] = curLocation?.latitude.toString().toRequestBody(MultipartBody.FORM)
                        fields["longitude"] = curLocation?.longitude.toString().toRequestBody(MultipartBody.FORM)
                        fields["isActive"] = isActive

                        serverViewModel.addServer(fields)

                    }
                    else{
                        Toast.makeText(this@ServerActivity,"Can't get your location, try again in few seconds",Toast.LENGTH_LONG).show()
                    }
                }
                serverViewModel.myResponse.observe(this@ServerActivity){
                    Toast.makeText(this@ServerActivity,"Successful",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this@ServerActivity,MainActivity::class.java).also{
            startActivity(it)
        }
    }

}