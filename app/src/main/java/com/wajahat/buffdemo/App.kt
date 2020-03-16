package com.wajahat.buffdemo

import android.app.Application
import com.wajahat.buffup.BuffUp
import timber.log.Timber

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        BuffUp.initialize()
    }
}