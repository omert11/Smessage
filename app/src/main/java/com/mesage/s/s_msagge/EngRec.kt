package com.mesage.s.s_msagge

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.teksatir_eng.view.*

class EngRec(var Englist: ArrayList<String>) : RecyclerView.Adapter<EngRec.EngViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EngViewHolder {
        var intt = LayoutInflater.from(p0.context)
        var teksohbet = intt.inflate(R.layout.teksatir_eng, p0, false)
        val viewholder = EngViewHolder(teksohbet,p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return Englist.size
    }

    override fun onBindViewHolder(holder: EngViewHolder, position: Int) {
        holder.isimata(position)
    }

    inner class EngViewHolder(val Viev: View, var ViewGroup:ViewGroup) : RecyclerView.ViewHolder(Viev) {
        val teksatirkisi = Viev as ConstraintLayout
        val tekisim=teksatirkisi.engelkisi

        fun isimata(position: Int) {
            val uid=Englist[position]
            FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(uid).child("isim")
                    .addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                           if (p0.exists()) {
                               tekisim.setText(p0.getValue() as String)
                           }
                        }

                    })
        }



    }

}
