package com.mintcare.alarmclock.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mintcare.R
import com.mintcare.alarmclock.AlarmClock
import com.mintcare.alarmclock.AlarmService
import com.mintcare.alarmclock.data.Alarm
import com.mintcare.alarmclock.util.Constants
import kotlinx.android.synthetic.main.activity_alarm_active.*


class AlarmActive : AppCompatActivity(), AlarmService.SnoozeListener {

    lateinit var alarm: Alarm
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_active)

        AlarmClock.instance.doWithService {
            it.snoozeListener = this
        }

        val preferences: SharedPreferences = applicationContext.getSharedPreferences(Constants.PreferencesAlarms, Context.MODE_PRIVATE)

        val args: Bundle? = intent.extras
        val id = args?.getString(Constants.AlarmID, "") ?: ""
        if (id.isBlank()) {
            finish()
        } else {
            alarm = AlarmClock.gson.fromJson<Alarm>(preferences.getString(id, ""), Alarm::class.java)
        }

        labelAlarmName.text = alarm.name

        if (alarm.snoozeTime == 0) buttonSnooze.visibility = View.GONE
        buttonSnooze.setOnClickListener {
            onSnooze()
            AlarmClock.instance.doWithService {
                it.snoozeAlarm()
            }
        }

        buttonOff.setOnClickListener {
            AlarmClock.instance.doWithService {
                it.stopAlarm()
            }
            finish()
        }
    }

    override fun onBackPressed() {
        onSnooze()
        AlarmClock.instance.doWithService {
            it.snoozeAlarm()
        }
    }

    override fun onSnooze() {
        if (buttonSnooze.isEnabled) {
            buttonSnooze.isEnabled = false
            handler.postDelayed({
                buttonSnooze.isEnabled = true
            }, alarm.snoozeTime * 60000L)
        }
    }
}