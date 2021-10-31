package com.dktechnology.firebase

//model class
class Data(val userName: String, val userMessage: String, val timestamp: Long){

    constructor() : this("","",0){}
}
