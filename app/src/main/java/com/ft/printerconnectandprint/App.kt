package com.ft.printerconnectandprint

import android.app.Application
import com.ft.printerconnectandprint.data.Prefs
import com.mazenrashed.printooth.Printooth
import dagger.hilt.android.HiltAndroidApp

val prefs: Prefs by lazy {
    App.prefs!!
}
@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        Printooth.init(this)
    }

    companion object {
        var prefs: Prefs? = null
        lateinit var instance: App
            private set
    }
}