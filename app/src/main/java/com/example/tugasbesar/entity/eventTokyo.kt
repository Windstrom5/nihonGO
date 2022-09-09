package com.example.tugasbesar.entity

class eventTokyo(var name : String, var alamat : String, var tanggal : String) {
    companion object{
        @JvmField
        var listOfevent= arrayOf(
            eventTokyo("Earth Day Tokyo 2022", "Yoyogi Park", "16th-17th April 2022"),
            eventTokyo("Kanagawa Shimbun Fireworks Festival", "Minato Mirai area", "early in August"),
            eventTokyo("2022 Tokyo Game Show","Chiba’s Makuhari Messe","15-18 September 2022")
        )
    }
}