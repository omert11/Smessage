package com.mesage.s.s_msagge.services

import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mesage.s.s_msagge.R
import com.mesage.s.s_msagge.prebilgdata
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teksatir_prebilgres.view.*

class presayfaadapter ( var Resimlist:ArrayList<prebilgdata>): PagerAdapter() {
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return (p0==p1 as ConstraintLayout)
    }

    override fun getCount(): Int {
        return Resimlist.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inf= LayoutInflater.from(container.context)
        val view=inf.inflate(R.layout.teksatir_prebilgres,container,false)
        val res=view.prebilgirestek
        val baslik=view.BilgBaslik
        val aciklama=view.BilgAciklama
        baslik.setText(Resimlist[position].baslik)
        aciklama.setText(Resimlist[position].aciklama)
        Picasso.get().load(Resimlist[position].res!!).into(res)
        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, obje: Any) {
        container.removeView(obje as ConstraintLayout)
    }
}