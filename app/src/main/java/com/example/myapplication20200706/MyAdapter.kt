package com.example.myapplication20200706

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication20200706.MyApplication.Companion.cate
import com.example.myapplication20200706.MyApplication.Companion.count
import com.example.myapplication20200706.MyApplication.Companion.cyclecount
import com.example.myapplication20200706.databinding.DialogInputBinding
import com.example.myapplication20200706.databinding.ItemMainBinding
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MyViewHolder(val binding: ItemMainBinding):RecyclerView.ViewHolder(binding.root)
class MyAdapter(val context: Context, val itemList:MutableList<ItemData>):RecyclerView.Adapter<MyViewHolder>(){
    override fun getItemCount(): Int {
        return itemList?.size ?:0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return MyViewHolder(ItemMainBinding.inflate(layoutInflater))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data=itemList.get(position)
        holder.binding.run{
            if(data.category.toString().equals(cate)){
                itemEmailView.text=data.email
                itemDateView.text= Instant.ofEpochMilli(data.date.toString().toLong()).atZone(ZoneId.systemDefault()).toLocalDate().toString()
                itemCategoryView.text=" 카테고리:  "+data.category.toString()
                itemContentView.text="        이름:   "+data.content
                itemCountView.text= "        수량:    "+data.count.toString()+"    (개)"
                itemCycleView.text="        주기:    "+data.cycle.toString()+"   (개월)"
            }
            else{
                root.visibility=View.GONE
                itemEmailView.visibility=View.GONE
                itemDateView.visibility=View.GONE
                itemCategoryView.visibility=View.GONE
                itemContentView.visibility=View.GONE
                itemCountView.visibility=View.GONE
                itemCycleView.visibility=View.GONE
            }
        }
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(holder.binding.root.context)
        if(count==0) {
            val now = System.currentTimeMillis()
        if(sharedPreferences.getBoolean("cycle",false)){
            if(data.category.toString().equals(cate)) {
            val datec= (now-data.date!!.toString().toLong())/(24*60*60*1000)
            if (datec != null && datec>0) {
                    val ad= datec/(data.cycle!!.toDouble()*30)
                    var map= mutableMapOf<String,Any>()
                    map["count"]=(data.count!!.toInt()-ad.toInt()).toString()
                    map["date"]=Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis.toString()
                    MyApplication.db.collection("news")
                        .document("${data.docId}").update(map)
                        .addOnSuccessListener {
                            val intent=Intent(holder.binding.root.context,ItemMainActivity::class.java)
                            intent.putExtra("notify","2")
                            holder.binding.root.context.startActivity(intent)
                            Log.d("mobileApp","업2")
                        }
                        .addOnFailureListener{
                            Log.d("mobileApp","data save error")
                        }
            }
            count=1}
        }}
        val alarmsound=sharedPreferences.getBoolean("alarm_sound",false)
        val alarmset=sharedPreferences.getBoolean("alarm_setting",false)
        if(data.count.toString()<=cyclecount.toString()&& alarmset==true){
            if(data.category.toString().equals(cate)) {
                holder.binding.root.setBackgroundColor(Color.RED)
                val manager = holder.binding.root.context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
                val builder: NotificationCompat.Builder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val ch_id = "one-channel"
                    val ch_id2="two-channel"
                        val channel = NotificationChannel(
                            ch_id,
                            "My Channel One",
                            NotificationManager.IMPORTANCE_DEFAULT
                        )
                        val channel2=NotificationChannel(
                            ch_id2,
                            "My Channel Two",
                            NotificationManager.IMPORTANCE_LOW
                        )
                    if(alarmsound){
                        channel.apply {
                            description = "My Channel One 소개"
                            setShowBadge(true)
                            enableLights(true)
                            lightColor = Color.RED
                            enableVibration(true)
                            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                            val audio_attr = AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build()
                            setSound(uri, audio_attr)

                        }
                        manager.createNotificationChannel(channel)
                        builder = NotificationCompat.Builder(holder.binding.root.context, ch_id)
                    }
                    else{
                        channel2.apply {
                            description = "My Channel Two 소개"
                            setShowBadge(true)
                            enableLights(true)
                            lightColor = Color.RED
                        }
                        manager.createNotificationChannel(channel2)
                        builder = NotificationCompat.Builder(holder.binding.root.context, ch_id2)
                    }
                    } else {
                        builder = NotificationCompat.Builder(holder.binding.root.context)
                    }
                    builder.run {
                        setSmallIcon(androidx.appcompat.R.drawable.abc_btn_check_material_anim)
                        setWhen(System.currentTimeMillis())
                        setContentTitle("생필품 알람")
                        setContentText("${data.content}가 ${data.count}개 남았어요!")
                    }
                    manager.notify(11, builder.build())
                }
        }
        holder.binding.root.setOnClickListener {
            val inflater=holder.binding.root.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rootView = inflater.inflate(R.layout.activity_cycle,null)
            val dialogBinding= DialogInputBinding.inflate(inflater)
            AlertDialog.Builder(holder.binding.root.context).run{
                setTitle("수량을 수정하시겠습니까?")
                setView(dialogBinding.root)
                setPositiveButton("저장",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if(p1== DialogInterface.BUTTON_POSITIVE){
                            var map= mutableMapOf<String,Any>()
                            map["count"]="${dialogBinding.editCycle.text}"
                            map["date"]=Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis.toString()
                            MyApplication.db.collection("news")
                                .document("${data.docId}").update(map)
                                .addOnSuccessListener {
                                    Log.d("mobileApp","업")
                                    val intent=Intent(holder.binding.root.context,ItemMainActivity::class.java)
                                    intent.putExtra("notify","2")
                                    holder.binding.root.context.startActivity(intent)                                }
                                .addOnFailureListener{
                                    Log.d("mobileApp","data save error")
                                }
                        }
                        }
                })
                setNegativeButton("취소",null)
                show()
            }
        }
    }
}


