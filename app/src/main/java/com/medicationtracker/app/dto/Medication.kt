package com.medicationtracker.app.dto

data class Medication(var id : String = "", var name: String = "", var doseAmount : Int = 0){
    override fun toString(): String {
        return "$name $doseAmount"
    }
}

