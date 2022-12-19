package com.example.tugasbesar.api

class AkomodasiApi {
    companion object{
        val BASE_URL = "http://192.168.1.3/server-nihongo/public/"

        val GET_ALL_URL = BASE_URL + "akomodasi"
        val GET_BY_NAMA_URL = BASE_URL + "akomodasi/"
        val ADD_URL = BASE_URL + "akomodasi"
        val UPDATE_URL = BASE_URL + "akomodasi/"
        val DELETE_URL = BASE_URL + "akomodasi/"
    }
}