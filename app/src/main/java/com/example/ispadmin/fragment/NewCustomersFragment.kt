package com.example.ispadmin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ispadmin.activity.CustomerActivity
import com.example.ispadmin.adapters.NewCustomerAdapter
import com.example.ispadmin.databinding.FragmentNewCustomersBinding
import com.example.ispadmin.model.Customer
import com.example.ispadmin.repository.CustomerRepository
import com.example.ispadmin.utils.Constants
import com.example.ispadmin.viewmodel.CustomerViewModel
import com.example.ispadmin.viewmodel.CustomerViewModelFactory

class NewCustomersFragment : Fragment() {

    lateinit var binding: FragmentNewCustomersBinding
    lateinit var customersList: ArrayList<Customer>
    lateinit var customerAdapter: NewCustomerAdapter
    lateinit var customerRepository: CustomerRepository
    lateinit var customerViewModelFactory: CustomerViewModelFactory
    lateinit var customerViewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewCustomersBinding.inflate(inflater,container,false)

        customerRepository = CustomerRepository()
        customerViewModelFactory = CustomerViewModelFactory(customerRepository)
        customerViewModel = ViewModelProvider(this,customerViewModelFactory)[CustomerViewModel::class.java]
        customerAdapter = NewCustomerAdapter()

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

            btnAddCustomer.setOnClickListener {
                Constants.currentCustomer = Customer(-1,"","","",0.00,0.00,"","",0,0,0,0,0)
                Intent(requireContext(),CustomerActivity::class.java).also{
                    startActivity(it)
                }
            }

        }

        return binding.root
    }

}