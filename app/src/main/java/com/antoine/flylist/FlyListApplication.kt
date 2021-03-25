package com.antoine.flylist

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class FlyListApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
