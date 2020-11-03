package com.mintcare.alarmclock

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.google.gson.Gson

import com.mintcare.alarmclock.util.Constants
import com.mintcare.alarmclock.util.Util

class AlarmClock : Application() {
    var serviceBinder: AlarmService.AlarmBinder? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        Util.ringtones += Util.getRingtones(this)

        val si = Intent(this, AlarmService::class.java).apply {
            action = Constants.ActionInit
        }

        bindService(si, Connection(this), Context.BIND_AUTO_CREATE or Context.BIND_ABOVE_CLIENT)
    }

    class Connection(val parent: AlarmClock) : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            parent.serviceBinder = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            parent.serviceBinder = service as? AlarmService.AlarmBinder
            parent.serviceBinder?.also { binder ->
                ServiceListeners.forEach { it(binder) }
            }
        }
    }

    fun doWithService(func: (AlarmService.AlarmBinder) -> Unit) {
        if (serviceBinder != null) {
            func(serviceBinder!!)
        } else {
            ServiceListeners += func
        }
    }

    companion object {
        val gson = Gson()
        val ServiceListeners = mutableListOf<(AlarmService.AlarmBinder) -> Unit>()
        lateinit var instance: AlarmClock //현재 일어나는 오류 lateinit property instance has not been initialized
            private set
    }
}