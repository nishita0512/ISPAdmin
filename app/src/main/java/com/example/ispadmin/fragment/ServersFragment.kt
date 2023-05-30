package com.example.ispadmin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.isp.viewmodel.ServerViewModelFactory
import com.example.ispadmin.R
import com.example.ispadmin.activity.ServerActivity
import com.example.ispadmin.adapters.CustomerAdapter
import com.example.ispadmin.adapters.ServerAdapter
import com.example.ispadmin.databinding.FragmentServersBinding
import com.example.ispadmin.databinding.FragmentSupportBinding
import com.example.ispadmin.model.Customer
import com.example.ispadmin.model.Server
import com.example.ispadmin.repository.CustomerRepository
import com.example.ispadmin.repository.ServerRepository
import com.example.ispadmin.utils.Constants
import com.example.ispadmin.viewmodel.CustomerViewModel
import com.example.ispadmin.viewmodel.CustomerViewModelFactory
import com.example.ispadmin.viewmodel.ServerViewModel


class ServersFragment : Fragment() {

    lateinit var binding: FragmentServersBinding
    lateinit var serversList: ArrayList<Server>
    lateinit var serverAdapter: ServerAdapter
    lateinit var serverRepository: ServerRepository
    lateinit var serverViewModelFactory: ServerViewModelFactory
    lateinit var serverViewModel: ServerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServersBinding.inflate(inflater,container,false)

        serversList = ArrayList()
        serverRepository = ServerRepository()
        serverViewModelFactory = ServerViewModelFactory(serverRepository)
        serverViewModel = ViewModelProvider(this,serverViewModelFactory)[ServerViewModel::class.java]
        serverAdapter = ServerAdapter()

        binding.apply{
            serverViewModel.getAllServers()
            serverViewModel.myResponseList.observe(requireActivity()){responseList ->
                serversList = responseList
                serverAdapter.submitList(serversList)
                recyclerServersList.adapter = serverAdapter
                recyclerServersList.layoutManager = LinearLayoutManager(requireContext())
            }
            btnAddServer.setOnClickListener {
                Constants.currentServer = Server(-1,0.0,0.0,"",0,0)
                Intent(requireContext(),ServerActivity::class.java).also{
                    startActivity(it)
                }
            }
        }

        return binding.root
    }

}