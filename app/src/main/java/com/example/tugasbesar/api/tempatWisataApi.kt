package com.example.tugasbesar.api

class tempatWisataApi {
    companion object{
        val BASE_URL = "http://192.168.100.227/server-nihongo/public/"

        val GET_ALL_URL = BASE_URL + "tempatwisata"
        val GET_BY_NAMA_URL = BASE_URL + "tempatwisata/"
        val ADD_URL = BASE_URL + "tempatwisata"
        val UPDATE_URL = BASE_URL + "tempatwisata/"
        val DELETE_URL = BASE_URL + "tempatwisata/"
    }
}