package com.example.ispadmin.activity

import android.Manifest
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
import com.example.ispadmin.R
import com.example.ispadmin.databinding.ActivityCustomerBinding
import com.example.ispadmin.databinding.FragmentNewCustomersBinding
import com.example.ispadmin.repository.CustomerRepository
import com.example.ispadmin.utils.Constants
import com.example.ispadmin.viewmodel.CustomerViewModel
import com.example.ispadmin.viewmodel.CustomerViewModelFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CustomerActivity : AppCompatActivity() {

    lateinit var binding: ActivityCustomerBinding
    lateinit var customerRepository: CustomerRepository
    lateinit var customerViewModelFactory: CustomerViewModelFactory
    lateinit var customerViewModel: CustomerViewModel
    lateinit var locationManager: LocationManager
    var curLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (ActivityCompat.checkSelfPermission(
                this@CustomerActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@CustomerActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@CustomerActivity,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
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

        customerRepository = CustomerRepository()
        customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this,customerViewModelFactory)[CustomerViewModel::class.java]

        binding.apply {

            if(Constants.currentCustomer.custId!=-1){
                edtTxtNewCustomerPassword.visibility = View.INVISIBLE

                edtTxtNewCustomerName.setText(Constants.currentCustomer.name)
                edtTxtNewCustomerAddress.setText(Constants.currentCustomer.address)
                edtTxtNewCustomerPhoneNo.setText(Constants.currentCustomer.phoneNo)
                edtTxtNewCustomerEmail.setText(Constants.currentCustomer.email)

                btnSubmitCustomer.setOnClickListener {
                    if(curLocation!=null){
                        btnSubmitCustomer.isEnabled = false
                        var lg = 0.0
                        var lt = 0.0
                        if(toggleUpdateLocation.isChecked){
                            lt = curLocation?.latitude!!
                            lg = curLocation?.longitude!!
                        }
                        else{
                            lg = Constants.currentCustomer.longitude
                            lt = Constants.currentCustomer.latitude
                        }

                        val values = HashMap<String, RequestBody>()
                        values["custId"] = Constants.currentCustomer.custId.toString().toRequestBody(MultipartBody.FORM)
                        values["name"] = edtTxtNewCustomerName.text.toString().toRequestBody(MultipartBody.FORM)
                        values["address"] = edtTxtNewCustomerAddress.text.toString().toRequestBody(
                            MultipartBody.FORM)
                        values["longitude"] = lg.toString().toRequestBody(MultipartBody.FORM)
                        values["latitude"] = lt.toString().toRequestBody(MultipartBody.FORM)
                        values["phoneNo"] = edtTxtNewCustomerPhoneNo.text.toString().toRequestBody(
                            MultipartBody.FORM)
                        values["Email"] = edtTxtNewCustomerEmail.text.toString().toRequestBody(MultipartBody.FORM)
                        values["CustomerSince"] = Constants.currentCustomer.customerSince.toString().toRequestBody(MultipartBody.FORM)
                        values["planEndDate"] = Constants.currentCustomer.planEndDate.toString().toRequestBody(MultipartBody.FORM)
                        values["planId"] = Constants.currentCustomer.planId.toString().toRequestBody(MultipartBody.FORM)

                        if(toggleIsActive.isChecked){
                            values["isActive"] = 1.toString().toRequestBody(MultipartBody.FORM)
                        }
                        else{
                            values["isActive"] = 0.toString().toRequestBody(MultipartBody.FORM)
                        }

                        customerViewModel.updateCustomer(values)
                    }
                    else{
                        Toast.makeText(this@CustomerActivity,"Can't get your location, try again in few seconds",Toast.LENGTH_LONG).show()
                    }
                }
                customerViewModel.myResponseString.observe(this@CustomerActivity){ responseString ->
                    Toast.makeText(this@CustomerActivity,responseString, Toast.LENGTH_LONG).show()
                    btnSubmitCustomer.isEnabled = true
                }
            }
            else{
                layoutUpdateLocation.visibility = View.GONE
                btnSubmitCustomer.setOnClickListener {
                    if(curLocation!=null){
                        btnSubmitCustomer.isEnabled = false
                        var lg = 0.0
                        var lt = 0.0
                        lt = curLocation?.latitude!!
                        lg = curLocation?.longitude!!

                        val time = System.currentTimeMillis()
                        val planEndTime = time+2592000000

                        val values = HashMap<String, RequestBody>()
                        values["name"] = edtTxtNewCustomerName.text.toString().toRequestBody(MultipartBody.FORM)
                        values["hashedPassword"] = Constants.sha256(edtTxtNewCustomerPassword.text.toString()).toRequestBody(
                            MultipartBody.FORM)
                        values["address"] = edtTxtNewCustomerAddress.text.toString().toRequestBody(
                            MultipartBody.FORM)
                        values["longitude"] = lg.toString().toRequestBody(MultipartBody.FORM)
                        values["latitude"] = lt.toString().toRequestBody(MultipartBody.FORM)
                        values["phoneNo"] = edtTxtNewCustomerPhoneNo.text.toString().toRequestBody(
                            MultipartBody.FORM)
                        values["Email"] = edtTxtNewCustomerEmail.text.toString().toRequestBody(MultipartBody.FORM)
                        values["CustomerSince"] = time.toString().toRequestBody(MultipartBody.FORM)
                        values["planEndDate"] = planEndTime.toString().toRequestBody(MultipartBody.FORM)
                        values["planId"] = 1.toString().toRequestBody(MultipartBody.FORM)
                        if(toggleIsActive.isChecked){
                            values["isActive"] = 1.toString().toRequestBody(MultipartBody.FORM)
                        }
                        else{
                            values["isActive"] = 0.toString().toRequestBody(MultipartBody.FORM)
                        }

                        customerViewModel.addCustomer(values)
                    }
                    else{
                        Toast.makeText(this@CustomerActivity,"Can't get your location, try again in few seconds",Toast.LENGTH_LONG).show()
                    }
                }
                customerViewModel.myResponse.observe(this@CustomerActivity){ response ->
                    if(response.custId!=-1){
                        Toast.makeText(this@CustomerActivity,"Customer Id: "+response.custId, Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@CustomerActivity,"Some Error Occurred", Toast.LENGTH_LONG).show()
                    }
                    btnSubmitCustomer.isEnabled = true
                }
            }



        }

    }
}