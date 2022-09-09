package com.example.tugasbesar.entity

class akomodasiTokyo(var name : String, var alamat : String, var rating : Double) {
    companion object{
        @JvmField
        var listOfakomodasi = arrayOf(
            akomodasiTokyo("Park Hyatt Tokyo", "3-7-1-2 Nishi-Shinjuku, Shinjuku-Ku", 5.0),
            akomodasiTokyo("ANA Intercontinental Hotel Tokyo", "1-12-33, Akasaka, Minato-ku, Tokyo", 4.9),
            akomodasiTokyo("Hotel Villa Fontaine","1-9-2 Higashi-shinbashi, Minato-ku, Tokyo",5.0),
            akomodasiTokyo("Apa Villa hotel Akasakamitsuke","19-10, 3, Akasaka, Minato-ku, Tokyo ",4.3),
            akomodasiTokyo("Akasaka Excel Hotel Tokyo","2-14-3, Nagata-cho, Chiyoda-ku, Tokyo",4.3)
        )
    }
}