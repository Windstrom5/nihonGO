package com.example.tugasbesar

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.tugasbesar.databinding.ActivityAddTiketBinding
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
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class addTiket : AppCompatActivity() {
    private var binding: ActivityAddTiketBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTiketBinding.inflate(layoutInflater)
        val view: View=binding!!.root
        setContentView(view)
        binding!!.buttonSave.setOnClickListener {
            val namaTempat = binding!!.etNamaTempat.text.toString()
            val alamat = binding!!.etAlamat.text.toString()
            val namaPembeli = binding!!.etNamaPembeli.text.toString()
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (namaTempat.isEmpty() && alamat.isEmpty() && namaPembeli.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Semuanya Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        createPdf(namaTempat, alamat, namaPembeli)
                    }
                }
            }catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
    }

    private fun createPdf(namaTempat: String, alamat: String, namaPembeli: String) {
        val pdfPath =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "Tiket.pdf")
        FileOutputStream(file)

        val writer =PdfWriter(file)
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
        table.addCell(Cell().add(Paragraph("Alamat Destiasi")))
        table.addCell(Cell().add(Paragraph(alamat)))
        table.addCell(Cell().add(Paragraph("Nama Pembeli")))
        table.addCell(Cell().add(Paragraph(namaPembeli)))
        table.addCell(Cell().add(Paragraph("Alamat Domisili")))
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
            "PDF Successfully Create!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
    }
}