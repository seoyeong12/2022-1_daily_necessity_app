package com.example.myapplication20200706

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.myapplication20200706.MyApplication.Companion.cyclecount


/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings,rootKey)
        val alarmPreference: ListPreference?=findPreference("alarm_cycle")
        alarmPreference?.summaryProvider= Preference.SummaryProvider<ListPreference> {
                preference ->
            val c=preference.value?.toInt()
            if(c==0){
                "알람이 울릴 주기를 설정할 수 있습니다."
            }
            else{
                c.toString()
            }
        }
    }
}