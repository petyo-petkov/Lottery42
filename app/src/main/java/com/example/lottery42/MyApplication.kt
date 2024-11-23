package com.example.lottery42

import android.app.Application
import com.example.lottery42.di.databaseModule
import com.example.lottery42.di.scannerModule
import com.example.lottery42.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class  MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(scannerModule, viewModelModule, databaseModule)

        }

    }

}