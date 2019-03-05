package com.example.a58204.android_lab6;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/**
 * Created by 58204 on 2017/10/30.
 */

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "MyDynamicFilter";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DYNAMICACTION))
        {
            Bundle bundle = intent.getExtras();
            String firstLetter = bundle.getString("firstLetter");
            String name = bundle.getString("name");
            String price = bundle.getString("price");
            String type = bundle.getString("type");
            String info = bundle.getString("info");
            Notification.Builder builder = new Notification.Builder(context);

            Bitmap bm = null;
            int id = 0;
            switch (name)
            {
                case "Enchated Forest" :
                    id = R.mipmap.enchatedforest;
                    bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.enchatedforest);
                    break;
                case "Arla Milk" :
                    id = R.mipmap.arla;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.arla);
                    break;
                case "Devondale Milk" :
                    id = R.mipmap.devondale;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.devondale);
                    break;
                case "Kindle Oasis" :
                    id = R.mipmap.kindle;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.kindle);
                    break;
                case "waitrose 早餐麦片" :
                    id = R.mipmap.waitrose;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.waitrose);
                    break;
                case "Mcvitie's 饼干" :
                    id = R.mipmap.mcvitie;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.mcvitie);
                    break;
                case "Ferrero Rocher" :
                    id = R.mipmap.ferrero;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ferrero);
                    break;
                case "Maltesers" :
                    id = R.mipmap.maltesers;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.maltesers);
                    break;
                case "Lindt" :
                    id = R.mipmap.lindt;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.lindt);
                    break;
                case "Borggreve" :
                    id = R.mipmap.borggreve;
                    bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.borggreve);
                    break;

            }
            builder.setContentTitle("马上下单")
                    .setContentText(name +"已添加到购物车")
                    .setAutoCancel(true)
                    .setLargeIcon(bm)
                    .setSmallIcon(id)
                    .setTicker("您有一条新消息");

            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            Intent mIntent = new Intent(context,MainActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("firstLetter", firstLetter);
            mBundle.putString("name", name);
            mBundle.putString("price", price);
            mBundle.putString("type", type);
            mBundle.putString("info",info);
            mIntent.putExtras(mBundle);

            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(mPendingIntent);
            Notification notification = builder.build();
            manager.notify(0,notification);
        }
    }
}
