package com.example.lottery42.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.lottery42.boleto.data.ScannerRepoImpl
import com.example.lottery42.boleto.data.database.DatabaseRepoImpl
import com.example.lottery42.boleto.data.network.NetworkRepoImpl
import com.example.lottery42.boleto.domain.DatabaseRepo
import com.example.lottery42.boleto.domain.NetworkRepo
import com.example.lottery42.boleto.domain.ScannerRepo
import com.example.lottery42.boleto.presentation.boleto_detail.DetailsViewModel
import com.example.lottery42.boleto.presentation.boleto_list.ListScreenViewModel
import com.example.lottery42.boleto.presentation.extra_info.ExtraInfoViewModel
import com.example.sqldelight.AppDatabase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scannerModule: Module = module {

    single<GmsBarcodeScanner> {
        val app: Application = get()
        val options: GmsBarcodeScannerOptions =
            GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_CODE_128
                )
                .build()
        GmsBarcodeScanning.getClient(app.applicationContext, options)
    }

    singleOf(::ScannerRepoImpl) bind ScannerRepo::class

}

val databaseModule = module {

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = get(),
            name = "boletos_DB"
        )
    }
    single {
        AppDatabase(get())
    }

    single {
        get<AppDatabase>().boletoQueries
    }

    singleOf(::DatabaseRepoImpl) bind DatabaseRepo::class

}

val networkModule = module {

    //singleOf<HttpClient>(::HttpClient)

    singleOf(::NetworkRepoImpl).bind<NetworkRepo>()

}

val viewModelModule = module {
    viewModelOf(::ListScreenViewModel)
    viewModelOf(::ExtraInfoViewModel)
    viewModelOf(::DetailsViewModel)
}