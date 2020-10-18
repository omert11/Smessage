package com.mesage.s.s_msagge


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EngelFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_engel, container, false)
        val uyarimesajitx = v.findViewById<TextView>(R.id.mesajtx2)
        val hayirbtn = v.findViewById<Button>(R.id.hayirbtn2)
        uyarimesajitx.setText("Mesaj aşamasına onay vermediniz yada karşı taraf onaylamadı")
        hayirbtn.setOnClickListener { dismiss() }

        return v
    }


}
