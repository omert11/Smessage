package com.mesage.s.s_msagge.services

import android.app.NotificationManager
import android.app.RemoteInput
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mesage.s.s_msagge.baseurl
import com.mesage.s.s_msagge.mesajdata
import com.mesage.s.s_msagge.serverkey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap

class Yanitsarvice:Service() {
    override fun onCreate() {
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var mesaj:CharSequence?=null
        Log.d("Service", "FirstService started")
        if (Gelenmesajial(intent)!=null){
            mesaj=Gelenmesajial(intent)
            if (UID!=null){
                Bilgilericek(UID!!,mesaj.toString())
                tokenalvebildirimolla(mesaj.toString())
            }else{
                Toast.makeText(this,"Mesaj Yollanamadi",Toast.LENGTH_SHORT).show()
            }

        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun Bilgilericek(uid: String,mesaj:String) {
        val BenimUid= FirebaseAuth.getInstance().currentUser!!.uid
        val Kod=Kodolustur(uid,BenimUid)
        FirebaseDatabase.getInstance().getReference()
                .child("Sohbetler")
                .child(Kod)
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                       val kaydets=p0.childrenCount.toInt()
                        kaydet(kaydets,Kod,mesaj)
                    }

                })
    }

    private fun kaydet(kaydets: Int,kod:String,mesaj:String) {
        val BenimUid= FirebaseAuth.getInstance().currentUser!!.uid
        val kaydedilecek=mesajdata(mesaj,BenimUid,false)
        FirebaseDatabase.getInstance().getReference()
                .child("Sohbetler")
                .child(kod)
                .child(kaydets.toString())
                .setValue(kaydedilecek)
        BildirimGuncelle()
        onDestroy()
    }

    override fun onDestroy() {
        Log.e("Service","Servis iptal")
        super.onDestroy()
    }
    private fun Kodolustur(uid: String, benimUid: String): String {
        val code1 = uid.hashCode()
        val code2 = benimUid.hashCode()
        val code = code1 + code2
        return code.toString()
    }

    private fun Gelenmesajial(intent: Intent?): CharSequence? {
       if (intent!=null) {
           val MKey="Mesaj"
           val UKey="UID"
        val remote=RemoteInput.getResultsFromIntent(intent)
        if (remote!=null){
            return remote.getCharSequence(MKey)
        }}
        return null
    }

    private fun BildirimGuncelle() {
        val notimanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notimanager.cancelAll()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private fun tokenalvebildirimolla(mesaj:String) {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(UID!!)
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val token=p0.child("Token").getValue() as String
                            val isim = p0.child("isim").getValue() as String
                            val isimk="Bilinmeyen"+isim.hashCode().toString()
                            bildirim(mesaj,isimk,token)
                        }
                    }

                })
    }
    fun bildirim(mesaj:String,baslik:String,token:String){
        val headerd = HashMap<String, String>()
        headerd.put("Content-Type", "application/json")
        headerd.put("Authorization", "key=" + serverkey)
        val data = fcmmodel.Data(mesaj, baslik, UID!!)
        val bildirim = fcmmodel(data, token)
        val etrofit = Retrofit.Builder().baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build()
        val int = etrofit.create(fcminterface::class.java)
        val istek = int.bildirimgonder(headerd, bildirim)
        istek.enqueue(object : Callback<Response<fcmmodel>> {
            override fun onFailure(call: Call<Response<fcmmodel>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Response<fcmmodel>>?, response: Response<Response<fcmmodel>>?) {

            }
        })
    }
}