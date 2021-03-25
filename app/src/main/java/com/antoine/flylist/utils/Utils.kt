package com.antoine.flylist.utils

import android.content.Context
import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Logger
import kotlin.math.pow

class Utils {
    companion object {
        fun epochToReadableDate(epoch: Long): String {
            return if (Build.VERSION.SDK_INT >= 26) {
                DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(epoch))
            } else {
                SimpleDateFormat.getDateTimeInstance().format(Date(epoch * 1000))
            }.replace("T", " ").replace("Z", " ").trim()
        }

        fun dateToEpoch(date: Date): Long {
            return (date.time / 10.0.pow(date.time.toString().length - 10)).toLong()
        }

        fun loadImageFromURL(url: String, context: Context, view: ImageView) {
            Logger.getGlobal().info("Loading image from: $url")
            Glide
                .with(context)
                .load(url)
                .override(1000, 55)
                .fitCenter()
                .into(view)
        }
    }
}
