package com.example.tugasbesar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.tugasbesar.databinding.ActivityMainBinding
import com.example.tugasbesar.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Tokyo : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    val GROUP = "com.example.tugasbesar"
    private val notificationId2 = 102
    val GROUP_KEY_WORK_EMAIL = "com.android.example.tugasbesar"
    val SUMMARY_ID = 0

    private lateinit var botNav : BottomNavigationView
    lateinit var vuser : String
    lateinit var mbunlde : Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokyo)

        createNotificationChannel()
        sendNotification1()

        changeFragment(FragmentTempatWisata())
        botNav = findViewById(R.id.botNav)
        botNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuWisata -> {
                    changeFragment(FragmentTempatWisata())
                    true
                }R.id.menuAkomodasi-> {
                changeFragment(FragmentAkomodasi())
                true
            }R.id.menuKuliner-> {
                changeFragment(FragmentKuliner())
                true
            }R.id.menuEvent->{
                changeFragment(FragmentEvent())
                true
            }else -> false

            }
        }

    }

    fun changeFragment(fragment: Fragment?) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.menu.and(R.menu.menu2) ,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menulogout) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@Tokyo)
            builder.setMessage("Wanna Logout Ma Nibba?")
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {

                    }
                })
                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        finishAndRemoveTask()

                    }
                })
                .show()
        }
        else if(item.itemId == R.id.menuKota) {
            val intent = Intent(this, kota::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.menuProfile){
            getBundle()
            val intent = Intent(this, profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
        }

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification1(){
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage","welcome",)
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notif1 = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Recommended Place For You in TOKYO!!")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("1. Tokyo SkyTree \n" +
                        "2. Tokyo Disney Land \n" +
                        "3. Tokyo Ginza \n" +
                        "4. Marunouchi \n" +
                        "5. Asakusa \n" +
                        "6. Senso-ji \n" +
                        "7. Roppongi Hills \n" +
                        "8. Tokyo Tower \n" +
                        "9. Tokyo Museums \n" +
                        "10. Shibuya Crossings \n"))

            .setGroup(GROUP_KEY_WORK_EMAIL)
            .build()

        val notif2 = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.message_arigatou)
            .setContentTitle("Welcome!!")
            .setContentText("Explore the streets of Tokyo :)")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.message_arigatou)))
            .setGroup(GROUP_KEY_WORK_EMAIL)
            .build()

        val summaryNotif = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setContentText("You have two message")
            .setSmallIcon(R.drawable.message_arigatou)
            .setStyle(NotificationCompat.InboxStyle()
                .addLine("Recommended Place For You in TOKYO!!")
                .addLine("Welcome!!")
                .setBigContentTitle("2 new messages"))
            .setGroup(GROUP_KEY_WORK_EMAIL)
            .setGroupSummary(true)
            .build()

        NotificationManagerCompat.from(this).apply {
            notify(notificationId1,notif1)
            notify(notificationId2,notif2)
            notify(SUMMARY_ID, summaryNotif)
        }
    }
}