package com.app.monrotv.domain.models


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class VideoDownload(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id: Long = 0,
    @ColumnInfo(name = "video_id") @SerializedName("video_id") val videoId: Long,
    @ColumnInfo(name = "video_url") @SerializedName("video_url") val videoUrl: String,
    @ColumnInfo(name = "video_type") @SerializedName("video_type") val videoType: String,
    @ColumnInfo(name = "video_thumb") @SerializedName("video_thumb") val videoThumb: String,
    @ColumnInfo(name = "video_title") @SerializedName("video_title") val videoTitle: String,
    @ColumnInfo(name = "duration") @SerializedName("duration") val duration: String,
    @ColumnInfo(name = "user_id") @SerializedName("user_id") val userId: Long
//    // downloading = 1, downloaded = 2, failed = 3
//    @ColumnInfo(name = "download_status") @SerializedName("download_status") val downloadStatus: Int
): Parcelable
