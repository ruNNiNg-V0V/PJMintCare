package com.mintcare.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mintcare.alarmclock.util.Constants

class Autostart : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val i = Intent(context, AlarmService::class.java).apply {
            action = Constants.ActionInit
        }
        context.startService(i)
    }
}