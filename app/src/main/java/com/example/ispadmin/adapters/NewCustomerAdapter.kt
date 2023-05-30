package com.example.ispadmin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.ispadmin.activity.ChatActivity
import com.example.ispadmin.activity.CustomerActivity
import com.example.ispadmin.databinding.CustomerSingleRowBinding
import com.example.ispadmin.model.Customer
import com.example.ispadmin.utils.Constants

class NewCustomerAdapter: ListAdapter<Customer, RecyclerView.ViewHolder>(Diff) {

    class CustomerViewHolder(private val binding: CustomerSingleRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(customer: Customer, context: Context){
            binding.apply{
                txtCustomerName.text = customer.name
                parentLayoutCustomerSingleRow.setOnClickListener {
                    Constants.currentCustomer = customer
                    val intent = Intent(context, CustomerActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomerViewHolder(
            CustomerSingleRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val customer = getItem(position)
        val viewHolder = holder as CustomerViewHolder
        viewHolder.bind(customer,holder.itemView.context)
    }

    private object Diff : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }

    }

}