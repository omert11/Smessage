package com.mesage.s.s_msagge

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mesage.s.s_msagge.services.sohbetdata
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teksatir_sohbet.view.*
import java.util.*

class SohbetRec(val sohbettekiler: ArrayList<sohbetdata>, val itemClickListener: (View, Int, Int) -> Unit, val konusmaadi: String,val Cont:Context) : RecyclerView.Adapter<SohbetRec.SohbetlerViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SohbetlerViewHolder {
        var intt = LayoutInflater.from(p0.context)
        var teksohbet = intt.inflate(R.layout.teksatir_sohbet, p0, false)
        val viewholder = SohbetlerViewHolder(teksohbet)
        viewholder.onClick(itemClickListener)
        return viewholder

    }

    override fun getItemCount(): Int {
        return sohbettekiler.size
    }

    override fun onBindViewHolder(holder: SohbetlerViewHolder, position: Int) {
        holder.atama(position)
        holder.sohbetresim.isClickable = true
        holder.sohbetisim.text = "Bilinmeyen" + Kodolustur(FirebaseAuth.getInstance().currentUser!!.uid, sohbettekiler[position].uid!!)
        holder.okunmamislistener(position)
    }

    inner class SohbetlerViewHolder(Viev: View) : RecyclerView.ViewHolder(Viev) {
        val teksatirsohbetgoruntusu = Viev as ConstraintLayout
        val sohbetisim = teksatirsohbetgoruntusu.SohbetKisiisim
        val sohbetresim = teksatirsohbetgoruntusu.Sohbetkisiresim
        val sohbetsonmesaj = teksatirsohbetgoruntusu.SohbetSonMesaj
        val sohbetsaat = teksatirsohbetgoruntusu.SohbetSaat
        val sohbetokunmamis = teksatirsohbetgoruntusu.SohbetOkunmamismesaj
        val gelengiden=teksatirsohbetgoruntusu.Sohbettaraf
        fun atama(position: Int) {
            val uid = sohbettekiler[position].uid!!
            val kisiuid = FirebaseAuth.getInstance().currentUser!!.uid
            val taraf=sohbettekiler[position].taraf!!
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                    .child(kisiuid).child(konusmaadi).child(uid).child("Durum")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.child("15").getValue() != null) {
                                val resimdurum = p0.child("15").getValue() as Boolean
                                val isimdurum = p0.child("40").getValue() as Boolean
                                val arkadasdurum = p0.child("75").getValue() as Boolean
                                if (resimdurum) {
                                    sohbetisim.text = "Bilinmeyen" + Kodolustur(kisiuid, uid)
                                    resimata(uid)
                                }
                                if (isimdurum) {
                                    isimata(uid)
                                    resimata(uid)
                                }
                                if (arkadasdurum) {
                                    arkadaslogosucagir()
                                }

                            }
                        }
                    })
            if (taraf=="gelen"){
                gelengiden.background=Cont.getDrawable(R.drawable.yuvbilgcerceve)
                gelengiden.text="Gelen Konuşma"
                gelengiden.setTextColor(Cont.resources.getColor(R.color.colorPrimary))
            }else{
                gelengiden.background=Cont.getDrawable(R.drawable.yuvbilgcercevemor)
                gelengiden.text="Giden Konuşma"
                gelengiden.setTextColor(Cont.resources.getColor(R.color.mors))
            }
        }

        private fun arkadaslogosucagir() {

        }

        private fun isimata(uid: String) {

            FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                    .child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val isim = p0.child("isim").getValue() as String
                            sohbetisim.text = isim
                        }

                    })
        }

        private fun resimata(uid: String) {
            if (sohbetresim.isClickable) {
                FirebaseStorage.getInstance().getReference().child("ProfilRes").child(uid)
                        .child("Profil.jpg").downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val uri = it.result!!
                        Picasso.get().load(uri).into(sohbetresim)
                        sohbetresim.isClickable == false

                    }
                }

            }
        }

        fun okunmamislistener(Pos: Int) {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid).child(konusmaadi).child(sohbettekiler[Pos].uid!!).child("MesajBilgisi")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.child("okunmamis").getValue() != null) {
                                val okunmamissayisi = p0.child("okunmamis").getValue() as String
                                sohbetokunmamis.setText(okunmamissayisi)
                                sohbetokunmamis.visibility=View.VISIBLE

                            }else{
                                sohbetokunmamis.visibility=View.INVISIBLE
                            }
                            if (p0.child("mesaj").getValue()!=null){
                                val sonmesaj=p0.child("mesaj").getValue() as String
                                val tarih = p0.child("saat").getValue() as String
                                sohbetsonmesaj.text=sonmesaj
                                sohbetsaat.text=tarih
                            }else{
                                sohbetsonmesaj.text="Bir konusma baslat"
                            }

                        }

                    })
        }

    }

    private fun Kodolustur(uidkullanicis: String, uidkonusucus: String): String {
        val code1 = uidkullanicis.hashCode()
        val code2 = uidkonusucus.hashCode()
        val code = code1 + code2
        return code.toString()
    }

    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
        }
        return this
    }
}
/*FirebaseDatabase.getInstance().getReference().child("Kullanicilar")
                    .child(uid)
                    .addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                        }

                    })

                    val mesajsayisi = p0.child(uid).child("MesajSayisi").getValue() as String
                            if (mesajsayisi.toInt() < 5) {

                                sohbetisim.text = "Anon"+Kodolustur(kisiuid,uid)
                            } else if (mesajsayisi.toInt() < 20) {

                                sohbetisim.text = "Anon"+Kodolustur(kisiuid,uid)
                                resimata(uid)
                            } else {
                                isimata(uid)
                                resimata(uid)
                            }

                    */