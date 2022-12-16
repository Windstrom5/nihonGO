package com.example.tugasbesar

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.databinding.ActivityDetailsBinding
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DetailsActivity : AppCompatActivity() {
    private lateinit var mbunlde : Bundle
    private lateinit var namaWisata : String
    private lateinit var nama: TextView
    private lateinit var rating: TextView
    private lateinit var alamat: TextView
    private lateinit var hargaTiket: TextView
    private lateinit var loading : LinearLayout
    private var queue: RequestQueue? = null
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var vcity : String
    lateinit var vnama : String
    lateinit var valamat : String
    lateinit var vrating : String
    lateinit var vcategory : String
    lateinit var vprice : String
    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = findViewById(R.id.layout_loading)
        queue= Volley.newRequestQueue(this)
        getBundle()
        nama = binding.namaWisata
        alamat = binding.alamatWisata
        rating = binding.ratingWisata
        hargaTiket = binding.hargaTiket
        nama.setText(vnama)
        alamat.setText(valamat)
        hargaTiket.setText(vprice)
        rating.setText(vrating)
        Log.d("Apa Coba",vnama)
//        getData(vnama)
        button_ticket.setOnClickListener(){
            createPdf(vnama,valamat,vuser,vcategory)
        }

        button_location.setOnClickListener(){
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,itemActivity::class.java)
        val mBundle = Bundle()
        mBundle.putString("username",vuser)
        mBundle.putString("password",vpass)
        mBundle.putString("city", vcity)
        mBundle.putString("category",vcategory)
        intent.putExtra("profile",mBundle)
        startActivity(intent)
    }

//    private fun setData(nama: String,alamat: String,rating: String,price: String){
//        binding.namaWisata.setText(nama)
//        binding.alamatWisata.setText(alamat)
//        binding.ratingWisata.setText(rating)
//        binding.hargaTiket.setText(price)
//    }

//    private fun getData(nama: String){
//        setLoading(true)
//        val StringRequest: StringRequest = object
//            : StringRequest(
//            Method.GET, tempatWisataApi.GET_BY_NAMA_URL + nama ,
//            Response.Listener { response->
//                val gson = Gson()
//                val jsonObject = JSONObject(response)
//                val jsonArray = jsonObject.getJSONArray("data")
//                for (i in 0 until jsonArray.length()) {
//                    val data = jsonArray.getJSONObject(i)
//                    binding.namaWisata.setText(data.getString("nama"))
//                    binding.alamatWisata.setText(data.getString("detail"))
//                    binding.ratingWisata.setText(data.getString("lokasi"))
//                    binding.hargaTiket.setText(data.getString("harga"))
//                    setLoading(false)
//                }
//            }, Response.ErrorListener { error->
//                setLoading(false)
//            }
//        ){
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String,String>()
//                headers["Accept"] = "application/json"
//                return headers
//            }
//        }
//        queue!!.add(StringRequest)
//    }

    fun setLoading(isLoading:Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            loading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            loading!!.visibility = View.INVISIBLE
        }
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vcity = mbunlde.getString("city")!!
                vnama = mbunlde.getString("nama")!!
                valamat = mbunlde.getString("alamat")!!
                vrating = mbunlde.getString("rating")!!
                vprice = mbunlde.getString("price")!!
                vcategory = mbunlde.getString("category")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vcity = ""
            vnama = ""
            valamat = ""
            vrating = ""
            vprice = ""
        }
    }

    private fun createPdf(namaTempat: String, alamat: String, namaPembeli: String,category : String) {
        val pdfPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Tiket.pdf")
        FileOutputStream(file)

        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f,5f,5f,5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.logo)

        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val tempat = Paragraph("Tiket Tempat Pariwisata").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)

        val group = Paragraph(
            """
                           Berikut adalah
                           Tiket Tempat Pariwisata
                           """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        val width = floatArrayOf(100f,100f)
        val table = Table(width)

        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Tempat Wisata")))
        table.addCell(Cell().add(Paragraph(namaTempat)))
        table.addCell(Cell().add(Paragraph("Jenis Wisata")))
        table.addCell(Cell().add(Paragraph(category)))
        table.addCell(Cell().add(Paragraph("Nama Pembeli")))
        table.addCell(Cell().add(Paragraph(namaPembeli)))
        table.addCell(Cell().add(Paragraph("Alamat Destinasi")))
        table.addCell(Cell().add(Paragraph(alamat)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Tiket Dibuat")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Waktu Tiket Dibuat")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """"
                                        $namaTempat
                                        $alamat
                                        $namaPembeli
                                        ${LocalDate.now().format(dateTimeFormatter)}
                                        ${LocalTime.now().format(timeFormatter)}
                                        """.trimIndent())
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
            HorizontalAlignment.CENTER)

        document.add(image)
        document.add(tempat)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        MotionToast.Companion.darkToast(this,
            "Created",
            "Ticket Have Been Generate!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
    }
}