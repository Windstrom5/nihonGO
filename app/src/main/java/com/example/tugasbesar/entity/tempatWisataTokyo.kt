package com.example.tugasbesar.entity

class tempatWisataTokyo(var name : String, var alamat : String, var rating : Double, val latitude: Double, val longtitude: Double) {
    companion object{
        @JvmField
        var listOftempatWisata = arrayOf(
            tempatWisataTokyo("Shinjuku Gyoen National Garden", "11 Naitomachi, Shinjuku City", 4.9,35.685272, 139.709442),
            tempatWisataTokyo("Sensō-ji Temple", "2 Chome-3-1 Asakusa, Taito", 4.9,35.714661,139.796783),
            tempatWisataTokyo("Tokyo Skytree"," 1 Chome-1-2 Oshiage, Sumida City",5.0,35.710064,139.810699),
            tempatWisataTokyo("Tsukiji Outer Market","4 Chome-16-2 Tsukiji, Chuo-ku",4.7,35.6568,139.7685),
            tempatWisataTokyo("Meiji Shrine","1-1 Yoyogikamizonocho, Shibuya City",4.3,35.675526,139.698578),
            tempatWisataTokyo("Imperial Palace","1-1 Chiyoda, Chiyoda City",4.3,35.685360,139.753372),
            tempatWisataTokyo("Disney Land Tokyo","1-1 Maihama, Urayasu, Chiba 279-0031",4.3,35.6263,139.8750),
            tempatWisataTokyo("Tokyo Tower"," 4 Chome-2-8 Shibakoen, Minato City",4.3,35.658581,139.745438)
        )
    }
}