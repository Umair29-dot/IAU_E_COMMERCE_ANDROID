package com.example.finalprojectpractice.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.finalprojectpractice.R
import com.example.finalprojectpractice.activities.ShoppingActivity
import com.example.finalprojectpractice.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)

		if(message.data != null) {
			generateNotification(message.data["title"]!!, message.data["message"]!!)
		} else {
			print("Data is null")
		}

	}

	fun generateNotification(title: String, message: String) {
		val intent = Intent(this, ShoppingActivity::class.java)
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //It will clear all the existed activities and put this on the top

		val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)  //FLAG_ONE_SHOT means, we will use this pendingIntent once

		var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL_ID)
			.setSmallIcon(R.drawable.baseline_android_24)
			.setAutoCancel(true)
			.setOnlyAlertOnce(true)
			.setContentIntent(pendingIntent)

		builder = builder.setContent(getRemoteView(title, message))

		val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val notificationChannel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
			notificationManager.createNotificationChannel(notificationChannel)
		}

		notificationManager.notify(Random.nextInt(), builder.build())

	}

	private fun getRemoteView(title: String, message: String): RemoteViews {
		val remoteView = RemoteViews("com.example.finalprojectpractice", R.layout.notification)
		remoteView.setTextViewText(R.id.tv_notification_title, title)
		remoteView.setTextViewText(R.id.tv_notification_message, message)
		remoteView.setImageViewResource(R.id.iv_notification_logo, R.drawable.app_logo)

		return remoteView
	}

}