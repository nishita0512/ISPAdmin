package com.example.ispadmin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.ispadmin.activity.ServerActivity
import com.example.ispadmin.databinding.ServerSingleRowBinding
import com.example.ispadmin.model.Server
import com.example.ispadmin.utils.Constants

class ServerAdapter: ListAdapter<Server, RecyclerView.ViewHolder>(Diff) {

    class ServerViewHolder(private val binding: ServerSingleRowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(server: Server, context: Context){
            binding.apply{
                txtServerId.text = server.id.toString()
                parentLayoutServerSingleRow.setOnClickListener {
                    println("Ad Lat: ${server.latitude}")
                    Constants.currentServer = server
                    val intent = Intent(context, ServerActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ServerViewHolder(
            ServerSingleRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val server = getItem(position)
        val viewHolder = holder as ServerViewHolder
        viewHolder.bind(server,holder.itemView.context)
    }

    private object Diff : DiffUtil.ItemCallback<Server>() {
        override fun areItemsTheSame(oldItem: Server, newItem: Server): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Server, newItem: Server): Boolean {
            return oldItem == newItem
        }

    }

}