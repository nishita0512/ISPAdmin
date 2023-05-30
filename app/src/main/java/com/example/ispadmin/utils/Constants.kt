package com.example.ispadmin.utils

import com.example.ispadmin.model.Customer
import com.example.ispadmin.model.Server
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Constants {

    const val BASE_URL = "http://192.168.164.98:3000/"
//    const val BASE_URL = "http://103.173.240.12:3000/"
    var currentCustomer = Customer(-1,"","","",0.00,0.00,"","",0,0,0,0,0)
    var currentServer = Server(-1,0.0,0.0,"",0,0)

    fun sha256(input: String): String {
        val bytes = input.toByteArray(StandardCharsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}