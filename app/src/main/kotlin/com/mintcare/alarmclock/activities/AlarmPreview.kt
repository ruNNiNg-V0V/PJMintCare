package com.mintcare.alarmclock.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.mintcare.R
import com.mintcare.alarmclock.AlarmClock

import com.mintcare.alarmclock.data.Alarm
import com.mintcare.alarmclock.fragments.FragmentAlarmPreview
import com.mintcare.alarmclock.util.Constants

import kotlinx.android.synthetic.main.activity_alarm_preview.*

class AlarmPreview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_preview)
        /*setSupportActionBar(toolbar)
        setTitle("알람 설정");*/


        fab.setOnClickListener { view ->
            val intent = Intent(this, AlarmCreate::class.java)
            startActivity(intent)
        }

        refreshAlarmList()
    }

    fun refreshAlarmList() {
        val preferences: SharedPreferences = applicationContext.getSharedPreferences(Constants.PreferencesAlarms, Context.MODE_PRIVATE)

        alarms.removeAllViewsInLayout()

        val fm = fragmentManager
        val ft = fm.beginTransaction()

        val alarmList = preferences.getStringSet(Constants.AlarmList, mutableSetOf())
        alarmList.forEach {
            val frag = FragmentAlarmPreview.create(it)
            ft.add(alarms.id, frag, "preview$it")
        }

        ft.commit()
    }

    override fun onResume() {
        super.onResume()
        refreshAlarmList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_alarm_preview, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.disable_all -> {
                val preferences: SharedPreferences = applicationContext.getSharedPreferences(Constants.PreferencesAlarms, Context.MODE_PRIVATE)
                val alarmList = preferences.getStringSet(Constants.AlarmList, emptySet())

                val alarms = alarmList.mapNotNull { AlarmClock.gson.fromJson(preferences.getString(it, ""), Alarm::class.java) }

                alarms.forEach { alarm ->
                    alarm.enabled = false

                    val alarmData = AlarmClock.gson.toJson(alarm)
                    preferences.edit().also {
                        it.putString(alarm.id, alarmData)
                    }.apply()
                }

                refreshAlarmList()
                AlarmClock.instance.doWithService {
                    it.refreshAlarms()
                }
            }
            else -> return false
        }
        return true
    }
}
