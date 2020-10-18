package com.mesage.s.s_msagge

import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teksatir_resim.view.*

class ProfilResRec(var resimler:ArrayList<Uri>): RecyclerView.Adapter<ProfilResRec.ResViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ResViewHolder {
        var intt = LayoutInflater.from(p0.context)
        var teksohbet = intt.inflate(R.layout.teksatir_resim, p0, false)
        val viewholder =  ResViewHolder(teksohbet,p0)
        return viewholder

    }

    override fun getItemCount(): Int {
      return  resimler.size
    }

    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {
        holder.resimata(position)
    }

    inner class ResViewHolder( Viev: View,var ii:ViewGroup) : RecyclerView.ViewHolder(Viev) {
        val tekresim = Viev as ConstraintLayout
        val resim = tekresim.tekresim
        fun resimata(position: Int) {
            val uri=resimler[position]
            Picasso.get().load(uri).centerCrop().resize(1000,1000).into(resim)
        }


    }




    }

