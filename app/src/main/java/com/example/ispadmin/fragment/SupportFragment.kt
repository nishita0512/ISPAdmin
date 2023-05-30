package com.example.ispadmin.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ispadmin.R
import com.example.ispadmin.adapters.CustomerAdapter
import com.example.ispadmin.databinding.FragmentSupportBinding
import com.example.ispadmin.model.Customer
import com.example.ispadmin.repository.CustomerRepository
import com.example.ispadmin.viewmodel.CustomerViewModel
import com.example.ispadmin.viewmodel.CustomerViewModelFactory

class SupportFragment : Fragment() {

    lateinit var binding: FragmentSupportBinding
    lateinit var customersList: ArrayList<Customer>
    lateinit var customerAdapter: CustomerAdapter
    lateinit var customerRepository: CustomerRepository
    lateinit var customerViewModelFactory: CustomerViewModelFactory
    lateinit var customerViewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportBinding.inflate(inflater,container,false)

        customerRepository = CustomerRepository()
        customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this,customerViewModelFactory)[CustomerViewModel::class.java]
        customerAdapter = CustomerAdapter()

        binding.apply {
            customerViewModel.getAllCustomers()
            customerViewModel.myResponseList.observe(requireActivity()) {response ->

                if(response.isNotEmpty()){
                    customersList = response
                    recyclerCustomersList.layoutManager = LinearLayoutManager(requireContext())
                    customerAdapter.submitList(customersList)
                    recyclerCustomersList.adapter = customerAdapter
                }

                customerViewModel.myResponse.removeObservers(requireActivity())
            }
        }

        return binding.root
    }

}