package com.mesage.s.s_msagge

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import kotlinx.android.synthetic.main.teksatir_mesaj.view.*

class MesajRec(var mesajlar: ArrayList<mesajdata>, val kullaniciuid: String, val konusulanuid: String) : RecyclerView.Adapter<MesajRec.MesajViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MesajViewHolder {
        var intt = LayoutInflater.from(p0.context)
        var teksohbet = intt.inflate(R.layout.teksatir_mesaj, p0, false)
        val viewholder = MesajViewHolder(teksohbet,p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return mesajlar.size
    }

    override fun onBindViewHolder(holder: MesajViewHolder, position: Int) {
        holder.mesajlariata(position)
    }

    inner class MesajViewHolder(val Viev: View, var ViewGroup:ViewGroup) : RecyclerView.ViewHolder(Viev) {


        val teksatirmesaj = Viev as LinearLayout
        val gelenmesaj = teksatirmesaj.Gelenmesaj
        val gidenmesaj = teksatirmesaj.Gidenmesaj
        val okundubilgisi = teksatirmesaj.OkundubilgisiImg
        fun mesajlariata(pos: Int) {

            if (mesajlar[pos].taraf == kullaniciuid) {
                gelenmesaj.visibility = View.INVISIBLE
                gidenmesaj.visibility = View.VISIBLE
                gidenmesaj.setText(mesajlar[pos].mesaj)
                if (mesajlar[pos].Okundubilgisi!!){
                    okundubilgisi.visibility=View.VISIBLE
                    okundubilgisi.setImageResource(R.drawable.ic_done_all_white_18dp)
                }else{
                    okundubilgisi.visibility=View.VISIBLE
                    okundubilgisi.setImageResource(R.drawable.ic_done_white_18dp)
                }
            } else if (mesajlar[pos].taraf!=kullaniciuid) {
                gidenmesaj.visibility=View.INVISIBLE
                okundubilgisi.visibility=View.INVISIBLE
                gelenmesaj.visibility = View.VISIBLE
                gelenmesaj.setText(mesajlar[pos].mesaj)
            }
        }




    }

}
