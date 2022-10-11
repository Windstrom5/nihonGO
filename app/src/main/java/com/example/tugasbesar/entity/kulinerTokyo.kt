package com.example.tugasbesar.entity

class kulinerTokyo(var name : String, var alamat : String, var rating : Double) {
    companion object{
        @JvmField
        var listOfkuliner= arrayOf(
            kulinerTokyo("Yakumo Saryō", "3-chōme-4-7 Yakumo, Meguro City", 4.0),
            kulinerTokyo("Kozue", "1-12-33, Akasaka, Minato-ku", 4.9),
            kulinerTokyo("Bar Benfiddich","〒160-0023 Tōkyō-to, Shinjuku-ku, Nishishinjuku, 1 Chome−13, 西新宿1ｰ13ｰ7",4.9),
            kulinerTokyo("Isetan Shinjuku","新宿3-14-1, Shinjuku",5.0)
        )
    }
}