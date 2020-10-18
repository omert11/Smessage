package com.mesage.s.s_msagge


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UyariFragment : DialogFragment() {
    var artiralacaktec = 0
    val uidkullanici = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_uyari, container, false)
        val uyarimesajitx = v.findViewById<TextView>(R.id.mesajtx)
        val evetbtn = v.findViewById<Button>(R.id.evetbtn)
        val hayirbtn = v.findViewById<Button>(R.id.hayirbtn)
        uyarimesajitx.setText(gidecekmesaj)
        tecrubecek()
        evetbtn.setOnClickListener { kontrolvetrueatama() }
        hayirbtn.setOnClickListener { kontrolvefalseatama() }

        return v
    }

    private fun tecrubecek() {
        FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child("Tecrube")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val tec = p0.getValue() as String
                        val arttir = tec.toInt()
                        artiralacaktec = arttir
                    }
                })
    }

    private fun kontrolvefalseatama() {
        when (gidecekmesaj) {
            getString(R.string.Soru_Profılres) -> {
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum").child("15").setValue(false)
                dismiss()
            }
            getString(R.string.Soru_Profılism)-> {
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum").child("40").setValue(false)
                dismiss()
            }
            getString(R.string.Soru_Ark) -> {
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum").child("75").setValue(false)
                dismiss()
            }
        }
    }

    private fun kontrolvetrueatama() {
        when (gidecekmesaj) {
            getString(R.string.Soru_Profılres) -> {
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum").child("15").setValue(true)
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child("Tecrube").setValue((artiralacaktec + 25).toString())

                dismiss()
            }
            getString(R.string.Soru_Profılism) -> {
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum").child("40").setValue(true)
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child("Tecrube").setValue((artiralacaktec + 100).toString())

                dismiss()
            }
            getString(R.string.Soru_Ark) -> {
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child(taraf).child(uidkonusucu).child("Durum").child("75").setValue(true)
                FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uidkullanici).child("Tecrube").setValue((artiralacaktec + 250).toString())
                dismiss()
            }
        }
    }


}
