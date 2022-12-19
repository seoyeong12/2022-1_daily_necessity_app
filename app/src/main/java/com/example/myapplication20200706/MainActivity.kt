package com.example.myapplication20200706

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.get
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch20_firebase.util.myCheckPermission
import com.example.myapplication20200706.MyApplication.Companion.cate
import com.example.myapplication20200706.MyApplication.Companion.count
import com.example.myapplication20200706.MyApplication.Companion.cyclecount
import com.example.myapplication20200706.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toogle: ActionBarDrawerToggle
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        count=0
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toogle= ActionBarDrawerToggle(this,binding.drawer,R.string.drawer_open,R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toogle.syncState()

        binding.swipe.setOnRefreshListener {
            Handler().postDelayed({
                if(binding.swipe.isRefreshing) binding.swipe.isRefreshing=false
            },1500)

        }
        /*val keyHash = Utility.getKeyHash(this)
        Log.d("mobileApp",keyHash)*/
        /*var keyHash = Utility.getKeyHash(this)
        Log.d("mobileApp5",keyHash)*/
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this)
        cyclecount= sharedPreferences.getString("alarm_cycle",null)?.toInt()
        myCheckPermission(this)
        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            if(binding.btnLogin.text.equals("로그인"))
                intent.putExtra("data","logout")
            else if(binding.btnLogin.text.equals("로그아웃"))
                intent.putExtra("data","login")
            startActivity(intent)
        }
        binding.mainDrawerView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.itemdate ->{
                    val intent =Intent()
                    intent.action="ACTION_EDIT"
                    intent.data= Uri.parse("http://www.google.com")
                    startActivity(intent)
                    true
                }
                R.id.itemprice ->{
                    val intent =Intent()
                    intent.action="ACTION_PRICE"
                    intent.data= Uri.parse("http://www.google.com")
                    startActivity(intent)
                    true
                }
            }
            true
        }

        binding.lv.setOnItemClickListener{parent,view,position,id ->

            val intent =Intent()
            intent.action="ACTION_ITEM"
            intent.data= Uri.parse("http://www.google.com")
            val temp=resources.getStringArray(R.array.category)
            cate=temp[position!!]
            startActivity(intent)
            true
        }
    }
    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth() || MyApplication.email != null){
            binding.btnLogin.text="로그아웃"
            binding.authTv.text="${MyApplication.email}님 반갑습니다."
            binding.authTv.textSize=16F
            binding.lv.visibility=View.VISIBLE
            binding.tvca.visibility=View.VISIBLE
        }
        else{
            binding.btnLogin.text="로그인"
            binding.authTv.text="덕성 모바일"
            binding.authTv.textSize=24F
            binding.lv.visibility=View.GONE
            binding.tvca.visibility=View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item))return true
        if(item.itemId==R.id.menu_main_setting){
            val intent=Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }
}