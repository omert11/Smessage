package com.mesage.s.s_msagge

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_ayarlar.*

class Ayarlar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayarlar)
        buttonlar()
        toolbar3.setTitle("Ayarlar")
        toolbar3.setTitleTextColor(resources.getColor(R.color.Beyaz))
        setSupportActionBar(toolbar3)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(resources.getDrawable(R.drawable.beyaz_back))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home->{
                onBackPressed()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun buttonlar() {
        val k_gri=resources.getColor(R.color.k_gri)
        val beyaz=resources.getColor(R.color.Beyaz)
        ay_Prof.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_Prof.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_Prof.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",0)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_Prof.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_cıkıs.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_cıkıs.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_cıkıs.setBackgroundColor(beyaz)
                    FirebaseAuth.getInstance().signOut()
                    val intt = Intent(this, giris_ekrani::class.java)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_cıkıs.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_Bıl.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_Bıl.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_Bıl.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",4)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_Bıl.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_Eng.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_Eng.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_Eng.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",3)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_Eng.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_Od.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_Od.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_Od.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",1)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_Od.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_Ver.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_Ver.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_Ver.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",2)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_Ver.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_sorun.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_sorun.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_sorun.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",6)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_sorun.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
        ay_yar.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ay_yar.setBackgroundColor(k_gri)
                }
                MotionEvent.ACTION_UP -> {
                    ay_yar.setBackgroundColor(beyaz)
                    val intt = Intent(this, Detay_ayar::class.java)
                    intt.putExtra("secim",5)
                    startActivity(intt)
                }
                MotionEvent.ACTION_CANCEL -> {
                    ay_yar.setBackgroundColor(beyaz)
                }
            }
            return@setOnTouchListener true
        }
    }
}
