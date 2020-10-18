package com.mesage.s.s_msagge

import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teksatir_res.view.*

class Sayfaadapter( var Resimlist:ArrayList<Uri>):PagerAdapter() {
    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return (p0==p1 as ConstraintLayout)
    }

    override fun getCount(): Int {
        return Resimlist.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inf=LayoutInflater.from(container.context)
        val view=inf.inflate(R.layout.teksatir_res,container,false)
        val res=view.profil_gozat_res
        Picasso.get().load(Resimlist[position]).into(res)
        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, obje: Any) {
        container.removeView(obje as ConstraintLayout)
    }
}