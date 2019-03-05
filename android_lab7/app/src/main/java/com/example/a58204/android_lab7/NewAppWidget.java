package com.example.a58204.android_lab7;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private String firstLetter;
    private String name;
    private String price;
    private String type;
    private String info;
    private int id;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent i = new Intent(context,DetailActivity.class);

        Bundle mBundle = new Bundle();
        mBundle.putString("firstLetter", firstLetter);
        mBundle.putString("name", name);
        mBundle.putString("price", price);
        mBundle.putString("type", type);
        mBundle.putString("info",info);
        i.putExtras(mBundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.mwidget,pendingIntent);
        ComponentName me = new ComponentName(context,NewAppWidget.class);
        appWidgetManager.updateAppWidget(me, updateViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();

        firstLetter = bundle.getString("firstLetter");
        name = bundle.getString("name");
        price = bundle.getString("price");
        type = bundle.getString("type");
        info = bundle.getString("info");
        switch (name)
        {
            case "Enchated Forest" :
                id = R.mipmap.enchatedforest;
                break;
            case "Arla Milk" :
                id = R.mipmap.arla;
                break;
            case "Devondale Milk" :
                id = R.mipmap.devondale;
                break;
            case "Kindle Oasis" :
                id = R.mipmap.kindle;
                break;
            case "waitrose 早餐麦片" :
                id = R.mipmap.waitrose;
                break;
            case "Mcvitie's 饼干" :
                id = R.mipmap.mcvitie;
                break;
            case "Ferrero Rocher" :
                id = R.mipmap.ferrero;
                break;
            case "Maltesers" :
                id = R.mipmap.maltesers;
                break;
            case "Lindt" :
                id = R.mipmap.lindt;
                break;
            case "Borggreve" :
                id = R.mipmap.borggreve;
                break;
        }
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Bundle mBundle = new Bundle();
        mBundle.putString("firstLetter", firstLetter);
        mBundle.putString("name", name);
        mBundle.putString("price", price);
        mBundle.putString("type", type);
        mBundle.putString("info",info);
        if(intent.getAction().equals("MyStaticFilter"))
        {
            Intent i = new Intent(context,DetailActivity.class);
            i.putExtras(mBundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.mwidget,pendingIntent);
            views.setImageViewResource(R.id.widget_img,id);
            views.setTextViewText(R.id.appwidget_text,name + "仅售" + price + "!");
        }

        if(intent.getAction().equals("MyDynamicFilter"))
        {
            Intent i = new Intent(context,MainActivity.class);
            i.putExtras(mBundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.mwidget,pendingIntent);
            views.setImageViewResource(R.id.widget_img,id);
            views.setTextViewText(R.id.appwidget_text,name + "已添加至购物车");
        }


        ComponentName me = new ComponentName(context,NewAppWidget.class);
        appWidgetManager.updateAppWidget(me, views);
    }

}

