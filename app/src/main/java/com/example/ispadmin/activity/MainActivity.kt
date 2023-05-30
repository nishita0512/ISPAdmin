package com.example.ispadmin.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import com.example.ispadmin.R
import com.example.ispadmin.adapters.ViewPagerAdapter
import com.example.ispadmin.databinding.ActivityMainBinding
import com.example.ispadmin.fragment.NewCustomersFragment
import com.example.ispadmin.fragment.ServersFragment
import com.example.ispadmin.fragment.SupportFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                finishAffinity()
            }
        } else {
            onBackPressedDispatcher.addCallback(this@MainActivity, object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    finishAffinity()
                }
            })
        }

        val fragmentList = arrayListOf(
            NewCustomersFragment(),
            ServersFragment(),
            SupportFragment()
        )

        val viewPagerAdapter = ViewPagerAdapter(fragmentList,this.supportFragmentManager,lifecycle)

        binding.apply{

            viewPagerMainActivity.isUserInputEnabled = false

            bottomMenu.setItemSelected(R.id.newCustomer)
            viewPagerMainActivity.adapter = viewPagerAdapter
            bottomMenu.setOnItemSelectedListener {
                when(it){
                    R.id.newCustomer ->{
                        viewPagerMainActivity.currentItem = 0
                    }
                    R.id.servers ->{
                        viewPagerMainActivity.currentItem = 1
                    }
                    R.id.support ->{
                        viewPagerMainActivity.currentItem = 2
                    }
                }
            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}