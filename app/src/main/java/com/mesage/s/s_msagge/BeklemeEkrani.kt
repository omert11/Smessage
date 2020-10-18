package com.mesage.s.s_msagge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BeklemeEkrani : AppCompatActivity() {
    var uid=""
    var Cins=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bekleme_ekrani)
         uid=FirebaseAuth.getInstance().currentUser!!.uid
         Cins=intent.extras.get("Cins") as String
        FirebaseDatabase.getInstance().getReference().child("Odalar").child(Cins)
                .child(uid).addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            if (p0.getValue()!=null&&p0.child("Bekleme").getValue()!=null){
                        val odaadi=p0.child("odaadi").getValue() as String
                        val durum =p0.child("Bekleme").getValue() as Boolean
                        if (durum){
                            odasil(Cins,uid)
                            videogoruntugit(odaadi,uid,Cins)}
                        }}
                    }

                })

    }

    override fun onDestroy() {
        super.onDestroy()
        odasil(Cins,uid)

    }
    private fun odasil(cins: String, uid: String) {
        FirebaseDatabase.getInstance().getReference().child("Odalar").child(cins)
                .child(uid).removeValue()
    }


    private fun videogoruntugit(odaadi:String,uid:String,Cins:String) {
        val intt= Intent(this,VideoSohbet::class.java)
        intt.putExtra("odaadi",odaadi)
        intt.putExtra("uid",uid)
        intt.putExtra("cins",Cins)
        startActivity(intt)
    }
}
