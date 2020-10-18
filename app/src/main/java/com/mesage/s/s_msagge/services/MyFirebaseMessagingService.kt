package com.mesage.s.s_msagge.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.health.UidHealthStats
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mesage.s.s_msagge.R
import com.mesage.s.s_msagge.giris_ekrani

var TOKEN: String? = null
var UID: String? = null

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        val bildirimbaslik = p0.notification?.title
        val bildirimBody = p0.notification?.body
        val baslik = p0.data.get("baslik")
        val mesaj = p0.data.get("mesaj")
        val uid =p0.data.get("uid")
        Log.e("FCM", "mesaj:$mesaj  baslik:$baslik")
        bildirimgonder(baslik, mesaj,uid)

        Log.e("FCM", bildirimbaslik + bildirimBody)
    }

    override fun onNewToken(Token: String?) {
        Log.e("TOKEN", Token)
        TokeniKaydet(Token!!)
    }

    private fun bildirimgonder(baslik: String?, mesaj: String?,uid:String?) {
        val bildirimid = 1
        val kanal_id = "SmSKanal"
        val name = getString(R.string.Kanaladi)
        val mesajintte=Intent(this,Yanitsarvice::class.java)
        val notificintt= Intent(this,giris_ekrani::class.java)
        val contentintetnt=PendingIntent.getActivity(this,0,notificintt,0)
        val mesajintt=PendingIntent.getService(this,0,mesajintte,0)
        val MKey="Mesaj"
        UID=uid
        val yanitblogu="Cevapla"
        val mesajgirisi=android.support.v4.app.RemoteInput.Builder(MKey)
                .setLabel(yanitblogu)
                .build()
        val action=NotificationCompat.Action.Builder(R.drawable.ic_send_cyan_900_36dp,yanitblogu,mesajintt
        )
                .addRemoteInput(mesajgirisi)
                .setAllowGeneratedReplies(true)
                .build()
        val builder = NotificationCompat.Builder(this, kanal_id)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(baslik)
                .setContentText(mesaj)
                .setColor(resources.getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.icepairlogo)
                .setStyle(NotificationCompat.BigTextStyle().bigText(mesaj))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .addAction(action)
        val noti=builder.build()
        noti.contentIntent=contentintetnt
        val notimanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val onemlilik = NotificationManager.IMPORTANCE_HIGH
            val benimkanalim = NotificationChannel(kanal_id, name, onemlilik)
            notimanager.createNotificationChannel(benimkanalim)
        }
        notimanager.notify(bildirimid, noti)
    }

    private fun TokeniKaydet(token: String) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Kullanicilar")
                    .child(FirebaseAuth.getInstance().uid!!)
                    .child("Token")
                    .setValue(token)
        } else {
            TOKEN = token
        }
    }

}
/* val bildirimid = 1
        val kanal_id = "SmSKanal"
        val name = getString(R.string.Kanaladi)
        val notificintt= Intent(this,giris_ekrani::class.java)
        val contentintetnt=PendingIntent.getActivity(this,0,notificintt,0)
        val builder = NotificationCompat.Builder(this, kanal_id)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(baslik)
                .setContentText(mesaj)
                .setSmallIcon(R.drawable.smeassgelogo)
                .setStyle(NotificationCompat.BigTextStyle().bigText(mesaj))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)

        val noti=builder.build()
        noti.contentIntent=contentintetnt
        val notimanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val onemlilik = NotificationManager.IMPORTANCE_HIGH
            val benimkanalim = NotificationChannel(kanal_id, name, onemlilik)
            notimanager.createNotificationChannel(benimkanalim)
        }
        notimanager.notify(bildirimid, noti)*/