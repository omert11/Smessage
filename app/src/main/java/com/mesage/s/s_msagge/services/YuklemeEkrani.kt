package com.mesage.s.s_msagge.services

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mesage.s.s_msagge.*
import kotlinx.android.synthetic.main.activity_yukleme_ekrani.*

class YuklemeEkrani : AppCompatActivity() {
    val dataref = FirebaseDatabase.getInstance().getReference()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    val storaref = FirebaseStorage.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yukleme_ekrani)
        yuklemeislemibaslat()
    }

    private fun yuklemeislemibaslat() {
        yuk_bilg.setText("Resminizi yüklüyoruz.")
        durumasamasi(80)
        resimyukle()
    }

    private fun resimyukle() {
        storaref.child("ProfilRes").child(uid!!).child("Profil.jpg").downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                uri = it.result!!
                kulllanicibilgicek()
            }
        }

    }

    private fun kulllanicibilgicek() {
        yuk_bilg.setText("Bilgilerinizi yüklüyoruz.")
        durumasamasi(64)
        dataref.child("Kullanicilar").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val datag = p0.getValue(kullanici::class.java)
                if (datag != null) {
                    data = datag
                }
                sohbetbilgilerinicek()
            }
        })
    }

    private fun sohbetbilgilerinicek() {
        yuk_bilg.setText("Sohbetlerinizi yüklüyoruz.")
        durumasamasi(48)
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists() && p0.child("Seviye").getValue() != null) {
                            konusulansayisi = p0.child("GidenKonusma").childrenCount.toInt()
                            val sevy = p0.child("Seviye").getValue() as String
                            sev = sevy.toInt()

                        }
                        konustuklarimcek()
                    }

                })
    }

    private fun konustuklarimcek() {
        durumasamasi(32)
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("GelenKonusma").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (kisileruid in p0.children) {
                                val uid = kisileruid.key
                                konustuklarim.add(uid!!)
                            }
                        }
                    }

                })
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("GidenKonusma").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (kisileruid in p0.children) {
                                val uid = kisileruid.key
                                konustuklarim.add(uid!!)
                            }
                        }
                    }

                })
        FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Engellenenler").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (kullanici in p0.children) {
                                val uid = kullanici.getValue() as String
                                konustuklarim.add(uid)

                            }

                        }
                        predurumcek()
                    }

                })
    }

    private fun predurumcek() {
        yuk_bilg.setText("Premiumu kontrol ediyoruz.")
        durumasamasi(16)
        dataref.child("Kullanicilar").child(uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            predurum = p0.child("Predurum").getValue() as Boolean
                            kalangun = p0.child("Kalangun").getValue() as String

                        }
                        anamenuyolla()
                    }

                }
        )
    }

    private fun anamenuyolla() {
        val intt = Intent(this, AnaMenu::class.java)
        startActivity(intt)
        finish()
    }

    private fun durumasamasi(marginson: Int) {
        val oankimargin = yuk_asama.layoutParams
        var degisen = marginson
        object : CountDownTimer(160, 10) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                degisen = degisen - 1
                oankimargin.height=degisen
                yuk_asama.layoutParams = oankimargin
            }

        }

    }
}
