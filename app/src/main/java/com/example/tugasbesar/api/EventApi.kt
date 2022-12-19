package com.example.tugasbesar.api

class EventApi {
    companion object{
        val BASE_URL = "http://192.168.1.3/server-nihongo/public/"

        val GET_ALL_URL = BASE_URL + "event"
        val GET_BY_NAMA_URL = BASE_URL + "event/"
        val ADD_URL = BASE_URL + "event"
        val UPDATE_URL = BASE_URL + "event/"
        val DELETE_URL = BASE_URL + "event/"
    }
}