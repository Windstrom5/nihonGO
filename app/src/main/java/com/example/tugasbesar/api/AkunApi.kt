package com.example.tugasbesar.api

class AkunApi {
    companion object{
        val BASE_URL = "http://192.168.1.4/server-nihongo/public/"

        val GET_ALL_URL = BASE_URL + "akun"
        val GET_BY_USERNAME = BASE_URL + "akun/"
        val ADD_URL = BASE_URL + "akun"
        val UPDATE_URL = BASE_URL + "akun/"
        val DELETE_URL = BASE_URL + "akun/"
    }
}