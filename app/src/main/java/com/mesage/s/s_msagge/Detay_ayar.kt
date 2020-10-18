package com.mesage.s.s_msagge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_detay_ayar.*

class Detay_ayar : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    val ayarref = FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(uid)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detay_ayar)
        val secim = intent.extras.get("secim") as Int
        secimegorevisibayari(secim)
        buttonayarlari()
        toolbar4
        toolbar4.setTitleTextColor(resources.getColor(R.color.Beyaz))
        setSupportActionBar(toolbar4)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.beyaz_back))
    }

    private fun buttonayarlari() {
        cinsaspinner.adapter=ArrayAdapter.createFromResource(this,R.array.Cinsiyet,R.layout.support_simple_spinner_dropdown_item)
        yollabtn.setOnClickListener {
            val metin = Soruntw.text.toString()

            FirebaseDatabase.getInstance().getReference().child("Sorunlar").child(uid)
                    .child("sorun").setValue(metin)
            Toasty.success(this, "Bu sorun bildiriminiz için teşşekkür ederiz en kısa zamanda dönüş yapacağız.", Toast.LENGTH_LONG).show()
            val intt = Intent(this, AnaMenu::class.java)
            startActivity(intt)
            finish()
        }
        veritasarufusw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ayarref.child("veritasarufu").setValue(true)
            } else {
                ayarref.child("veritasarufu").setValue(false)
            }
        }
        Bildirimsw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ayarref.child("bildirim").setValue(true)
            } else {
                ayarref.child("bildirim").setValue(false)
            }
        }
        kaydetaydt.setOnClickListener {
          val r= FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid)
            r.child("isim").setValue(isimaydty.text.toString())
            r.child("cinsiyet").setValue(cinsaspinner.selectedItem as String)
            r.child("bio").setValue(bio.text.toString())
            Toasty.info(this,"Bilgileriniz kaydedildi",Toast.LENGTH_SHORT).show()
        }

    }

    private fun secimegorevisibayari(secim: Int) {
        when (secim) {
            0->{
                toolbar4.setTitle("Profili Düzenle")
                Dty_Profilduzenle.visibility=View.VISIBLE
                bilgicek(0)
            }
            1 -> {
                toolbar4.setTitle("Ödemeler")
                Dty_odemeler.visibility = View.VISIBLE
                bilgicek(1)
            }
            2 -> {
                Dty_verikullanimi.visibility = View.VISIBLE
                bilgicek(2)
                toolbar4.setTitle("Veri Kullanımı")
            }
            3 -> {
                toolbar4.setTitle("Engellenenler")
                Dty_engellenen.visibility = View.VISIBLE
                bilgicek(3)
            }
            4 -> {
                toolbar4.setTitle("Bildirimler")
                Dty_bildirim.visibility = View.VISIBLE
                bilgicek(4)
            }
            5 -> {
                toolbar4.setTitle("Yardım Merkezi")
                Dty_hesapyardım.visibility = View.VISIBLE
                bilgicek(5)

            }
            6 -> {
                toolbar4.setTitle("Sorun Bildir")
                Dty_sorunbildir.visibility = View.VISIBLE
                //bilgicek(6)

            }
        }
    }

    private fun bilgicek(i: Int) {
        when(i){
            0->{FirebaseDatabase.getInstance().getReference().child("Kullanicilar").
                    child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var bioc=""
                    if (p0.child("bio").exists()){
                        bioc=p0.child("bio").getValue() as String
                    }
                    val cins=p0.child("cinsiyet").getValue() as String
                    val isim =p0.child("isim").getValue() as String
                    if (cins=="Kadin"){
                        cinsaspinner.setSelection(0)
                    }else{
                        cinsaspinner.setSelection(1)
                    }
                    bio.setText(bioc)
                    isimaydty.setText(isim)
                }

            })}
            1->{ayarref.child("Odemeler").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val odemelist=ArrayList<String>()
                    if (p0.exists()){
                        for (odeme in p0.children){
                            odemelist.add(p0.getValue() as String)//Data tipinde de cekebilirsin
                        }
                        recolusturodemeler(odemelist)
                    }
                }

            }) }
            2->{ayarref.child("veritasarufu").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        veritasarufusw.isChecked=p0.getValue() as Boolean
                    }
                }

            }) }
            3->{ayarref.child("Engellenenler").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val uidlist=ArrayList<String>()
                    if(p0.exists()){
                        for (kullanici in p0.children){
                            uidlist.add(kullanici.getValue() as String)
                        }
                        recolusturEng(uidlist)
                    }
                }

            }) }
            4->{ayarref.child("bildirim").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                      Bildirimsw.isChecked=p0.getValue() as Boolean
                    }
                }

            }) }
            5->{ }//Katagorilerin getirilmesi ve atanmasi ustunde dusunulmesi gerek

        }
    }

    private fun recolusturodemeler(odemelist: ArrayList<String>) {

    }

    private fun recolusturEng(uidlist: ArrayList<String>) {
        val adp=EngRec(uidlist)
        Engrecw.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Engrecw.adapter=adp

    }
}
