package com.mesage.s.s_msagge

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profil.*

class Profil : AppCompatActivity() {
    val dataref=FirebaseDatabase.getInstance().getReference()
    var uid=""
    var izindurum=0
    val urilist=ArrayList<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid=intent.extras.get("uid") as String
        izindurum=intent.extras.get("izin") as Int
        setContentView(R.layout.activity_profil)
        Profilresimlerinicek()
        Profilbilgilerinicek()

    }

    private fun Profilbilgilerinicek() {
        dataref.child("Kullanicilar").child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val data = p0.getValue(kullanici::class.java)
                    if (data != null) {
                        var bio=""
                        if (p0.child("bio").exists()){ bio=p0.child("bio").getValue() as String}
                        val song=p0.child("songorulme").getValue() as String
                        datayagoreata(data,bio,song)
                    }
                }
            }

        })
    }

    private fun datayagoreata(data: kullanici,bio:String,song:String) {
        when(izindurum){
            0->{
                prof_goz_isim.setText("Bilinmeyen"+uid.hashCode().toString()+",")
                prof_goz_yas.setText((2019-data.Yil!!.toInt()).toString())
                prof_goz_bio.setText(bio)
                prof_goz_konum.setText(data.Sehir)
                prof_goz_song.setText(song)
            }
            1->{prof_goz_isim.setText(data.Isim+",")
                prof_goz_bio.setText(bio)
                prof_goz_yas.setText((2019-data.Yil!!.toInt()).toString())
                prof_goz_konum.setText(data.Sehir)
                prof_goz_song.setText(song)}
        }
    }

    private fun Profilresimlerinicek() {
        dataref.child("Kullanicilar").child(uid).child("Resimsay").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val resimsay = p0.getValue() as String
                    rsimdongusu(resimsay.toInt())
                }else{
                    rsimdongusu(0)
                }
            }

        })
    }

    private fun rsimdongusu(toInt: Int) {
        urilist.clear()
        if (toInt==0){
            FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uid!!).child("Profil.jpg").downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    val eklenecekuri = it.result!!
                    urilist.add(eklenecekuri)
                    Resimleriyukle()
                }}
        }else{
        for (dongu in 1..toInt) {
            FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uid).child("Resimlerim")
                    .child(dongu.toString() + ".jpg").downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    val eklenecekuri = it.result!!
                    urilist.add(eklenecekuri)
                    Resimleriyukle()

                }
            }
        }}
    }

    private fun Resimleriyukle() {
        val adpt=Sayfaadapter(urilist)
        sayfacev.adapter=adpt
        indicator.setViewPager(sayfacev)
    }

    /*var denemesayisiint=0
    var konusulansayisi=0

    val konustuklarim=ArrayList<String>()
    val gorunur = View.VISIBLE
    val gorunmez = View.INVISIBLE
    val rasg = Random()
    var durumlar = aramaclass(true, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        gonderilensohbetsayisicek()
        arrayata()
        konustuklarimcek()
        listenerler()
        buttonayarlari()
        bulbttn.setOnClickListener {
            if (durumlar.Cins!! || durumlar.Yas!! || durumlar.Konum!!) {
                if (konusulansayisi<(2+sev)){
                bulbttnyapilacaklar()}else{
                    Toasty.info(this,getString(R.string.Konusma_Pre),Toast.LENGTH_SHORT).show()
                }
            } else {
                Toasty.error(this,getString(R.string.Kontrol_Alan),Toast.LENGTH_SHORT).show()
            }

        }
        videobtn.setOnClickListener { odakontol()  }
    }

    private fun buttonayarlari() {
        videobtn.buttonColor=resources.getColor(R.color.colorPrimary)
        videobtn.shadowColor=resources.getColor(R.color.golge)
        videobtn.setFButtonPadding(3,3,3,3)
        bulbttn.buttonColor=resources.getColor(R.color.colorPrimaryDark)
        bulbttn.shadowColor=resources.getColor(R.color.golge)
        bulbttn.setFButtonPadding(5,3,5,3)
    }

    private fun gonderilensohbetsayisicek() {
        val uid= FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid)
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()&&p0.child("Seviye").getValue()!=null){
                            konusulansayisi= p0.child("GidenKonusma").childrenCount.toInt()
                            val sevy=p0.child("Seviye").getValue() as String
                            sev=sevy.toInt()
                        }
                    }

                })
    }

    private fun konustuklarimcek() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("GelenKonusma").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            for (kisileruid in p0.children){
                                val uid=kisileruid.key
                                konustuklarim.add(uid!!)
                            }
                        }
                    }

                })
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("GidenKonusma").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            for (kisileruid in p0.children){
                                val uid=kisileruid.key
                                konustuklarim.add(uid!!)
                            }
                        }
                    }

                })
        FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("Engellenenler").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            for (kullanici in p0.children){
                                val uid=kullanici.getValue() as String
                                konustuklarim.add(uid)
                            }
                        }
                    }

                })
    }

    private fun odakontol() {
        if (cinsspin.selectedItem=="Kadin"){
            FirebaseDatabase.getInstance().getReference().child("Odalar").child("Kadin")
                    .addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()){
                                if (p0.childrenCount!=0L){
                                    val rands=Random().nextInt(p0.childrenCount.toInt())
                                    var inx=0
                                    for (oda in p0.children){
                                        if (inx==rands){
                                            val odaadi=oda.child("odaadi").getValue() as String
                                            val odakey=oda.key!!
                                            odayakatildi(odakey,"Kadin")
                                            videogoruntugit(odaadi,odakey,"Erkek")
                                        }
                                    }
                                }else{
                                    odaolustur()
                                }
                            }else{
                                odaolustur()
                            }
                        }

                    })

        }else{
            FirebaseDatabase.getInstance().getReference().child("Odalar").child("Erkek")
                    .addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()){
                                if (p0.childrenCount!=0L){
                                    val rands=Random().nextInt(p0.childrenCount.toInt())
                                    var inx=0
                                    for (oda in p0.children){
                                        if (inx==rands){
                                            val odaadi=oda.child("odaadi").getValue() as String
                                            val odakey=oda.key!!
                                            odayakatildi(odakey,"Erkek")
                                            videogoruntugit(odaadi,odakey,"Erkek")
                                            break
                                        }else{
                                            inx++
                                        }
                                    }
                                }else{
                                    odaolustur()
                                }
                            }else{
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
        var Cinsiyetim=""
        if (cinsspin.selectedItem=="Kadin"){
            Cinsiyetim="Erkek"
        }else{
            Cinsiyetim="Kadin"
        }
        val uid= FirebaseAuth.getInstance().currentUser!!.uid
        val ref=FirebaseDatabase.getInstance().getReference().child("Odalar").child(Cinsiyetim).child(uid)
        val odaadi=uid.hashCode().toString()
        ref.child("odaadi").setValue(odaadi)
        ref.child("Bekleme").setValue(false)
        beklemeekraninagit(Cinsiyetim)
    }

    private fun beklemeekraninagit(Cins: String) {
        val intt=Intent(this,BeklemeEkrani::class.java)
        intt.putExtra("Cins",Cins)
        startActivity(intt)
    }

    private fun videogoruntugit(odaadi:String,uid:String,Cins: String) {
        val intt=Intent(this,VideoSohbet::class.java)
        intt.putExtra("odaadi",odaadi)
        intt.putExtra("uid",uid)
        intt.putExtra("cins",Cins)
        startActivity(intt)
    }
    private fun bulbttnyapilacaklar() {
        Denemesayisi.setText(denemesayisiint.toString())
        denemesayisiint++
        durumatar()
        bekleme.visibility=gorunur
        bulbttn.isEnabled = false
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val sayi = p0.childrenCount.toInt()
                        var index = 0
                        val rasgelesayi = rasg.nextInt(sayi-1)
                        val uid=FirebaseAuth.getInstance().currentUser!!.uid
                        for (data in p0.children) {
                            if (data.key!=uid){
                            if (rasgelesayi == index) {
                                if (durumlar.Cins!!) {
                                    val cins = data.child("cinsiyet").getValue() as String
                                    if (cinsspin.selectedItem != cins) {
                                        durumlar.Cins = false
                                    }
                                }
                                if (durumlar.Konum!!) {
                                    val konum = data.child("sehir").getValue() as String
                                    if (konumspin.selectedItem != konum) {
                                        durumlar.Konum = false
                                    }
                                }
                                if (durumlar.Yas!!) {
                                    val yil = data.child("yil").getValue() as String
                                    val yas = 2018 - yil.toInt()
                                    if (yasspin.selectedItem != yas.toString()) {
                                        durumlar.Yas = false
                                    }
                                }
                                if (durumlar.Cins!! || durumlar.Yas!! || durumlar.Konum!!) {
                                    val kullanici =data.key
                                    if (konustuklarim.isEmpty()){
                                        yapilacak(kullanici)
                                    }
                                    for (i in konustuklarim){
                                        if (i==kullanici){
                                            bulbttnyapilacaklar()
                                            break
                                        }else if (i==konustuklarim.last()&&i!=kullanici){
                                            yapilacak(kullanici)
                                        }
                                    }
                                } else {
                                    if (denemesayisiint==3000){ break }
                                    bulbttnyapilacaklar()
                                }
                                break
                            } else {
                                index++
                            }
                        }}
                    }
                })
    }

    private fun yapilacak(kullanici: String?) {
        FirebaseDatabase.getInstance().getReference().child("KullaniciAyarlari").child(kullanici!!)
                .child("Engellenenler").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if(p0.exists()){
                            for (kullanicilar in p0.children ){
                               val uid= kullanicilar.getValue()as String
                                if (uid==FirebaseAuth.getInstance().currentUser!!.uid){
                                    bulbttnyapilacaklar()
                                }else if (kullanicilar.getValue() == p0.children.last()){
                                    sohbetolustur(kullanici)
                                }
                            }

                        }else{
                            sohbetolustur(kullanici)

                        }

                    }

                })
    }

    private fun sohbetolustur(kullanici: String?) {
        var durumvaryok=false
        for (ysk in konustuklarim){
            if(ysk==kullanici){
                durumvaryok=false
            }else if(ysk==konustuklarim.last()&&ysk!=kullanici){
                durumvaryok=true
            }
        }

        if (durumvaryok){
            val ref=FirebaseDatabase.getInstance().getReference()
            val uid=FirebaseAuth.getInstance().currentUser!!.uid
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
            bekleme.visibility=gorunmez
            startActivity(intt)
        }else{
            bulbttnyapilacaklar()
        }

    }

    private fun durumatar() {
        if (yasswitch.isChecked) {
            durumlar.Yas = true
        }
        if (cinsswitch.isChecked) {
            durumlar.Cins = true
        }
        if (konumswitch.isChecked) {
            durumlar.Konum = true
        }
    }

    private fun listenerler() {
        cinsswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cinsspin.visibility = gorunur
                durumlar.Cins = true
            } else {
                cinsspin.visibility = gorunmez
                durumlar.Cins = false
            }
        }
        konumswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                konumspin.visibility = gorunur
                if (predurum) {
                    durumlar.Konum = true
                } else {
                    Toast.makeText(this, getString(R.string.Kontrol_Pre), Toast.LENGTH_SHORT).show()
                    konumswitch.isChecked = false
                    konumspin.visibility = gorunmez
                }

            } else {
                durumlar.Konum = false
                konumspin.visibility = gorunmez
            }
        }
        yasswitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                yasspin.visibility = gorunur
                if (predurum) {
                    durumlar.Yas = true
                } else {
                    Toast.makeText(this, getString(R.string.Kontrol_Pre), Toast.LENGTH_SHORT).show()
                    yasswitch.isChecked = false
                    yasspin.visibility = gorunmez

                }
            } else {
                durumlar.Yas = false
                yasspin.visibility = gorunmez
            }
        }
    }

    private fun arrayata() {
        konumspin.adapter = ArrayAdapter.createFromResource(this, R.array.Sehir, android.R.layout.simple_spinner_dropdown_item)
        yasspin.adapter = ArrayAdapter.createFromResource(this, R.array.Yas, android.R.layout.simple_spinner_dropdown_item)
        cinsspin.adapter = ArrayAdapter.createFromResource(this, R.array.Cinsiyet, android.R.layout.simple_spinner_dropdown_item)
    }*/
}
