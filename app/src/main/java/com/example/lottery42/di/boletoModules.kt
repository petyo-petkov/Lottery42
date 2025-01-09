package com.example.lottery42.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.View
import android.webkit.WebView
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
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.factoryOf
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

    singleOf(::DatabaseRepoImpl).bind<DatabaseRepo>()

}

val networkModule = module {

    singleOf<HttpClient>(::HttpClient)

    singleOf(::NetworkRepoImpl).bind<NetworkRepo>()

}

val viewModelModule = module {
    viewModelOf(::ListScreenViewModel)
    viewModelOf(::ExtraInfoViewModel)
    viewModelOf(::DetailsViewModel)
}