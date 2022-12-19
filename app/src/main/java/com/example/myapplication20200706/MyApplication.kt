package com.example.myapplication20200706

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication: MultiDexApplication() {
    companion object{
        var networkService:NetworkService
        val retrofit:Retrofit
            get()=Retrofit.Builder()
                .baseUrl("https://api.odcloud.kr/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init{
            networkService= retrofit.create(NetworkService::class.java)
        }
        lateinit var auth: FirebaseAuth
        var email:String?=null
        lateinit var db : FirebaseFirestore
        lateinit var storage: FirebaseStorage
        var cate:String?=null
        var count:Int?=0
        var cyclecount:Int ?=0
        fun checkAuth(): Boolean{
            var currentUser = auth.currentUser
            return currentUser?.let{
                email = currentUser.email
                currentUser.isEmailVerified
            }?: let{
                false
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        KakaoSdk.init(this,"a77a2c77cb87459dc36691b9ac12ba18")

        db =FirebaseFirestore.getInstance()
        storage =Firebase.storage
    }
}