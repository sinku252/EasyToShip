package com.tws.courier.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.monrotv.domain.models.VideoDownload

@Dao
interface VideoDownloadDao {

    @Query("SELECT * FROM videodownload")
    suspend fun getAll(): List<VideoDownload>

    @Insert
    suspend fun insertAll(videos: List<VideoDownload>)

    @Insert
    suspend fun insert(video: VideoDownload)

    @Delete
    suspend fun delete(video: VideoDownload)

    @Query("DELETE FROM videodownload")
    suspend fun deleteAll()

    @Query("SELECT * FROM videodownload WHERE user_id=:userId")
    suspend fun getAllDownloads(userId: Long): List<VideoDownload>

    @Query("SELECT * FROM videodownload WHERE user_id=:userId AND video_url=:link LIMIT 1")
    suspend fun getIfDownloaded(userId: Long, link: String): VideoDownload?

    @Query("SELECT * FROM videodownload WHERE video_url=:videoUrl")
    suspend fun getAllDownloads(videoUrl: String): List<VideoDownload>

//    @Transaction
//    open suspend fun sync(makes: List<Make>) {
//        deleteAll()
//        insertAll(makes)
//    }
}
