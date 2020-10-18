package com.mesage.s.s_msagge


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mesage.s.s_msagge.services.presayfaadapter
import com.pixelcan.inkpageindicator.InkPageIndicator
import kotlinx.android.synthetic.main.fragment_blank.*


class prekesfet :DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_blank, container, false)
        val wpagerpre=v.findViewById<ViewPager>(R.id.Prebilgrescev)
        val indicator=v.findViewById<InkPageIndicator>(R.id.sayfabelirteci)
        val kapat=v.findViewById<ImageView>(R.id.prebilkapat)
        kapat.setOnClickListener { dismiss() }
        val satinal1=v.findViewById<TextView>(R.id.satinal_1)
        val satinal2=v.findViewById<TextView>(R.id.satinal_2)
        val satinal3=v.findViewById<TextView>(R.id.satinal_3)
        resimleriata(wpagerpre,indicator)
        return v
    }

    private fun resimleriata(wpagerpre: ViewPager?, indicator: InkPageIndicator?) {
        val array=ArrayList<prebilgdata>()
        array.add(prebilgdata(R.drawable.preres_dahafazlakisi,"Daha Fazla Kişiyle Tanış","Mesaj sınırın olmadığı için daha fazla insanla konuşma fırsatı yakala."))
        array.add(prebilgdata(R.drawable.preres_onecik,"Öne Çık","İnsanlar seni daha çabuk bulsun öne çık."))
        array.add(prebilgdata(R.drawable.preres_zamankazan,"Daha Fazla Zaman","Görüntülü konuşmada sınırsız zamanın olsun istediğin kadar konuş."))
        array.add(prebilgdata(R.drawable.preres_farkliinsanlar,"Farklı İnsanlar Tanı","Diğer kişiler tarafından beğenilmiş yada daha önce daha çok kişiyle konuşmuş insanlarla konuş."))
        val adpt=presayfaadapter(array)
        wpagerpre!!.adapter=adpt
        indicator!!.setViewPager(wpagerpre)

    }


}
