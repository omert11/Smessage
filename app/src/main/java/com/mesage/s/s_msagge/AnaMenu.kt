package com.mesage.s.s_msagge

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.mesage.s.s_msagge.services.sohbetdata
import com.soundcloud.android.crop.Crop
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_ana_menu.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var sev = 1
val baseurl = "https://fcm.googleapis.com/fcm/"
var serverkey: String? = null
var predurum: Boolean = true
var kalangun = ""
var uri: Uri? = null
var data = kullanici()
var konusulansayisi = 0
val konustuklarim = ArrayList<String>()

class AnaMenu : AppCompatActivity() {
    val dataref = FirebaseDatabase.getInstance().getReference()
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    val storaref = FirebaseStorage.getInstance().getReference()
    val urilist = ArrayList<Uri>()
    var secim = ""
    var durumlar = aramaclass(true, false, false)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.arkadaslar -> {
                visiata(arkadaslay)
                return@OnNavigationItemSelectedListener true
            }
            R.id.Anamenu -> {
                visiata(Anamenu)
                return@OnNavigationItemSelectedListener true
            }
            R.id.messajlar -> {
                visiata(mesajlarlay)
                return@OnNavigationItemSelectedListener true

            }
            R.id.profil -> {
                visiata(profillay)
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

    private fun visiata(gelenlayout: View) {
        val gorunmez = View.INVISIBLE
        haftaninenlerilay.visibility = gorunmez
        arkadaslay.visibility = gorunmez
        Anamenu.visibility = gorunmez
        mesajlarlay.visibility = gorunmez
        profillay.visibility = gorunmez
        gelenlayout.visibility = View.VISIBLE

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_menu)
        anamenubilgicek()
        predurumata()
        anamenuicinayarlar()
        Sohbetlistener()
        Profilresimlerinicek()
        Seviyebariduzenle()
        Profilistatistikcek()
        profilactionmenu()
        birinibulayarlari()
        Songorulmeolayi()
        Sohbetcek()
        begenicek()
        ArkasRecIcinKontrol()
        istatistikcek()
        serverkeyolustur()
        navigation.selectedItemId = R.id.Anamenu
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun Profilistatistikcek() {
        /* dataref.child("Kullanicilar").child(uid!!).addListenerForSingleValueEvent(object :ValueEventListener{
             override fun onCancelled(p0: DatabaseError) {

             }

             override fun onDataChange(p0: DataSnapshot) {
                 var gelensayi=0
                 var gidensayi=0
                 if(p0.child("bio").exists()){
                     profilbioy.setText(p0.child("bio").getValue() as String)
                 }
                 if (p0.child("GelenKonusma").exists()){
                     gelensayi=p0.child("GelenKonusma").childrenCount.toInt()}
                 if (p0.child("GidenKonusma").exists()){
                     gidensayi=p0.child("GidenKonusma").childrenCount.toInt()
                 }
                 val toplamkonusma=gelensayi+gidensayi
                 val tec= p0.child("Tecrube").getValue() as String
                 val mesajsay=(tec.toInt())/5
                 val begeni=p0.child("Like").getValue() as String
                 profilmenuToplamK.setText(toplamkonusma.toString())
                 profilmenuToplamBegeni2.setText(begeni)
                 profilmenuToplamMesaj2.setText(mesajsay.toString())
             }

         })*/
        dataref.child("Kullanicilar").child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("bio").exists()) {
                    profilbioy.setText(p0.child("bio").getValue() as String)
                }
                if (p0.child("yil").exists()) {
                    val yil = p0.child("yil").getValue() as String
                    val yas = 2019 - yil.toInt()
                    profilyasy.setText(yas.toString())
                }

            }
        })
    }

    private fun profilactionmenu() {
        /*val menuolusturucu = SubActionButton.Builder(this)
        val itemicon = ImageView(this)
        val itemicon2 = ImageView(this)
        val itemicon3 = ImageView(this)
        itemicon2.setImageResource(R.drawable.ic_add_a_photo_black_24dp)
        itemicon.setImageResource(R.drawable.ic_insert_photo_black_24dp)
        itemicon3.setImageResource(R.drawable.ic_person_pin_black_24dp)
        val button1 = menuolusturucu.setContentView(itemicon).build()

        val btn1 = button1.layoutParams
        btn1.width = 150
        btn1.height = 150
        button1.setOnClickListener { ProfilResimYuk("Pm")
        }
        val button2 = menuolusturucu.setContentView(itemicon2).build()
        button2.setOnClickListener { ProfilResimYuk("P") }
        val btn2 = button2.layoutParams
        btn2.width = 150
        btn2.height = 150
        val button3 = menuolusturucu.setContentView(itemicon3).build()
        button3.setOnClickListener { ayarla() }
        val btn3 = button3.layoutParams
        btn3.width = 150
        btn3.height = 150
        FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(ActionButnn)
                .build()*/
        profilresimekle.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    profilresimekle.elevation = 10F
                }
                MotionEvent.ACTION_UP -> {
                    profilresimekle.elevation = 15F
                    ProfilResimYuk("Pm")
                }
                MotionEvent.ACTION_CANCEL -> {
                    profilresimekle.elevation = 15F
                }
            }
            return@setOnTouchListener true
        }
        profilprofilres.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            profilprofilres.elevation = 10F
                        }
                        MotionEvent.ACTION_UP -> {
                            profilprofilres.elevation = 15F
                            ProfilResimYuk("P")
                        }
                        MotionEvent.ACTION_CANCEL -> {
                            profilprofilres.elevation = 15F
                        }
                    }
                    return@setOnTouchListener true
                }
        profilayarlar.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            profilayarlar.elevation = 10F
                        }
                        MotionEvent.ACTION_UP -> {
                            profilayarlar.elevation = 15F
                            ayarla()                        }
                        MotionEvent.ACTION_CANCEL -> {
                            profilayarlar.elevation = 15F
                        }
                    }
                    return@setOnTouchListener true
                }
        Profilpre.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Profilpre.elevation = 10F
                }
                MotionEvent.ACTION_UP -> {
                    Profilpre.elevation = 15F
                    prefragmentac()
                }
                MotionEvent.ACTION_CANCEL -> {
                    Profilpre.elevation = 15F
                }
            }
            return@setOnTouchListener true
        }

    }

    private fun prefragmentac() {
        val fragment=prekesfet()
        fragment.show(supportFragmentManager,"pre")
    }

    private fun ayarla() {
        val intt = Intent(this, Ayarlar::class.java)
        startActivity(intt)
    }

    private fun ProfilResimYuk(Sec: String) {
        Crop.pickImage(this)
        when (Sec) {
            "Pm" -> {
                secim = "Pm"
            }
            "P" -> {
                secim = "P"
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            kesimdenonce(data!!.data)
        } else if (requestCode == Crop.REQUEST_CROP) {
            kesimiyakala(resultCode, data)
        }
    }

    private fun kesimiyakala(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            Resmiboyutlandir(Crop.getOutput(data))
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toasty.error(this, "Hata", Toast.LENGTH_SHORT).show()
        }
    }

    private fun kesimdenonce(data: Uri?) {
        val kesuri = Uri.fromFile(File(cacheDir, "cropped"))
        Crop.of(data, kesuri).asSquare().start(this)

    }

    private fun Resmiboyutlandir(uri: Uri?) {
        val boyutlandir = Resimskstir()
        boyutlandir.execute(uri)
    }

    inner class Resimskstir : AsyncTask<Uri, Void, ByteArray>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            uploadoncesidonustur(result!!)
        }

        override fun doInBackground(vararg params: Uri?): ByteArray? {
            val bitmap = MediaStore.Images.Media.getBitmap(this@AnaMenu.contentResolver, params[0])
            var resimByts: ByteArray? = null
            for (i in 1..5) {
                resimByts = bitmaptanbyte(bitmap, 100 / i)
            }
            return resimByts
        }

        private fun bitmaptanbyte(bitmap: Bitmap, i: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, i, stream)
            return stream.toByteArray()
        }

    }

    private fun uploadoncesidonustur(result: ByteArray) {
        val resimuri = result
        when (secim) {
            "Pm" -> {
                Resimlerisay(resimuri)
            }
            "P" -> {
                FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uid!!)
                        .child("Profil.jpg").putBytes(result)
                anamenubilgicek()
            }
        }

    }

    private fun Resimlerisay(resimuri: ByteArray) {
        dataref.child("Kullanicilar").child(uid!!).child("Resimsay")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val sayi = p0.getValue() as String
                            Resimyukle(resimuri, sayi)
                        } else {
                            Resimyukle(resimuri, "1")
                        }
                    }

                })
    }

    private fun Resimyukle(resimuri: ByteArray, sayi: String) {
        val arttir = sayi.toInt() + 1
        FirebaseStorage.getInstance().getReference().child("ProfilRes")
                .child(uid!!).child("Resimlerim").child(sayi + ".jpg").putBytes(resimuri)
                .addOnCompleteListener {
                    Toasty.success(this, "Resim yükleme işlemi başarılı", Toast.LENGTH_SHORT).show()
                    dataref.child("Kullanicilar").child(uid).child("Resimsay")
                            .setValue(arttir.toString())
                }
    }

    private fun Profilresimlerinicek() {
        dataref.child("Kullanicilar").child(uid).child("Resimsay").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val resimsay = p0.getValue() as String
                    rsimdongusu(resimsay.toInt())
                } else {
                    Resimleriyukle()
                }
            }

        })
    }

    private fun rsimdongusu(toInt: Int) {
        urilist.clear()
        if (toInt == 0) {
            urilist.add(uri!!)
        }
        for (dongu in 1..toInt) {
            FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uid!!).child("Resimlerim")
                    .child(dongu.toString() + ".jpg").downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    val eklenecekuri = it.result!!
                    urilist.add(eklenecekuri)
                    Resimleriyukle()

                }
            }
        }
    }

    private fun Resimleriyukle() {
        val adpt = Sayfaadapter(urilist)
        sayfacev2.adapter = adpt
        indicator2.setViewPager(sayfacev2)
    }

    private fun begenicek() {
        /*  dataref.child("Kullanicilar").child(uid!!).child("Like").addValueEventListener(
                  object : ValueEventListener {
                      override fun onCancelled(p0: DatabaseError) {

                      }

                      override fun onDataChange(p0: DataSnapshot) {
                          if (p0.exists()) {
                              val begenisayisi = p0.getValue() as String
                              Anamenubegeni.setText(begenisayisi)
                          } else {
                              Anamenubegeni.setText("0")
                          }
                      }

                  }
          )*/
    }

    private fun predurumata() {
        if (predurum) {
            predurumtx.setText("Premium Kullanıcı($kalangun)")
            predurumtx.setTextColor(resources.getColor(R.color.altin))
            durumlar.Konum = true
        } else {
            predurumtx.setText("Bedava Hesap")
            predurumtx.setTextColor(resources.getColor(R.color.colorPrimary))
        }
    }


    private fun serverkeyolustur() {
        dataref.child("Server").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                serverkey = p0.getValue() as String
            }

        })
    }

    private fun istatistikcek() {
        dataref.child("HaftaninEnleri").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val Alinanmesajsay = p0.child("Alinanmesajsayisi").getValue() as String
                    val Arkadassayisi = p0.child("Arkadassayisi").getValue() as String
                    val Toplammesajsayisi = p0.child("Toplammesajsayisi").getValue() as String
                    val Tecrube = p0.child("Tecrube").getValue() as String
                    HaftaninEnlerirecolustur(Alinanmesajsay, Arkadassayisi, Toplammesajsayisi, Tecrube)
                }
            }

        })
    }

    private fun HaftaninEnlerirecolustur(alinanmesajsay: String, arkadassayisi: String, toplammesajsayisi: String, tecrube: String) {
        val itemOnClick: (Int) -> Unit = { position ->
            when (position) {
                0 -> {
                    Haftaninenleribilgicek(toplammesajsayisi, getString(R.string.Hf_EmsjAtn))


                }
                1 -> {
                    Haftaninenleribilgicek(arkadassayisi, getString(R.string.Hf_EcAo))

                }
                2 -> {
                    Haftaninenleribilgicek(alinanmesajsay, getString(R.string.Hf_EcKa))

                }
                3 -> {
                    Haftaninenleribilgicek(tecrube, getString(R.string.Hf_EcTk))
                }

            }
        }
        var adpt = HaftaninenleriRec(alinanmesajsay, arkadassayisi, toplammesajsayisi, tecrube, itemOnClick)
        HaftaninenlerimenuRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        HaftaninenlerimenuRec.adapter = adpt

    }

    private fun Haftaninenleribilgicek(Uid: String, Kat: String) {
        dataref.child("Kullanicilar").child(Uid).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val isim = p0.child("isim").getValue() as String
                            var arkadassayisi = "0"
                            var Gelenmesajsayisi = "0"

                            if (p0.child("Istekler").exists()) {
                                arkadassayisi = p0.child("Istekler").childrenCount.toString()
                            }
                            val Tecrube = p0.child("Tecrube").getValue() as String
                            val Toplammesajsayisi = Tecrube.toInt() / 5
                            if (p0.child("GelenKonusma").exists()) {
                                Gelenmesajsayisi = p0.child("GelenKonusma").childrenCount.toString()
                            }
                            HaftaninenlerimenuArkadassayisi.setText(getString(R.string.Arkadassay) + arkadassayisi)
                            HaftaninenlerimenuGelenmesajsayisi.setText(getString(R.string.Gelenkonusmasay) + Gelenmesajsayisi)
                            HaftaninenlerimenuIsim.setText(isim)
                            Haftaninenlerimenutoplammesajsayisi.setText(getString(R.string.Toplammesjsay) + Toplammesajsayisi.toString())
                            HaftaninenlerimenuKatagori.setText(Kat)
                        }

                    }
                }
        )
    }


    private fun ArkasRecIcinKontrol() {
        dataref.child("Kullanicilar").child(uid!!).child("Istekler")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val arrayarkadas = ArrayList<arkadasdata>()
                        if (p0.exists()) {
                            for (arkadas in p0.children) {
                                val uid = arkadas.key!!
                                val durum = arkadas.getValue() as Boolean
                                val data = arkadasdata(uid, durum)
                                arrayarkadas.add(data)
                            }
                            recolusturarkadas(arrayarkadas)
                        }
                    }

                })
    }

    private fun recolusturarkadas(array: ArrayList<arkadasdata>) {
        val itemOnClick: (String, String) -> Unit = { uid, taraf ->
            val intt = Intent(this, Mesaj::class.java)
            intt.putExtra("uid", uid)
            intt.putExtra("taraf", taraf)
            startActivity(intt)
        }
        var adpt = ArkadasRec(array, this, itemOnClick)
        ArkadaslarmenuArkadasrec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ArkadaslarmenuArkadasrec.adapter = adpt
        arkadassayisihesapla(array)
    }

    private fun arkadassayisihesapla(array: ArrayList<arkadasdata>) {
        var sayi = 0
        for (arkadas in array) {
            if (arkadas.durumistekarkadas!!) {
                sayi++
            }
        }
        if (array.isEmpty()) {
            arkadasbilg.visibility = View.VISIBLE
        } else {
            arkadasbilg.visibility = View.INVISIBLE
        }
        ArdaslarmenuArkadassayisi.setText(sayi.toString())
    }


    private fun Seviyebariduzenle() {
        dataref.child("Kullanicilar").child(uid!!).child("Tecrube")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.getValue() != null) {
                            val tec = p0.getValue() as String
                            tecrubeyegoreata(tec.toInt())
                        }
                    }

                })
    }

    private fun tecrubeyegoreata(Tecrube: Int) {


        if (25000 < Tecrube) {
            sevsayisi.setText("Sv.10")
            yuzdeyiayarla(Tecrube, 10)
        } else if (15000 < Tecrube) {
            sevsayisi.setText("Sv.9")
            yuzdeyiayarla(Tecrube, 9)
        } else if (8000 < Tecrube) {
            sevsayisi.setText("Sv.8")
            yuzdeyiayarla(Tecrube, 8)
        } else if (4000 < Tecrube) {
            sevsayisi.setText("Sv.7")
            yuzdeyiayarla(Tecrube, 7)
        } else if (2500 < Tecrube) {
            sevsayisi.setText("Sv.6")
            yuzdeyiayarla(Tecrube, 6)
        } else if (1250 < Tecrube) {
            sevsayisi.setText("Sv.5")
            yuzdeyiayarla(Tecrube, 5)
        } else if (500 < Tecrube) {
            sevsayisi.setText("Sv.4")
            yuzdeyiayarla(Tecrube, 4)
        } else if (250 < Tecrube) {
            sevsayisi.setText("Sv.3")
            yuzdeyiayarla(Tecrube, 3)
        } else if (100 < Tecrube) {
            sevsayisi.setText("Sv.2")
            yuzdeyiayarla(Tecrube, 2)
        } else if (100 > Tecrube) {
            sevsayisi.setText("Sv.1")
            yuzdeyiayarla(Tecrube, 1)
        }

    }

    private fun yuzdeyiayarla(gelentec: Int, Sev: Int) {
        var Oran = 0F
        val Tecrube = gelentec.toFloat()
        val nf = NumberFormat.getInstance()
        val sevref = dataref.child("Kullanicilar").child(uid!!).child("Seviye")
        nf.maximumFractionDigits = 2
        nf.minimumFractionDigits = 2
        when (Sev) {
            1 -> {
                Oran = (Tecrube - 0F) * 1F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("1")
            }
            2 -> {
                val kat = (Tecrube - 100F)
                Oran = kat / 1.5F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("2")

            }
            3 -> {
                val kat = (Tecrube - 250F)
                Oran = kat / 2.5F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("3")
            }
            4 -> {
                val kat = (Tecrube - 500F)
                Oran = kat / 7.5F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("4")
            }
            5 -> {
                val kat = (Tecrube - 1250)
                Oran = kat / 12.5F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("5")
            }
            6 -> {
                val kat = (Tecrube - 2500)
                Oran = kat / 15F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("6")
            }
            7 -> {
                val kat = (Tecrube - 4000)
                Oran = kat / 40F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("7")
            }
            8 -> {
                val kat = (Tecrube - 8000)
                Oran = kat / 70F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("8")
            }
            9 -> {
                val kat = (Tecrube - 15000)
                Oran = kat / 100F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("9")
            }
            10 -> {
                val kat = (Tecrube - 25000)
                Oran = kat / 10000F
                sevyuzdesi.setText("%" + nf.format(Oran))
                progressBar.progress = Oran
                sevref.setValue("10")
            }

        }
    }

    private fun Sohbetcek() {
        val Sohbetlist = ArrayList<sohbetdata>()
        dataref.child("Kullanicilar").child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Sohbetlist.clear()
                val gelen = p0.child("GelenKonusma").children
                for (sohbet in gelen) {
                    if (sohbet.key != "MesajSayisi" && sohbet.getValue() != null) {
                        Sohbetlist.add(sohbetdata(sohbet.key!!,"gelen") )                    }
                }
                val giden = p0.child("GidenKonusma").children
                for (sohbet in giden) {
                    if (sohbet.key != "MesajSayisi" && sohbet.getValue() != null) {
                        Sohbetlist.add(sohbetdata(sohbet.key!!,"giden") )                    }
                }
                recolusturgelen(Sohbetlist)
            }
        })
    }

    private fun Sohbetlistener() {
        val Sohbetlist = ArrayList<sohbetdata>()
        dataref.child("Kullanicilar").child(uid!!).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Sohbetlist.clear()
                val gelen = p0.child("GelenKonusma").children
                for (sohbet in gelen) {
                    if (sohbet.key != "MesajSayisi" && sohbet.getValue() != null) {
                        Sohbetlist.add(sohbetdata(sohbet.key!!,"gelen") )
                    }
                }
                val giden = p0.child("GidenKonusma").children
                for (sohbet in giden) {
                    if (sohbet.key != "MesajSayisi" && sohbet.getValue() != null) {
                        Sohbetlist.add(sohbetdata(sohbet.key!!,"giden"))
                    }
                }
                recolusturgelen(Sohbetlist)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Sohbetlist.clear()
                val gelen = p0.child("GelenKonusma").children
                for (sohbet in gelen) {
                    if (sohbet.key != "MesajSayisi" && sohbet.getValue() != null) {
                        Sohbetlist.add(sohbetdata(sohbet.key!!,"gelen") )                    }
                }
                val giden = p0.child("GidenKonusma").children
                for (sohbet in giden) {
                    if (sohbet.key != "MesajSayisi" && sohbet.getValue() != null) {
                        Sohbetlist.add(sohbetdata(sohbet.key!!,"giden") )                    }
                }
                recolusturgelen(Sohbetlist)
            }
        })
    }

    /*private fun recolusturgiden(gidenList: ArrayList<String>) {
        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
            val uidkonusmaci = gidenList[position]
            val intt = Intent(this, Mesaj::class.java)
            uidkonusucu = ""
            taraf = ""
            intt.putExtra("uid", uidkonusmaci)
            intt.putExtra("taraf", "GidenKonusma")
            startActivity(intt)
        }
        var adpt = SohbetRec(gidenList, itemOnClick, "GidenKonusma")
        gonderilenrec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        gonderilenrec.adapter = adpt}*/


    private fun recolusturgelen(gelenList: ArrayList<sohbetdata>) {
        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
            val uidkonusmaci = gelenList[position].uid
            uidkonusucu = ""
            taraf = ""
            var tarafyazisi=""
            if (gelenList[position].taraf=="gelen"){
                tarafyazisi="GelenKonusma"
            }else{
                tarafyazisi="GidenKonusma"
            }

            val intt = Intent(this, Mesaj::class.java)
            intt.putExtra("uid", uidkonusmaci)
            intt.putExtra("taraf", tarafyazisi)
            startActivity(intt)
        }
        var adpt = SohbetRec(gelenList, itemOnClick, "GelenKonusma",this)
        gelenlerrec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        gelenlerrec.adapter = adpt

    }

    private fun anamenubilgicek() {
        Picasso.get().load(uri).into(AnamenuProfilresim)
        uriatandi()
        datayagoreata(data)

    }

    private fun uriatandi() {
        Picasso.get().load(uri).into(profilresmiy)
    }

    private fun datayagoreata(data: kullanici) {
        profilisimy.setText(data.Isim + ",")
        profkonumy.setText(data.Sehir)
    }

    private fun anamenuicinayarlar() {
        birinibulbtnn()
        AnamenuProfilresim.setOnClickListener { navigation.selectedItemId=R.id.profil
        visiata(profillay)}
        bilgilendirme(7)
    }

    private fun bilgilendirme(onc: Int) {
        /*val mtn= resources.obtainTypedArray(R.array.Bilgilendirme)
        val rand=Random().nextInt(12)
        if (rand!=onc){
        Bilgilendirme.setText(mtn.getText(rand))}
        else{
            bilgilendirme(onc)
        }
        Bilgilendirme.setOnClickListener { bilgilendirme(rand) }*/
    }

    private fun birinibulbtnn() {
        birinibul.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    birinibul.elevation = 10F
                }
                MotionEvent.ACTION_UP -> {
                    birinibul.elevation = 15F
                    if (durumlar.Cins!! || durumlar.Yas!! || durumlar.Konum!!) {
                        if (konusulansayisi < (2 + sev)) {
                            bulbttnyapilacaklar()
                        } else {
                            Toast.makeText(this, getString(R.string.Konusma_Pre), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.Kontrol_Alan), Toast.LENGTH_SHORT).show()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    birinibul.elevation = 15F
                }
            }
            return@setOnTouchListener true
        }

        birinibul2.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    birinibul2.elevation = 10F
                }
                MotionEvent.ACTION_UP -> {
                    birinibul2.elevation = 15F
                    odakontol()

                }
                MotionEvent.ACTION_CANCEL -> {
                    birinibul2.elevation = 15F
                }
            }
            return@setOnTouchListener true
        }

    }

    private fun intentbaslatbul() {
        val intt = Intent(this, Profil::class.java)
        startActivity(intt)
    }

    override fun onStop() {
        super.onStop()
        val suankizamanturu=SimpleDateFormat("dd/MM  H:mm")
        val tarih=suankizamanturu.format(Date())
        dataref.child("Kullanicilar").child(uid!!).child("songorulme").setValue(tarih)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val suankizamanturu=SimpleDateFormat("dd/MM  H:mm")
        val tarih=suankizamanturu.format(Date())
        dataref.child("Kullanicilar").child(uid!!).child("songorulme").setValue(tarih)
    }


    override fun onResume() {
        super.onResume()
        dataref.child("Kullanicilar").child(uid!!).child("songorulme").setValue(getString(R.string.Cevrimici))
    }


    private fun Songorulmeolayi() {
        dataref.child("Kullanicilar").child(uid!!).child("songorulme").setValue(getString(R.string.Cevrimici))
    }

    private fun birinibulayarlari() {
        yasspintextayarla()
        spinayarla()
    }

    private fun spinayarla() {
        cinsspin2.adapter = ArrayAdapter.createFromResource(this, R.array.Cinsiyet, android.R.layout.simple_spinner_dropdown_item)
        konumspin2.adapter = ArrayAdapter.createFromResource(this, R.array.Sehir, android.R.layout.simple_spinner_dropdown_item)

    }

    private fun yasspintextayarla() {
        yasaraligi.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            if (predurum) {
                kucukyas.setText(minValue.toString())
                buyukyas.setText(maxValue.toString())
                durumlar.Yas = true
            } else {
                yasaraligi.isEnabled = false
                Toasty.info(this, "Yaş seçeneği için öncelikle Premium olmalısınız").show()
            }
        }
    }

    private fun bulbttnyapilacaklar() {
        durumatar()
        val rasg = Random()
//        bekleme.visibility=gorunur
        birinibul.isClickable = false
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val sayi = p0.childrenCount.toInt()
                        var index = 0
                        val rasgelesayi = rasg.nextInt(sayi - 1)
                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        for (data in p0.children) {
                            if (data.key != uid) {
                                if (rasgelesayi == index) {
                                    if (durumlar.Cins!!) {
                                        val cins = data.child("cinsiyet").getValue() as String
                                        if (cinsspin2.selectedItem != cins) {
                                            durumlar.Cins = false
                                        }
                                    }
                                    if (durumlar.Konum!!) {
                                        val konum = data.child("sehir").getValue() as String
                                        if (konumspin2.selectedItem != konum) {
                                            durumlar.Konum = false
                                        }
                                    }
                                    if (durumlar.Yas!!) {
                                        val yil = data.child("yil").getValue() as String
                                        val yas = 2018 - yil.toInt()
                                        val kucuk = kucukyas.text.toString().toInt()
                                        val buyuk = buyukyas.text.toString().toInt()
                                        for (secyas in kucuk..buyuk) {
                                            if (yas == secyas) {
                                                durumlar.Yas = true
                                                break
                                            } else {
                                                durumlar.Yas = false
                                            }
                                        }
                                    }
                                    if (durumlar.Cins!! || durumlar.Yas!! || durumlar.Konum!!) {
                                        if (durumlar.Cins!!){
                                        val kullanici = data.key
                                        if (konustuklarim.isEmpty()) {
                                            yapilacak(kullanici)
                                        }
                                        for (i in konustuklarim) {
                                            if (i == kullanici) {
                                                bulbttnyapilacaklar()
                                                break
                                            } else if (i == konustuklarim.last() && i != kullanici) {
                                                yapilacak(kullanici)
                                            }
                                        }}else{
                                            bulbttnyapilacaklar()
                                        }
                                    } else {
                                        // if (denemesayisiint==3000){ break }
                                        bulbttnyapilacaklar()
                                    }
                                    break
                                } else {
                                    index++
                                }
                            }
                        }
                    }
                })
    }

    private fun yapilacak(kullanici: String?) {
        FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(kullanici!!)
                .child("Engellenenler").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (kullanicilar in p0.children) {
                                val uid = kullanicilar.getValue() as String
                                if (uid == FirebaseAuth.getInstance().currentUser!!.uid) {
                                    bulbttnyapilacaklar()
                                } else if (kullanicilar.getValue() == p0.children.last()) {
                                    sohbetolustur(kullanici)
                                }
                            }

                        } else {
                            sohbetolustur(kullanici)

                        }

                    }

                })
    }

    private fun sohbetolustur(kullanici: String?) {
        var durumvaryok = false
        if (konustuklarim.isNotEmpty()) {
            for (ysk in konustuklarim) {
                if (ysk == kullanici) {
                    durumvaryok = false
                } else if (ysk == konustuklarim.last() && ysk != kullanici) {
                    durumvaryok = true
                }
            }
        } else {
            durumvaryok = true
        }

        if (durumvaryok) {
            val ref = FirebaseDatabase.getInstance().getReference()
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            ref.child("Kullanicilar").child(kullanici!!).child("GelenKonusma").child(uid).child("MesajSayisi").setValue("0")
            ref.child("Kullanicilar").child(uid).child("GidenKonusma").child(kullanici).child("MesajSayisi").setValue("0")
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid).child("GidenKonusma").child(kullanici).child("Durum").child("15").setValue(false)
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullanici).child("GelenKonusma").child(uid).child("Durum").child("15").setValue(false)
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid).child("GidenKonusma").child(kullanici).child("Durum").child("40").setValue(false)
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullanici).child("GelenKonusma").child(uid).child("Durum").child("40").setValue(false)
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid).child("GidenKonusma").child(kullanici).child("Durum").child("75").setValue(false)
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullanici).child("GelenKonusma").child(uid).child("Durum").child("75").setValue(false)
            val intt = Intent(this, Mesaj::class.java)
            intt.putExtra("uid", kullanici)
            intt.putExtra("taraf", "GidenKonusma")
            //bekleme.visibility=gorunmez
            startActivity(intt)
        } else {
            bulbttnyapilacaklar()
        }

    }
    private fun durumatar() {
        if (predurum) {
            durumlar.Yas = true
            durumlar.Konum = true
            durumlar.Cins = true
        } else {
            durumlar.Yas = false
            durumlar.Konum = false
            durumlar.Cins = true
        }
    }

    private fun odakontol() {
        if (cinsspin2.selectedItem == "Kadin") {
            FirebaseDatabase.getInstance().getReference().child("Odalar").child("Kadin")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                if (p0.childrenCount != 0L) {
                                    val rands = Random().nextInt(p0.childrenCount.toInt())
                                    var inx = 0
                                    for (oda in p0.children) {
                                        if (inx == rands) {
                                            val odaadi = oda.child("odaadi").getValue() as String
                                            val odakey = oda.key!!
                                            odayakatildi(odakey, "Kadin")
                                            videogoruntugit(odaadi, odakey, "Erkek")
                                        }
                                    }
                                } else {
                                    odaolustur()
                                }
                            } else {
                                odaolustur()
                            }
                        }

                    })

        } else {
            FirebaseDatabase.getInstance().getReference().child("Odalar").child("Erkek")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                if (p0.childrenCount != 0L) {
                                    val rands = Random().nextInt(p0.childrenCount.toInt())
                                    var inx = 0
                                    for (oda in p0.children) {
                                        if (inx == rands) {
                                            val odaadi = oda.child("odaadi").getValue() as String
                                            val odakey = oda.key!!
                                            odayakatildi(odakey, "Erkek")
                                            videogoruntugit(odaadi, odakey, "Erkek")
                                            break
                                        } else {
                                            inx++
                                        }
                                    }
                                } else {
                                    odaolustur()
                                }
                            } else {
                                odaolustur()
                            }
                        }

                    })
        }
    }

    private fun odayakatildi(odakey: String, Cins: String) {
        FirebaseDatabase.getInstance().getReference().child("Odalar")
                .child(Cins).child(odakey).child("Bekleme").setValue(true)
    }

    private fun odaolustur() {
        var Cinsiyetim = ""
        if (cinsspin2.selectedItem == "Kadin") {
            Cinsiyetim = "Erkek"
        } else {
            Cinsiyetim = "Kadin"
        }
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference().child("Odalar").child(Cinsiyetim).child(uid)
        val odaadi = uid.hashCode().toString()
        ref.child("odaadi").setValue(odaadi)
        ref.child("Bekleme").setValue(false)
        beklemeekraninagit(Cinsiyetim)
    }

    private fun beklemeekraninagit(Cins: String) {
        val intt = Intent(this, BeklemeEkrani::class.java)
        intt.putExtra("Cins", Cins)
        startActivity(intt)
    }

    private fun videogoruntugit(odaadi: String, uid: String, Cins: String) {
        val intt = Intent(this, VideoSohbet::class.java)
        intt.putExtra("odaadi", odaadi)
        intt.putExtra("uid", uid)
        intt.putExtra("cins", Cins)
        startActivity(intt)
    }
}