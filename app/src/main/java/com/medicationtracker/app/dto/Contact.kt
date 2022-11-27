package com.medicationtracker.app.dto

data class Contact(var id : String = "", var name: String = "", var phone : String = "", var email : String = ""){
    override fun toString() : String{
        return "$name $phone $email"
    }
}