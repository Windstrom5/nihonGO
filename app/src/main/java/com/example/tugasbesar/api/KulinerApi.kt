package com.example.tugasbesar.api

class KulinerApi {
    companion object{
        val BASE_URL = "http://192.168.1.5/server-nihongo/public/"

        val GET_ALL_URL = BASE_URL + "kuliner"
        val GET_BY_NAMA_URL = BASE_URL + "kuliner/"
        val ADD_URL = BASE_URL + "kuliner"
        val UPDATE_URL = BASE_URL + "kuliner/"
        val DELETE_URL = BASE_URL + "kuliner/"
    }
}