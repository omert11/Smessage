package com.mesage.s.s_msagge

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import io.agora.rtc.RtcEngine
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.android.synthetic.main.activity_video_sohbet.*
import java.util.*
import java.util.zip.CheckedInputStream

class VideoSohbet : AppCompatActivity() {
    lateinit var RtcMotor: RtcEngine
    var odaadi = ""
    var Cins = ""
    var uid1 = ""
    var uid2 = ""
    var sevkatki= (sev*20000).toLong()
    lateinit var a:CountDownTimer
    var begentus=true
    var Rtceventtuccu = object : IRtcEngineEventHandler() {
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) { // Tutorial Step 5
            runOnUiThread { Gelenvideoyukle(uid)
               seviyekatkisikarsilastir()}

        }

        override fun onUserMuteAudio(uid: Int, muted: Boolean) {
            runOnUiThread{onUserMuteAudio(uid,muted)}
        }
        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            leaveChannel()
            odakontol()
        }
    }

    private fun seviyekatkisikarsilastir() {
        FirebaseDatabase.getInstance().getReference().child("OdaOlusturKonusan")
                .child(odaadi)
                .addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            for (kullanici in p0.children){
                                val sure = kullanici.getValue() as Long
                                val odauid=kullanici.key!!
                                if (odauid!=uid1){
                                    uid2=odauid
                                }
                                if (sure>sevkatki){
                                    sevkatki=sure
                                    sayacayarlarivesayacbaslat()
                                    break
                                }else if (kullanici.getValue()==p0.children.last().getValue()){
                                    sayacayarlarivesayacbaslat()
                                    break
                                }
                            }
                        }
                    }

                })
    }

    private fun sayacayarlarivesayacbaslat() {
        val sure=60+(sevkatki.toInt()/1000)
        videosayac.setText(sure.toString())
        sayacbaslat()
    }

    private fun Gelenvideoyukle(uid: Int) {
        val ekran = GelenGoruntu
        val surfaceview = RtcEngine.CreateRendererView(baseContext)
        ekran.addView(surfaceview)
        RtcMotor.setupRemoteVideo(VideoCanvas(surfaceview, VideoCanvas.RENDER_MODE_HIDDEN, uid))
        surfaceview.setTag(uid)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_sohbet)
        odaadi = intent.extras.get("odaadi") as String
        uid1=intent.extras.get("uid") as String
        Cins=intent.extras.get("cins") as String
        buttonayarla()
        sayacsuresiyaz()
        val izinler = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            AgoromotorunuyukleveKanalagir()
        } else {
            ActivityCompat.requestPermissions(this, izinler, 155)
        }
    }

    private fun sayacsuresiyaz() {
        FirebaseDatabase.getInstance().getReference()
                .child("OdaOlusturKonusan")
                .child(odaadi)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(sevkatki)
    }

    private fun sayacbaslat() {
       a= object :CountDownTimer(60000+sevkatki,1000){
            override fun onFinish() {
                leaveChannel()
                RtcEngine.destroy()
                odakontol()
            }

            override fun onTick(millisUntilFinished: Long) {
                val Videosayac=videosayac.text.toString()
                val dusensure=Videosayac.toInt()-1
                videosayac.setText(dusensure.toString())
            }

        }.start()
    }

    private fun buttonayarla() {
        vdeo_kpt.setOnClickListener{
            leaveChannel()
            RtcEngine.destroy()
            odasilveanamenudon() }
        vdeo_cik.setOnClickListener {
            leaveChannel()
            RtcEngine.destroy()
            odakontol()
        }
        vdeo_begn.setOnTouchListener { _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    vdeo_begn.elevation=10F
                }
                MotionEvent.ACTION_UP -> {
                    vdeo_begn.elevation=15F
                    if (begentus){
                        begentus=false
                        Toasty.success(this,"Konuştuğun kişi beğeni ve tecrübe kazandı.Sende istemeyi unutma:)", Toast.LENGTH_LONG)
                    begenvetecata()}else{
                        Toasty.info(this,"Yanlızca bir kez beğenebilirsin")
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    vdeo_begn.elevation=15F
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun begenvetecata() {
        likeartislemi()
        tecata()
    }
    private fun likeartislemi() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid2)
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
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid2)
                .child("Like").setValue(likesayisi)
    }
    private fun tecata() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid2).child("Tecrube")
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
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid2).child("Tecrube").setValue(tec)
    }

    private fun AgoromotorunuyukleveKanalagir() {
        Agoramotorunuyukle()
        Videoayarlari()
        Kendivideoayarim()
        odayakatil()
    }

    private fun Videoayarlari() {
        RtcMotor!!.enableVideo()
//      mRtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_360P, false) // Earlier than 2.3.0
        RtcMotor!!.setVideoEncoderConfiguration(VideoEncoderConfiguration(VideoEncoderConfiguration.VD_1920x1080,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
    }

    fun Agoramotorunuyukle() {
        RtcMotor = RtcEngine.create(baseContext, getString(R.string.agora_app_id), Rtceventtuccu)

    }

    private fun odayakatil() {
        RtcMotor.joinChannel(null, odaadi, "Extra Optional Data", Random().nextInt(1000000))
    }

    fun Kendivideoayarim() {
        val ekran = Gidengoruntu
        val surfaceview = RtcEngine.CreateRendererView(baseContext)
        surfaceview.setZOrderMediaOverlay(true)
        ekran.addView(surfaceview)
        RtcMotor.setupLocalVideo(VideoCanvas(surfaceview, VideoCanvas.RENDER_MODE_HIDDEN, 0))

    }

    override fun onBackPressed() {
        leaveChannel()
        RtcEngine.destroy()
        odasilveanamenudon()
    }

    private fun odasilveanamenudon() {
        FirebaseDatabase.getInstance().getReference().child("Odalar").child(Cins).child(uid1)
                .removeValue()
        val intt=Intent(this,AnaMenu::class.java)
        startActivity(intt)
    }

    override fun onStop() {
        super.onStop()
        leaveChannel()
        RtcEngine.destroy()
        odasilveanamenudon()
    }

    override fun onPause() {
        super.onPause()
        leaveChannel()
        RtcEngine.destroy()
        odasilveanamenudon()
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
        odasilveanamenudon()
    }
    private fun leaveChannel() {
        RtcMotor.leaveChannel()
    }
    private fun odakontol() {
        if (Cins=="Kadin"){
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
    private fun videogoruntugit(odaadi:String,uid:String,Cins: String) {
        val intt=Intent(this,VideoSohbet::class.java)
        intt.putExtra("odaadi",odaadi)
        intt.putExtra("uid",uid)
        intt.putExtra("cins",Cins)
        startActivity(intt)
    }
    private fun odaolustur() {
        val Cinsiyetim=Cins
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
}
