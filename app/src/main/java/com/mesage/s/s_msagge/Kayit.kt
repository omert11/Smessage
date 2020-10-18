package com.mesage.s.s_msagge

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mesage.s.s_msagge.services.YuklemeEkrani
import com.soundcloud.android.crop.Crop
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_kayit.*
import java.io.ByteArrayOutputStream
import java.io.File

class Kayit : AppCompatActivity() {
    lateinit var resimuri: ByteArray
    var resim = false
    val dataref = FirebaseDatabase.getInstance().getReference()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val gorunur = View.VISIBLE
    val gorunmez = View.INVISIBLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bekletme_gecis)
        izinkontrol2()
    }

    private fun kaydetolaylari() {
        Progress.visibility = gorunur
        if (resim && isimet.text.isNotEmpty()) {
            kaydetbtn.isEnabled = false
            FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uid!!).child("Profil.jpg").putBytes(resimuri)
                    .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {
                        override fun onComplete(p0: Task<UploadTask.TaskSnapshot>) {
                            verilerikaydet()
                            tokenata()
                        }
                    })

        } else if (isimet.text.isEmpty()) {
            Toasty.error(this, getString(R.string.Kontrol_isim), Toast.LENGTH_SHORT).show()
            Progress.visibility = gorunmez

        } else {
            Toasty.error(this, getString(R.string.Kontol_resim), Toast.LENGTH_SHORT).show()
            Progress.visibility = gorunmez

        }

    }

    private fun verilerikaydet() {
        val isim = isimet.text.toString()
        val cinsiyet = Cinsiyetspin.selectedItem as String
        val sehir = sehirspin.selectedItem as String
        val gun = gunspin.selectedItem as String
        val ay = ayspin.selectedItem as String
        val yil = yilspin.selectedItem as String
        val data = kullanici(isim, sehir, gun, ay, yil, cinsiyet, "0")
        dataref.child("Kullanicilar").child(uid!!).setValue(data)
        dataref.child("Kullanicilar").child(uid).child("Tecrube").setValue("0")
        dataref.child("Kullanicilar").child(uid).child("Toplammesajsayisi").setValue("0")
        dataref.child("Kullanicilar").child(uid).child("Alinanmesajsayisi").setValue("0")
        dataref.child("Kullanicilar").child(uid).child("Arkadassayisi").setValue("0")
        dataref.child("Kullanicilar").child(uid).child("Predurum").setValue(false)
        dataref.child("Kullanicilar").child(uid).child("Kalangun").setValue("0")
        dataref.child("Kullanicilar").child(uid).child("Like").setValue("0")
        dataref.child("Kullanicilar").child(uid).child("Resimsay").setValue("0")
        yuklemeekraniyolla()
    }

    private fun yuklemeekraniyolla() {
        val intt = Intent(this@Kayit, YuklemeEkrani::class.java)
        startActivity(intt)
        finish()
    }

    private fun izinkontrol() {
        val izinler = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA, android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
        if (ContextCompat.checkSelfPermission(this, izinler[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, izinler[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, izinler[2]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, izinler[3]) == PackageManager.PERMISSION_GRANTED
        ) {
            resimcek()
        } else {
            ActivityCompat.requestPermissions(this, izinler, 150)
        }
    }

    private fun izinkontrol2() {
        val izinler = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(this, izinler[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, izinler[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, izinler[2]) == PackageManager.PERMISSION_GRANTED
        ) {
            kontrol()
        } else {
            ActivityCompat.requestPermissions(this, izinler, 185)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 150) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                resimcek()
            } else {
                Toasty.error(this, getString(R.string.Kontrol_izin), Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == 185) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                kontrol()
            } else {
                Toasty.error(this, getString(R.string.Kontrol_izin), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resimcek() {
     Crop.pickImage(this)
    }

    private fun spinnerarraylariata() {
        sehirspin.adapter = ArrayAdapter.createFromResource(this, R.array.Sehir, android.R.layout.simple_spinner_dropdown_item)
        Cinsiyetspin.adapter = ArrayAdapter.createFromResource(this, R.array.Cinsiyet, android.R.layout.simple_spinner_dropdown_item)
        yilspin.adapter = ArrayAdapter.createFromResource(this, R.array.Yillar, android.R.layout.simple_spinner_dropdown_item)
        ayspin.adapter = ArrayAdapter.createFromResource(this, R.array.Aylar, android.R.layout.simple_spinner_dropdown_item)
        gunspin.adapter = ArrayAdapter.createFromResource(this, R.array.Gunler, android.R.layout.simple_spinner_dropdown_item)
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
            Toasty.error(this,"Hata", Toast.LENGTH_SHORT).show();
        }
    }

    private fun kesimdenonce(data: Uri?) {
        val kesuri=Uri.fromFile(File(cacheDir,"cropped"))
        Crop.of(data,kesuri).asSquare().start(this)
    }

    inner class Resimskstir : AsyncTask<Uri, Void, ByteArray>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            uploadoncesidonustur(result!!)
        }

        override fun doInBackground(vararg params: Uri?): ByteArray?{
            val bitmap=MediaStore.Images.Media.getBitmap(this@Kayit.contentResolver,params[0])
            var resimByts:ByteArray?=null
            for (i in 1..5){
                resimByts=bitmaptanbyte(bitmap,100/i)
            }
            return resimByts
        }

        private fun bitmaptanbyte(bitmap: Bitmap, i: Int): ByteArray {
            val stream=ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,i,stream)
            return stream.toByteArray()
        }

    }

    private fun uploadoncesidonustur(result: ByteArray) {
        resimuri = result

    }

    private fun Resmiboyutlandir(uri: Uri) {
        val boyutlandir=Resimskstir()
        boyutlandir.execute(uri)
        picassoilecek(uri)

    }

    private fun kontrol() {
        dataref.child("Kullanicilar").child(uid!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.child("isim").getValue() != null) {
                            tokenata()
                            val intt = Intent(this@Kayit, YuklemeEkrani::class.java)
                            startActivity(intt)
                            finish()
                        } else {
                            setContentView(R.layout.activity_kayit)
                            spinnerarraylariata()
                            resimayarla.setOnClickListener { izinkontrol() }
                            kaydetbtn.setOnClickListener { kaydetolaylari() }
                        }
                    }
                })

    }

    private fun tokenata() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {

            val token = it.result.token
            Log.e("STOKEN", token)
            dataref.child("Kullanicilar").child(uid!!).child("Token").setValue(token)

        }
    }


    private fun picassoilecek(data: Uri) {
        Picasso.get().load(data).into(Gelenresim)
        resim = true
    }
}
