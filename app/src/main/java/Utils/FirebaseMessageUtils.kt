package Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vincent.ebook.R

class FirebaseMessageUtils : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("TAG", "@@@@ in onMessageReceived: ")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        p0.notification?.let {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel("normal","Normal",NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            val notification = NotificationCompat.Builder(this,"normal")
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setSmallIcon(R.drawable.login_surface_icon)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.book))
                .build()

            manager.notify(1,notification)
            Log.d("TAG", "@@@@ received notification title = ${it.title} ")
            Log.d("TAG", "@@@@ received notification body = ${it.body}")
        }
    }
}