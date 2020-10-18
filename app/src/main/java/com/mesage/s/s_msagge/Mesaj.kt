package com.mesage.s.s_msagge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mesage.s.s_msagge.services.fcminterface
import com.mesage.s.s_msagge.services.fcmmodel
import com.squareup.picasso.Picasso
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_mesaj.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var gidecekmesaj = ""
var uidkonusucu = ""
var taraf = ""
var tarafters = ""

class Mesaj : AppCompatActivity(), OnMenuItemClickListener, OnMenuItemLongClickListener {
   lateinit var fragmentManager:FragmentManager
    lateinit var fragment:ContextMenuDialogFragment
    var arkadasdurumu=false
    var izindurum=0
    override fun onMenuItemClick(p0: View?, p1: Int) {
        when(p1){
            1->{likeartislemi()}
            2->{likeeksislemi()}
            3->{arkadasekleme()}
            4->{engelle()}
            5->{goruntulukonusmabaslat()}
        }
    }


    private fun goruntulukonusmabaslat() {

    }

    private fun likeeksislemi() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkonusucu)
                .child("Like").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val oankilike=p0.getValue() as String
                            val arttir=oankilike.toInt()-1
                            likeata(arttir.toString())
                        }else{
                            likeata("-1")
                        }
                    }
                })

    }

    private fun likeartislemi() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkonusucu)
                .child("Like").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val oankilike=p0.getValue() as String
                            val arttir=oankilike.toInt()+1
                            likeata(arttir.toString())
                        }else{
                          likeata("1")
                        }
                    }

                })
    }

    private fun likeata(likesayisi: String) {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkonusucu)
                .child("Like").setValue(likesayisi)
    }

    private fun engelle() {

       FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(uidkullanici)
                .child("Engellenenler").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val sayi =p0.childrenCount.toString()
                            engelkadet(sayi)
                        }else{
                            engelkadet("0")
                        }
                    }



                })
    }
    private fun engelkadet(sayi: String) {
        FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(uidkullanici)
                .child("Engellenenler").child(sayi).setValue(uidkonusucu)
        sohbetsil()
    }

    private fun sohbetsil() {
      val ref = FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
        ref.child(uidkullanici).child(taraf).child(uidkonusucu).removeValue()
        ref.child(uidkonusucu).child(tarafters).child(uidkullanici).removeValue()
        val kod =Kodolustur()
        FirebaseDatabase.getInstance().getReference().child("Sohbetler").child(kod).removeValue()
        onBackPressed()
    }

    private fun arkadasekleme() {
        if (arkadasdurumu){
            arkadasekle()
        }else{
            Toasty.info(this,"Arkadaşlık mesaj sınırını geçmediniz(75.msj)",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMenuItemLongClick(p0: View?, p1: Int) {
    }

    val dataref=FirebaseDatabase.getInstance().getReference()
    val uid=FirebaseAuth.getInstance().currentUser!!.uid
    var arraymesaj = ArrayList<mesajdata>()
    val uidkullanici = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arraymesaj.clear()
        uidkonusucu = intent.extras.get("uid") as String
        taraf = intent.extras.get("taraf") as String
        if (taraf == "GelenKonusma") {
            tarafters = "GidenKonusma"
        } else {
            tarafters = "GelenKonusma"
        }
        setContentView(R.layout.activity_mesaj)
        fragmentManager=supportFragmentManager
        KonusmaMesajSend.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val params = KonusmaMesajSend.layoutParams
                    params.height = 100
                    params.width = 100
                    KonusmaMesajSend.layoutParams = params
                }
                MotionEvent.ACTION_UP -> {
                    val params = KonusmaMesajSend.layoutParams
                    params.height = -2
                    params.width = -2
                    KonusmaMesajSend.layoutParams = params
                    kontroldurumvemesajsayisi()

                }
                MotionEvent.ACTION_CANCEL -> {
                    val params = KonusmaMesajSend.layoutParams
                    params.height = -2
                    params.width = -2
                    KonusmaMesajSend.layoutParams = params
                }
            }
            return@setOnTouchListener true
        }
        toolbaryukle()
        menufragmentyukle()
        mesajcek()
        mesajsayisikonroller()
        durumagorebilgilericek()
        Songorulmecek()
        okundubilgisi()
        Songorulmeolayi()
        object :CountDownTimer(3000,500){
            override fun onFinish() {
                okunduyap()
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inf=menuInflater
        inf.inflate(R.menu.menu_mesaj,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mesaj_menu-> {
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null){
                    fragment.show(fragmentManager,ContextMenuDialogFragment.TAG)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun menufragmentyukle() {
       val menuayarlari = MenuParams()
        menuayarlari.actionBarSize=resources.getDimension(R.dimen.tool_bar_height).toInt()
        menuayarlari.menuObjects=menuobjelerinial()
        menuayarlari.isClosableOutside=false
        fragment=ContextMenuDialogFragment.newInstance(menuayarlari)
        fragment.setItemClickListener(this)
        fragment.setItemLongClickListener(this)
    }

    private fun menuobjelerinial(): MutableList<MenuObject> {
        val Menuobjeleri=ArrayList<MenuObject>()
        val kapat=MenuObject("Kapat")
        kapat.resource=R.drawable.ic_clear_blue_500_24dp
        val yolla=MenuObject("Like")
        yolla.resource=R.drawable.ic_thumb_up_blue_500_24dp
        val favoriyeal=MenuObject("Dislike")
        favoriyeal.resource=R.drawable.ic_thumb_down_blue_500_24dp
        val arkadasekle=MenuObject("Arkadaş Ekle")
        arkadasekle.resource=R.drawable.ic_person_add_blue_500_24dp
        val engel=MenuObject("Engelle")
        engel.resource=R.drawable.ic_block_blue_500_24dp
        Menuobjeleri.add(kapat)
        Menuobjeleri.add(yolla)
        Menuobjeleri.add(favoriyeal)
        Menuobjeleri.add(arkadasekle)
        Menuobjeleri.add(engel)
        return Menuobjeleri
    }

    private fun toolbaryukle() {
        setSupportActionBar(toolbar)
        if(supportActionBar!=null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_blue_500_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }



    private fun arkadasekle() {
        dataref.child("Kullanicilar").child(uidkonusucu).child("Istekler").child(uid).setValue(false)
    }

    private fun Songorulmecek() {
        dataref.child("Kullanicilar").child(uidkonusucu).child("songorulme")
                .addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.getValue()!=null){
                            val songorulme=p0.getValue() as String
                            KonusmaKisisong.text=songorulme

                        }
                    }
                })
    }

    private fun okundubilgisi() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("MesajBilgisi")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.child("okunmamis").getValue() != null) {
                            removeOkunmamis()
                        }else{

                        }

                    }

                })
    }

    private fun removeOkunmamis() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici)
                .child(taraf).child(uidkonusucu).child("MesajBilgisi")
                .child("okunmamis").removeValue()
    }

    private fun okunduyap() {
        var gidecekarray=ArrayList<mesajdata>()
        for (mesajtek in arraymesaj){
            if (mesajtek.taraf!= uidkullanici){
            mesajtek.Okundubilgisi=true}
            gidecekarray.add(mesajtek)
        }
        FirebaseDatabase.getInstance().getReference().child("Sohbetler").child(Kodolustur()).setValue(gidecekarray)
    }

    private fun durumagorebilgilericek() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                .child(uidkullanici).child(taraf).child(uidkonusucu)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.child("MesajSayisi").getValue() != null&&p0.exists()&&p0.child("Durum").child("15").getValue() != null) {
                            val resimdurum = p0.child("Durum").child("15").getValue() as Boolean
                            val isimdurum = p0.child("Durum").child("40").getValue() as Boolean
                            val arkadasdurum = p0.child("Durum").child("75").getValue() as Boolean
                            if (resimdurum&&isimdurum==false&&arkadasdurum==false) {
                                KonusmaKisiisim.setText("Bilinmeyen" + Kodolustur())
                                resimata()
                                izindurum=0
                            } else if(resimdurum==false&&isimdurum==false&&arkadasdurum==false) {
                                KonusmaKisiisim.setText("Bilinmeyen" + Kodolustur())
                                izindurum=0
                            }
                            if (isimdurum&&resimdurum&&arkadasdurum==false) {
                                isimata()
                                resimata()
                                izindurum=1
                            }
                            if (arkadasdurum&&resimdurum&&isimdurum) {
                                arkadaslogosucagir()
                                isimata()
                                resimata()
                                izindurum=1
                            }

                        }
                    }
                })
    }

    private fun arkadaslogosucagir() {
        arkadasdurumu=true
    }

    private fun isimata() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                .child(uidkonusucu)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val isim = p0.child("isim").getValue() as String
                        KonusmaKisiisim.text = isim
                    }

                })

    }

    private fun resimata() {
        FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uidkonusucu)
                .child("Profil.jpg").downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                val uri = it.result!!
                Picasso.get().load(uri).into(KonusmaKisiresim)


            }
        }
        KonusmaKisiresim.setOnClickListener {
            val intt=Intent(this,Profil::class.java)
            intt.putExtra("uid", uidkonusucu)
            intt.putExtra("izin", izindurum)
            startActivity(intt)
        }
    }

    private fun kontroldurumvemesajsayisi() {
        if (arraymesaj.size == 15 || arraymesaj.size == 40 || arraymesaj.size == 75) {
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            when (arraymesaj.size) {
                                15 -> {
                                    val durum = p0.child("15").getValue() as Boolean
                                    if (durum) {
                                        if (KonusmaMesaj.text.isNotEmpty()) {
                                            if (KonusmaMesaj.text.length < 300) {
                                                konusmayiyolla(false)

                                            } else {
                                                Toasty.error(this@Mesaj, getString(R.string.Kontrol_Karakter), Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toasty.info(this@Mesaj, getString(R.string.Kontrol_onay_p), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                40 -> {
                                    val durum = p0.child("40").getValue() as Boolean
                                    if (durum) {
                                        if (KonusmaMesaj.text.isNotEmpty()) {
                                            if (KonusmaMesaj.text.length < 300) {
                                                konusmayiyolla(false)

                                            } else {
                                                Toasty.info(this@Mesaj, getString(R.string.Kontrol_Karakter), Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toasty.info(this@Mesaj, getString(R.string.Kontrol_onay_i), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                75 -> {
                                    val durum = p0.child("75").getValue() as Boolean
                                    if (durum) {
                                        if (KonusmaMesaj.text.isNotEmpty()) {
                                            if (KonusmaMesaj.text.length < 300) {
                                                konusmayiyolla(false)

                                            } else {
                                                Toasty.info(this@Mesaj, getString(R.string.Kontrol_Karakter), Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toasty.info(this@Mesaj, getString(R.string.Kontrol_onay_a), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    })
        } else {
            if (KonusmaMesaj.text.isNotEmpty()) {
                if (KonusmaMesaj.text.length < 300) {
                    konusmayiyolla(false)

                } else {
                    Toasty.info(this@Mesaj, getString(R.string.Kontrol_Karakter), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mesajsayisikonroller() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("MesajSayisi")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                        val sayi = (p0.getValue() as String).toInt()
                        if (sayi == 15) {
                            gidecekmesaj = getString(R.string.Soru_Profılres)
                            val fragment = UyariFragment()
                            fragment.show(supportFragmentManager, "15")
                        } else if (sayi == 40) {
                            gidecekmesaj = getString(R.string.Soru_Profılism)
                            val fragment = UyariFragment()
                            fragment.show(supportFragmentManager, "15")
                        } else if (sayi == 75) {
                            gidecekmesaj = getString(R.string.Soru_Ark)
                            val fragment = UyariFragment()
                            fragment.show(supportFragmentManager, "15")
                        }}
                    }
                })
    }


    private fun mesajcek() {
        FirebaseDatabase.getInstance().getReference().child("Sohbetler").child(Kodolustur())
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                        arraymesaj.clear()
                        for (mesaj in p0.children) {
                            if (mesaj.key != null) {
                                val tekmesaj = mesaj.getValue(mesajdata::class.java)
                                arraymesaj.add(tekmesaj!!)
                            }

                        }
                        recviewolustur()}
                    }

                })
    }

    private fun recviewolustur() {
        var adpt = MesajRec(arraymesaj, uidkullanici, uidkonusucu)
        KonusmaRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        KonusmaRec.adapter = adpt
        KonusmaRec.scrollToPosition(arraymesaj.size - 1)
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("MesajSayisi").setValue(arraymesaj.size.toString())
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkonusucu).child(tarafters).child(uidkullanici).child("MesajSayisi").setValue(arraymesaj.size.toString())

    }


    private fun konusmayiyolla(Okunmadurumu:Boolean) {
        dataref.child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu)
                .child("Durum").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val durum15 =p0.child("15").getValue() as Boolean
                            val durum40 =p0.child("40").getValue() as Boolean
                            val durum75 =p0.child("75").getValue() as Boolean
                            Karsilastir(Okunmadurumu,durum15,durum40,durum75)
                        }
                    }

                })
    }

    private fun Karsilastir(okunmadurumu: Boolean, durum15: Boolean, durum40: Boolean, durum75: Boolean) {
        dataref.child("Kullanicilar").child(uidkonusucu).child(tarafters).child(uidkullanici)
                .child("Durum").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val durumk15 =p0.child("15").getValue() as Boolean
                            val durumk40 =p0.child("40").getValue() as Boolean
                            val durumk75 =p0.child("75").getValue() as Boolean
                            if (durum15==durumk15&&durum40==durumk40&&durum75==durumk75){
                                Kontrolsonrasiyolla(okunmadurumu)
                            }else{
                                Uyarigoster()
                            }
                        }
                    }

                })
    }

    private fun Uyarigoster() {
        val fragg=EngelFragment()
        fragg.show(supportFragmentManager,"Engelle")
    }

    private fun Kontrolsonrasiyolla(okunmadurumu: Boolean) {
        if (kontrol()) {
            val mesaj = KonusmaMesaj.text.toString()
            val kayitmesaj = mesajdata(mesaj, uidkullanici,okunmadurumu)
            arraymesaj.add(kayitmesaj)
            mesajikaydet()
            KonusmaMesaj.setText("")
        } else {
            KonusmaMesaj.setText("")
        }
    }

    private fun kontrol(): Boolean {
        val inx = arraymesaj.size
        if (inx >= 2) {
            if (arraymesaj[inx - 1].taraf == uidkullanici
                    && arraymesaj[inx - 2].taraf == uidkullanici
            ) {
                Toasty.error(this, "En Fazla 2 Mesaj Ust Uste Atilabilir!", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun mesajikaydet() {
        val Kod = Kodolustur()
        FirebaseDatabase.getInstance().getReference().child("Sohbetler").child(Kod).setValue(arraymesaj)
        tecata()
        okunmamiskontrol()
        okunduyap()
        tokenalvebildirimolla()
    }

    private fun tokenalvebildirimolla() {
        dataref.child("Kullanicilar").child(uidkonusucu)
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            val token=p0.child("Token").getValue() as String
                            val isim = p0.child("isim").getValue() as String
                            val mesaj = arraymesaj.last().mesaj!!
                            val isimk="Bilinmeyen"+isim.hashCode().toString()
                            if (izindurum==0){
                                bildirim(mesaj,isimk,token)
                            }else{
                            bildirim(mesaj,isim,token)}
                        }
                    }

                })
    }

    private fun okunmamiskontrol() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkonusucu).child(tarafters).child(uidkullanici).child("MesajBilgisi")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.child("okunmamis").getValue() != null) {
                            var okunmamissayisi = p0.child("okunmamis").getValue() as String
                            if (okunmamissayisi.toInt()==0){okunmamissayisi="1"}
                            val kaydedilecek = okunmamissayisi.toInt() + 1
                            kaydet(kaydedilecek.toString())
                        } else {
                            kaydet("1")
                        }

                    }

                })
    }

    private fun kaydet(Kaydet: String) {
        val zaman=Calendar.getInstance().time
        val dakka=zaman.minutes
        val saat=zaman.hours
        val gun = zaman.day
        val ay=zaman.month
        val tarih=ay.toString()+"/"+gun.toString()+"   "+saat.toString()+":"+dakka.toString()
        val data =mesajdata(arraymesaj.last().mesaj!!,tarih,Kaydet)
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkonusucu).child(tarafters).child(uidkullanici).child("MesajBilgisi")
                .setValue(data)
    }

    private fun tecata() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child("Tecrube")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                        val tec = p0.getValue() as String
                        val arttir = tec.toInt() + 5
                        tecyolla(arttir.toString())}
                        else{
                            tecyolla("0")
                        }
                    }
                })
    }

    private fun tecyolla(tec: String) {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child("Tecrube").setValue(tec)
    }

    private fun Kodolustur(): String {
        val code1 = uidkullanici.hashCode()
        val code2 = uidkonusucu.hashCode()
        val code = code1 + code2
        return code.toString()
    }

    override fun onBackPressed() {
        if (fragment!=null&&fragment.isAdded){
            fragment.dismiss()
        }else{
        val intt= Intent(this,AnaMenu::class.java)
        startActivity(intt)}
    }

    override fun onPause() {
        super.onPause()
        val suankizamanturu= SimpleDateFormat("dd/MM  H:mm")
        val tarih=suankizamanturu.format(Date())
        dataref.child("Kullanicilar").child(uid!!).child("songorulme").setValue(tarih)
    }

    override fun onResume() {
        super.onResume()
        dataref.child("Kullanicilar").child(uid).child("songorulme").setValue("Cevrim Ici")
    }
    private fun Songorulmeolayi() {
        object :CountDownTimer(1500,100){
            override fun onFinish() {
                okunduyap()
            }

            override fun onTick(millisUntilFinished: Long) {
                dataref.child("Kullanicilar").child(uid).child("songorulme").setValue("Cevrim Ici")
            }

        }.start()
    }
    fun bildirim(mesaj:String,baslik:String,token:String){
        var headerd = HashMap<String, String>()
        headerd.put("Content-Type", "application/json")
        headerd.put("Authorization", "key=" + serverkey)
        var data = fcmmodel.Data(mesaj, baslik, uidkullanici)
        var bildirim = fcmmodel(data, token)
        var etrofit = Retrofit.Builder().baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build()
        var int = etrofit.create(fcminterface::class.java)
        var istek = int.bildirimgonder(headerd, bildirim)
        istek.enqueue(object : Callback<Response<fcmmodel>> {
            override fun onFailure(call: Call<Response<fcmmodel>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Response<fcmmodel>>?, response: Response<Response<fcmmodel>>?) {

            }
        })
    }
}
