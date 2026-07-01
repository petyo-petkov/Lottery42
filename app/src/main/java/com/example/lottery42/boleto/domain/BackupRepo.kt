package com.example.lottery42.boleto.domain

import android.net.Uri

interface BackupRepo {
    suspend fun backupDatabase(uri: Uri): Result<Unit>
    suspend fun restoreDatabase(uri: Uri): Result<Unit>
}
