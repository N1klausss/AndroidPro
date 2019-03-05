package com.example.a58204.android_lab6;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 58204 on 2017/10/20.
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String firstLetter = bundle.getString("firstLetter");
        final String name = bundle.getString("name");
        final String price = bundle.getString("price");
        final String type = bundle.getString("type");
        final String info = bundle.getString("info");

        ImageButton mback = (ImageButton) findViewById(R.id.backbutt);
        mback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        final ImageButton mlike = (ImageButton) findViewById(R.id.like);
        mlike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String string = (String) mlike.getTag();
                if(string.equals("0"))
                {
                    mlike.setBackground(getResources().getDrawable(R.drawable.full_star));
                    mlike.setTag("1");
                }else{
                    mlike.setBackground(getResources().getDrawable(R.drawable.empty_star));
                    mlike.setTag("0");
                }
            }
        });

        ImageView goods_img = (ImageView) findViewById(R.id.goodimg);
        switch (name)
        {
            case "Enchated Forest" :
                goods_img.setBackgroundResource(R.mipmap.enchatedforest);
                break;
            case "Arla Milk" :
                goods_img.setBackgroundResource(R.mipmap.arla);
                break;
            case "Devondale Milk" :
                goods_img.setBackgroundResource(R.mipmap.devondale);
                break;
            case "Kindle Oasis" :
                goods_img.setBackgroundResource(R.mipmap.kindle);
                break;
            case "waitrose 早餐麦片" :
                goods_img.setBackgroundResource(R.mipmap.waitrose);
                break;
            case "Mcvitie's 饼干" :
                goods_img.setBackgroundResource(R.mipmap.mcvitie);
                break;
            case "Ferrero Rocher" :
                goods_img.setBackgroundResource(R.mipmap.ferrero);
                break;
            case "Maltesers" :
                goods_img.setBackgroundResource(R.mipmap.maltesers);
                break;
            case "Lindt" :
                goods_img.setBackgroundResource(R.mipmap.lindt);
                break;
            case "Borggreve" :
                goods_img.setBackgroundResource(R.mipmap.borggreve);
                break;

        }

        TextView goods_name = (TextView) findViewById(R.id.name);
        goods_name.setText(name);

        TextView goods_price = (TextView) findViewById(R.id.price);
        goods_price.setText(price);

        TextView goods_type = (TextView) findViewById(R.id.type);
        goods_type.setText(type);

        TextView goods_info= (TextView) findViewById(R.id.info);
        goods_info.setText(info);

        ImageButton add_goods = (ImageButton) findViewById(R.id.add);

        add_goods.setOnClickListener(new View.OnClickListener()
        {
            /*
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("name",name);
                bundle1.putString("firstLetter",firstLetter);
                bundle1.putString("price",price);
                bundle1.putString("type",type);
                bundle1.putString("info",info);
                Intent intent1 = new Intent();
                intent1.putExtras(bundle1);
                setResult(1,intent1);
                Toast.makeText(DetailActivity.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();
            }*/

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("firstLetter",firstLetter);
                bundle.putString("price",price);
                bundle.putString("type",type);
                bundle.putString("info",info);
                Intent intentBroadcast = new Intent("MyDynamicFilter");
                intentBroadcast.putExtras(bundle);
                DynamicReceiver dynamicReceiver = new DynamicReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("MyDynamicFilter");
                registerReceiver(dynamicReceiver,intentFilter);
                sendBroadcast(intentBroadcast);
                EventBus.getDefault().post(new MessageEvent(name));
                Toast.makeText(DetailActivity.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();
            }
        });
























    }
}
