package com.mesage.s.s_msagge

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.FirebaseStorage
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teksatir_haftaninenleri.view.*
import java.util.*

class HaftaninenleriRec(var alinanmesajsay: String,var arkadassayisi: String,var toplammesajsayisi: String,var tecrube: String,val itemClickListener: (Int) -> Unit) : RecyclerView.Adapter<HaftaninenleriRec.EnlerViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EnlerViewHolder {
        var intt = LayoutInflater.from(p0.context)
        var teksohbet = intt.inflate(R.layout.teksatir_haftaninenleri, p0, false)
        val viewholder = EnlerViewHolder(teksohbet,p0)
        return viewholder

    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: EnlerViewHolder, position: Int) {
        holder.katagoriyegoreresimata(position)
    }

    inner class EnlerViewHolder(val Viev: View, var ViewGroup:ViewGroup) : RecyclerView.ViewHolder(Viev) {
        val mavi = Color.BLUE
        val beyaz=Color.WHITE
        val yesil = Color.GREEN
        val sari=Color.YELLOW
        val mor=Color.MAGENTA
        val kirmizi=Color.RED
        val array= arrayListOf<Int>(mavi,beyaz,yesil,sari,mor,kirmizi)
        val teksatir=Viev as ConstraintLayout
        val tekresim=teksatir.HaftaninEnleriTekresim
        var pos=0
        fun katagoriyegoreresimata(position: Int) {
            when(position){
                0->{
                    resimata(toplammesajsayisi)
                    renkdegis(tekresim)
                    pos=position
                    tekresim.onClick(itemClickListener)
                }
                1->{
                    resimata(arkadassayisi)
                    pos=position
                    renkdegis(tekresim)
                    tekresim.onClick(itemClickListener)
                }
                2->{
                    pos=position
                    resimata(alinanmesajsay)
                    renkdegis(tekresim)
                    tekresim.onClick(itemClickListener)

                }
                3->{
                    resimata(tecrube)
                    pos=position
                    renkdegis(tekresim)
                    tekresim.onClick(itemClickListener)
                }

            }
        }

        private fun resimata(Uidkatagori: String) {
            FirebaseStorage.getInstance().getReference().child("ProfilRes").child(Uidkatagori).child("Profil.jpg").downloadUrl
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            uriyegoreata(it.result)
                        }
                    }
        }

        fun renkdegis(tekrsim:CircularImageView){
            val rand=Random()
            val randnumber=rand.nextInt(5)
            val randnumber2=rand.nextInt(5)
            val randnumber3=rand.nextInt(5)
            val coloranim = ValueAnimator.ofObject(ArgbEvaluator(),array[randnumber],array[randnumber2],array[randnumber3])
            coloranim.setDuration(6000)
            coloranim.repeatMode = ValueAnimator.REVERSE
            coloranim.repeatCount = ValueAnimator.INFINITE
            coloranim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(p0: ValueAnimator?) {
                    tekrsim.setBorderColor(p0!!.animatedValue as Int)
                }
            })
            coloranim.start()
        }
        private fun uriyegoreata(result: Uri?) {
            Picasso.get().load(result).into(tekresim)
        }
        fun <T : CircularImageView> T.onClick(event: ( position: Int) -> Unit): T {
            tekresim.setOnClickListener {
                event.invoke(pos)
            }
            return this
        }
    }



}