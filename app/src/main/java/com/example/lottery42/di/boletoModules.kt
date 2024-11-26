package com.example.lottery42.di

import android.app.Application
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.lottery42.boleto.data.ScannerRepoImpl
import com.example.lottery42.boleto.data.database.DatabaseRepoImpl
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.ScannerRepo
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenViewModel
import com.example.sqldelight.AppDatabase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scannerModule = module {

    single<Context> { get<Application>().applicationContext }

    single {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_CODE_128
            )
            .build()
    }

    single {
        val context: Context = get()
        val options: GmsBarcodeScannerOptions = get()
        GmsBarcodeScanning.getClient(context, options)
    }

    singleOf(::ScannerRepoImpl).bind<ScannerRepo>()

//factoryOf(::ScannerRepoImpl).bind<ScannerRepo>()

}

val databaseModule = module {

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = get(),
            name = "boletosDB"
        )
    }
    single {
        AppDatabase(get())
    }

    single {
        get<AppDatabase>().boletoQueries
    }

    singleOf(::DatabaseRepoImpl).bind<DatabaseRepo>()

}

val viewModelModule = module {
    viewModelOf(::ListScreenViewModel)
}