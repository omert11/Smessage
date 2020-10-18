package com.mesage.s.s_msagge

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_giris_ekrani.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class giris_ekrani : AppCompatActivity() {
  lateinit var Kullanicikontrol:FirebaseAuth.AuthStateListener
    val auth= FirebaseAuth.getInstance()
    val Callbackmanager=CallbackManager.Factory.create()
    var telno=""
    val RC_SIGN_IN =2
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(Kullanicikontrol)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris_ekrani)
        auth.setLanguageCode("tr")
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        Kullanicikontrol=FirebaseAuth.AuthStateListener {
            if (it.currentUser!=null){
                val intet=Intent(this,Kayit::class.java)
                startActivity(intet)
                finish()
            }
        }
        val googlegirisclent = GoogleSignIn.getClient(this,gso)
        googlegrsbttn.setOnClickListener { val girisintent = googlegirisclent.signInIntent
            startActivityForResult(girisintent,RC_SIGN_IN) }
        facebookgirisbtn.setReadPermissions("email","public_profile")
        facebookgirisbtn.registerCallback(Callbackmanager,object :FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                FacebookToken(result!!.accessToken)
            }

            override fun onCancel() {
                Toasty.error(this@giris_ekrani, getString(R.string.Giris_iptal), Toast.LENGTH_SHORT).show()

            }

            override fun onError(error: FacebookException?) {
                Toasty.error(this@giris_ekrani, getString(R.string.Giris_hata) + error, Toast.LENGTH_SHORT).show()

            }
        })
        try {
            val info = getPackageManager().getPackageInfo(
                    "com.mesage.s.s_msagge",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
        telefonnumarasi.setOnClickListener { visibtel() }
        gonderbtn.setOnClickListener { onaykoduyolla() }

    }

    private fun onaykoduyolla() {
        val telno11=teltx.text.toString()
        if (telno11.isNullOrEmpty()){
            Toasty.error(this,getString(R.string.Kontrol_Alan) , Toast.LENGTH_SHORT).show()

        }else{
            telno="+90"+telno11
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                   telno,60,TimeUnit.SECONDS,this,Geridonus
            )
        }
    }

    private fun visibtel() {
        val gorunur=View.VISIBLE
        val gorunmez=View.INVISIBLE
        GirisCons.visibility=gorunmez
        TelCons.visibility=gorunur
    }

    fun FacebookToken(token :AccessToken){
        val credantial= FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credantial)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val user = auth.currentUser
                        Toasty.success(this, getString(R.string.Giris_basarili), Toast.LENGTH_SHORT).show()

                    }else{
                        Toasty.success(this, getString(R.string.Giris_basarisiz), Toast.LENGTH_SHORT).show()

                    }
                }
    }
    val Geridonus = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential?) {

        }

        override fun onVerificationFailed(p0: FirebaseException?) {
        }

        override fun onCodeSent(verficid: String?, token: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(verficid, token)
            visibonay()
            kontrolmech(verficid,token)
        }
    }

    private fun visibonay() {
        val gorunur=View.VISIBLE
        val gorunmez=View.INVISIBLE
        TelCons.visibility=gorunmez
        onaycons.visibility=gorunur
    }

    private fun kontrolmech(verficid: String?, token: PhoneAuthProvider.ForceResendingToken?) {
        onaybtn.setOnClickListener {
            if (onaytx.text.isNullOrEmpty()){
                Toasty.error(this, getString(R.string.Kontrol_Alan), Toast.LENGTH_SHORT).show()

            }else{
                onayla(verficid!!,onaytx.text.toString())

            }
        }
        kodtekrarbtn.setOnClickListener { tekraryolla(token!!) }
    }

    private fun onayla(verficid: String, s: String) {
        val crendital=PhoneAuthProvider.getCredential(verficid,s)
        crendtallegirisyap(crendital)
    }

    fun tekraryolla(token:PhoneAuthProvider.ForceResendingToken){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                telno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                Geridonus,         // OnVerificationStateChangedCallbacks
                token)
    }


    private fun crendtallegirisyap(p0: PhoneAuthCredential?) {
        auth.signInWithCredential(p0 as AuthCredential)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Toasty.success(this, getString(R.string.Giris_basarili), Toast.LENGTH_SHORT).show()
                    }else{
                        Toasty.error(this, getString(R.string.Giris_basarisiz), Toast.LENGTH_SHORT).show()
                    }
                }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Callbackmanager.onActivityResult(requestCode,resultCode,data)
        if (requestCode==RC_SIGN_IN){
            if (data !=null) {
                val result = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val hesap = result.getResult(ApiException::class.java)
                    fireGirisGoogleile(hesap!!)
                    Toasty.success(this, getString(R.string.Giris_basarili), Toast.LENGTH_SHORT).show()

                } catch (e: ApiException) {
                    Log.w("hata", "signInResult:failed code=" + e.getStatusCode())
                    Toasty.error(this, getString(R.string.Giris_basarisiz), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun fireGirisGoogleile(hesap: GoogleSignInAccount) {
        Log.e("giris",hesap.id)
        val credetilal = GoogleAuthProvider.getCredential(hesap.idToken,null)
        auth.signInWithCredential(credetilal)
                .addOnCompleteListener {
                    if (it.isSuccessful){

                    }
                }
    }


}
