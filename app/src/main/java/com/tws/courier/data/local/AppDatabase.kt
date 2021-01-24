package com.tws.courier.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.monrotv.domain.models.VideoDownload

@Database(
    entities = arrayOf(
        VideoDownload::class
    ), version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDownloadDao(): VideoDownloadDao
}
