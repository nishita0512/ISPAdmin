package com.example.ispadmin.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ispadmin.R
import com.example.ispadmin.adapters.SupportMessagesAdapter
import com.example.ispadmin.databinding.ActivityChatBinding
import com.example.ispadmin.model.SupportMessage
import com.example.ispadmin.utils.Constants
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var firebaseClient: FirebaseFirestore
    lateinit var sharedPreferences: SharedPreferences
    lateinit var messagesList: ArrayList<SupportMessage>
    lateinit var supportMessagesAdapter: SupportMessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseClient = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("ISP", AppCompatActivity.MODE_PRIVATE)
        messagesList = ArrayList<SupportMessage>()
        supportMessagesAdapter = SupportMessagesAdapter()

        binding.apply {

            txtCustomerNameSupportFragment.text = Constants.currentCustomer.name

            firebaseClient.collection(Constants.currentCustomer.custId.toString()).orderBy("dateAndTime").get().addOnSuccessListener {
                val documents = it.documents
                for(document in documents){
                    val message = document["message"].toString()
                    val dateAndTime = document["dateAndTime"] as Long
                    val messageFrom = document["messageFrom"].toString()
                    messagesList.add(SupportMessage(message, dateAndTime, messageFrom))
                }
                setValues()
                supportFragmentProgressBarLayout.visibility = View.GONE
            }

            btnSendMessageSupport.setOnClickListener {btnSend ->
                btnSendMessageSupportProgressBar.visibility = View.VISIBLE
                btnSend.isEnabled = false

                val message = edtTxtSupportMessage.text.toString()

                firebaseClient.collection(Constants.currentCustomer.custId.toString()).orderBy("dateAndTime").get().addOnSuccessListener {
                    val lastMessageId = if(it.documents.isNotEmpty()){
                        it.documents[it.documents.lastIndex].id.toInt()
                    } else{
                        1
                    }
                    val currentMessageId = (lastMessageId+1).toString()
                    val date = Calendar.getInstance().time.time
                    val messageHashmap = HashMap<String,Any>()
                    messageHashmap["message"] = message
                    messageHashmap["dateAndTime"] = date
                    messageHashmap["messageFrom"] = "admin"
                    firebaseClient.collection(Constants.currentCustomer.custId.toString()).document(currentMessageId).set(messageHashmap as Map<String, Any>).addOnSuccessListener {
                        edtTxtSupportMessage.text.clear()
                        btnSend.isEnabled = true
                        btnSendMessageSupportProgressBar.visibility = View.GONE
                    }
                }
            }

        }

        firebaseClient.collection(Constants.currentCustomer.custId.toString()).addSnapshotListener { value, error ->
            if(error==null){
                val documents = value?.documents!!
                messagesList.clear()
                for(document in documents){
                    val message = document["message"].toString()
                    val dateAndTime = document["dateAndTime"] as Long
                    val messageFrom = document["messageFrom"].toString()
                    messagesList.add(SupportMessage(message, dateAndTime, messageFrom))
                }
                messagesList.sortBy {
                    it.dateAndTime
                }
                setValues()
            }
        }

    }

    private fun setValues() {
        val linearLayoutManager = LinearLayoutManager(this@ChatActivity)
        linearLayoutManager.stackFromEnd = true

        addDateChips()
        supportMessagesAdapter.submitList(messagesList)

        binding.apply {
            recyclerSupportMessages.apply{
                layoutManager = linearLayoutManager
                adapter = supportMessagesAdapter
                scrollToPosition(messagesList.size-1)
            }
        }

    }

    private fun addDateChips(){
        if(messagesList.size>0){

            messagesList.add(0, SupportMessage("",messagesList[0].dateAndTime,""))

            var messagesListSize = messagesList.size
            var i = 2

            while(i<messagesListSize) {

                if((messagesListSize==1)){
                    break
                }

                if((messagesList[i-1].messageFrom!="") and (messagesList[i].messageFrom!="")){

                    val previousMessageDate = (SimpleDateFormat("dd", Locale.getDefault()).format(Date(messagesList[i-1].dateAndTime))).toInt()
                    val previousMessageMonth = (SimpleDateFormat("MM", Locale.getDefault()).format(Date(messagesList[i-1].dateAndTime))).toInt()
                    val previousMessageYear = (SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(messagesList[i-1].dateAndTime))).toInt()

                    val date = (SimpleDateFormat("dd", Locale.getDefault()).format(Date(messagesList[i].dateAndTime))).toInt()
                    val month = (SimpleDateFormat("MM", Locale.getDefault()).format(Date(messagesList[i].dateAndTime))).toInt()
                    val year = (SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(messagesList[i].dateAndTime))).toInt()


                    if((year!=previousMessageYear) or (month!=previousMessageMonth) or (date!=previousMessageDate)){
                        println("i $i lastIndex $messagesListSize preDate $previousMessageDate date $date message ${messagesList[i].message}")
                        messagesList.add(i, SupportMessage("",messagesList[i].dateAndTime,""))
                        messagesListSize++
                    }
                }

                i++

            }
        }
    }

}