package com.example.tugasbesar

import android.R.attr.x
import android.R.attr.y
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_maps.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import java.io.IOException


class MapsActivity : AppCompatActivity() {
    lateinit var vnama : String
    lateinit var valamat : String
    lateinit var mbunlde : Bundle
    var vlatitude:Double = 0.0
    var vlongtitude:Double = 0.0
    var modelMainList: MutableList<ModelMain> = ArrayList()
    lateinit var mapController: MapController
    lateinit var overlayItem: ArrayList<OverlayItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        getBundle()
        Configuration.getInstance().load(this,PreferenceManager.getDefaultSharedPreferences(this))
        val geoPoint = GeoPoint(vlatitude,vlongtitude)
        val startPoint = GeoPoint(x, y)
        mapView.setMultiTouchControls(true)
        mapView.controller.animateTo(geoPoint)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        mapController = mapView.controller as MapController
        mapController.setCenter(geoPoint)
        mapController.zoomTo(10)

        getLocationMarker()
    }


    private fun getLocationMarker(){
//        try{
//            val stream = assets.open("sample_maps.json")
//            val size = stream.available()
//            val buffer = ByteArray(size)
//            stream.read(buffer)
//            stream.close()
//            val strContent = String(buffer, StandardCharsets.UTF_8)
//            try{
//                val jsonObject = JSONObject(strContent)
//                val jsonArrayResult = jsonObject.getJSONArray("results")
//                for(i in 0 until jsonArrayResult.length()){
//                    val jsonObjectResult = jsonArrayResult.getJSONObject(i)
//                    val modelMain = ModelMain()
//                    modelMain.strName = jsonObjectResult.getString("name")
//                    modelMain.strAlamat = jsonObjectResult.getString("vicinity")
//
//                    //get lat long
//                    val jsonObjectGeo = jsonObjectResult.getJSONObject("geometry")
//                    val jsonObjectLoc = jsonObjectResult.getJSONObject("location")
//                    modelMain.latLoc = jsonObjectLoc.getDouble("lat")
//                    modelMain.longLoc = jsonObjectLoc.getDouble("lng")
//                }
//                initMarker(modelMainList)
//            }catch (e: JSONException){
//                e.printStackTrace()
//            }
//        }catch (ignored: IOException){
//            Toast.makeText(
//                this@MapsActivity,"Oops Ada Yang Tidak Beres. Coba Ulang Beberapa Saat Lagi",Toast.LENGTH_SHORT
//            )
//        }
        try{
            getBundle()
            val modelMain = ModelMain()
            modelMain.strName = vnama
            modelMain.strAlamat=valamat
            modelMain.latLoc = vlatitude
            modelMain.longLoc = vlongtitude

            modelMainList.add(modelMain)
            initMarker(modelMainList)
        }catch (ignored: IOException){
            Toast.makeText(
                this@MapsActivity,"Oops Ada Yang Tidak Beres. Coba Ulang Beberapa Saat Lagi",Toast.LENGTH_SHORT
            )
        }
    }

    private fun initMarker(modelList: List<ModelMain>){
        for(i in modelList.indices){
            overlayItem = ArrayList()
            overlayItem.add(
                OverlayItem(
                    modelList[i].strName, modelList[i].strAlamat, GeoPoint(modelList[i].latLoc, modelList[i].longLoc)
                )
            )
            val info = ModelMain()
            info.strName = modelList[i].strName
            info.strAlamat = modelList[i].strAlamat

            val marker = Marker(mapView)
            marker.icon = resources.getDrawable(R.drawable.ic_baseline_location_on_24)
            marker.position = GeoPoint(modelList[i].latLoc, modelList[i].longLoc)
            marker.relatedObject = info
            marker.infoWindow = CustomInfoWindow(mapView)
            marker.setOnMarkerClickListener{ item, arg1->
                item.showInfoWindow()
                true
            }
            val marker2 = Marker(mapView)

            marker2.position = GeoPoint(35.658581, 139.745438)
            marker2.setOnMarkerClickListener{ item, arg1->
                item.showInfoWindow()
                true
            }
            mapView.overlays.add(marker)
            mapView.overlays.add(marker2)
            mapView.invalidate()
        }
    }

    public override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        if (mapView != null){
            mapView.onResume()
        }
    }

    public override fun onPause() {
        super.onPause()
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        if (mapView != null){
            mapView.onPause()
        }
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("Location")!!
            if(mbunlde != null){
                vnama = mbunlde.getString("Nama")!!
                valamat = mbunlde.getString("Alamat")!!
                vlatitude = mbunlde.getDouble("latitude")!!
                vlongtitude = mbunlde.getDouble("longtitude")!!
            }else{

            }
        }catch (e: NullPointerException){
            vnama = ""
            valamat = ""
            vlatitude = 0.0
            vlongtitude = 0.0
        }
    }
}