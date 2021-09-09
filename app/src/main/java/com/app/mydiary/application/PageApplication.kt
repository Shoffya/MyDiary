package com.app.mydiary.application

import android.app.Application
import com.app.mydiary.helper.HelperDB

class PageApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: PageApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}