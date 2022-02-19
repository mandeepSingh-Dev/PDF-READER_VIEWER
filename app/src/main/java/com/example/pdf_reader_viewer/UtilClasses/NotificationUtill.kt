package com.example.pdf_reader_viewer.UtilClasses

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pdf_reader_viewer.R

class NotificationUtill(activity: Activity) {

    var activity = activity
    var channelId = "1"
    var notificationbuilder:NotificationCompat.Builder?=null

    @SuppressLint("RemoteViewLayout")
    fun notiofiact(uri: Uri, pdftitlee:String){

        var notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationManagerCompat = NotificationManagerCompat.from(activity.applicationContext)

        Log.d("ugwef3",uri.toString())
        var intent = Intent(/*(activity.applicationContext,PdfView_Activity::class.java*/)
        intent.putExtra(Intent.EXTRA_STREAM,uri)
        intent.setAction(Intent.ACTION_VIEW)
        intent.setData(uri)

        var pendingIntent = PendingIntent.getActivity(activity.applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
         var pendingAction = NotificationCompat.Action.Builder(R.drawable.ic_item_pdf_icon,"view",pendingIntent).build()


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            var channle = NotificationChannel(channelId,"NotiFoti",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channle)

          //  var remoteView = RemoteViews(activity.applicationContext.packageName,R.layout.custom_notifiaction)
           //  remoteView.setTextViewText(R.id.detials,pdftitlee)


            notificationbuilder = NotificationCompat.Builder(activity.applicationContext, channelId)
                .setSmallIcon(R.drawable.ic__422509_acorbat_logo_pdf_file_icon)
                .setContentTitle("file downloaded")
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setContentText(pdftitlee)
            //builder.setOngoing(true);
               .setPriority(NotificationCompat.PRIORITY_HIGH)

                /*.setContentTitle("File Downloaded")
                .setContentText(pdftitlee)*/
              //  .setContentIntent(pendingIntent)
                //.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .addAction(pendingAction)
              //  .(remoteView)
               // .setColor(Color.BLUE)
                //.setContent(remoteView)

                //.set
              //  .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
               // .setContentIntent(pendingIntent)
            var notification = notificationbuilder?.build()
            notificationManagerCompat.notify(1234,notification!!)
        }

    }

}