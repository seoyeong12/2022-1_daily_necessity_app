package com.example.myapplication20200706

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication20200706.databinding.ActivityCycleBinding
import com.example.myapplication20200706.databinding.DialogInputBinding
import com.example.myapplication20200706.databinding.ItemMainBinding

class CycleViewHolder(val binding:ItemMainBinding):RecyclerView.ViewHolder(binding.root)
class CycleAdapter(val context: Context, val itemList:MutableList<ItemData>):RecyclerView.Adapter<CycleViewHolder>(){
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CycleViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return CycleViewHolder(ItemMainBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: CycleViewHolder, position: Int) {
        val binding2=holder.binding.root
        val data=itemList.get(position)
        holder.binding.run{
            itemEmailView.text=data.email
            itemDateView.text=data.date
            itemCategoryView.text=" 카테고리:  "+data.category
            itemContentView.text="        이름:   "+data.content
            itemCountView.visibility= View.GONE
            itemCycleView.text="        주기:    "+data.cycle.toString()+"   (개월)"
        }

        binding2.setOnClickListener{
            val inflater=binding2.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rootView = inflater.inflate(R.layout.activity_cycle,null)
            val dialogBinding=DialogInputBinding.inflate(inflater)
            AlertDialog.Builder(binding2.context).run{
                setTitle("주기를 수정하시겠습니까?")
                setView(dialogBinding.root)
                setPositiveButton("저장",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if(p1==DialogInterface.BUTTON_POSITIVE){
                            var map= mutableMapOf<String,Any>()
                            map["cycle"]="${dialogBinding.editCycle.getText()}"
                            MyApplication.db.collection("news")
                                .document("${data.docId}").update(map)
                                .addOnSuccessListener {
                                    notifyDataSetChanged()
                                    val intent=Intent(holder.binding.root.context,CycleActivity::class.java)
                                    intent.putExtra("notify2","2")
                                    holder.binding.root.context.startActivity(intent)
                                    Log.d("mobileApp","업")
                                }
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
class CycleActivity : AppCompatActivity() {
    lateinit var adapter: CycleAdapter
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info,menu)
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityCycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.toolbar.title="주기 관리"

        MyApplication.db.collection("news")
            .get()
            .addOnSuccessListener { result->
                val itemList= mutableListOf<ItemData>()
                for(document in result){
                    val item=document.toObject(ItemData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                }
                adapter=CycleAdapter(this,itemList)
                binding.cycleRecyclerView.layoutManager= LinearLayoutManager(this)
                binding.cycleRecyclerView.adapter= adapter
                if(intent.getStringExtra("notify2")=="2"){
                    adapter!!.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId=== R.id.info){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("생필품 교체 주기").setMessage("칫솔 - 3개월\n" +
                    "샤워볼 - 1개월\n" +
                    "샤워기필터 - 2~3개월\n" +
                    "수건 - 1년\n" +
                    "수세미 - 1개월\n" +
                    "도마 - 1년\n" +
                    "행주 - 1개월\n" +
                    "고무장갑 - 3개월\n" +
                    "플라스틱 용기 - 3개월\n" +
                    "멀티탭 - 2~3년\n" +
                    "매트릭스 - 5~7년\n" +
                    "면도기 or 면도날 - 2주\n" +
                    "렌즈통 - 1개월\n" +
                    "인공눈물 - 1~2개월 이내\n"+
                    "메이크업브러쉬 - 1년\n" +
                    "빗 - 6개월\n" +
                    "베개솜 - 1년\n")
            val alertDialog = builder.create()
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
