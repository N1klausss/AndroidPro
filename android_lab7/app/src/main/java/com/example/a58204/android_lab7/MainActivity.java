package com.example.a58204.android_lab7;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    final String [] firstLetter = new String[] {"E","A","D","K","W","M","F","M","L","B" };
    final String [] goodsName = new String[] {"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis",
            "waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
    final String [] price = {"¥ 5.00","¥ 59.00","¥ 79.00 ","¥ 2399.00","¥ 179.00","¥ 14.90","¥ 132.59","¥ 141.43",
            "¥ 139.43","¥ 28.90"};
    final String [] type = {"作者","产地","产地","版本","重量","产地","重量","重量","重量","重量"};
    final String [] info = {"Johanna Basford","德国","澳大利亚","8GB","2Kg","英国","300g","118g","249g","640g"};

    List<Map<String,Object>> data1 = new ArrayList<>();
    SimpleAdapter simpleAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Map<String,Object>> data = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("firstLetter",firstLetter[i]);
            temp.put("name",goodsName[i]);
            temp.put("price",price[i]);
            temp.put("type",type[i]);
            temp.put("info",info[i]);
            data.add(temp);
        }
        final CommonAdapter<Map<String,Object>> commonAdapter = new CommonAdapter<Map<String,Object>>(this,R.layout.recycle_item, data)
        {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> item) {
                TextView firstLetter = holder.getView(R.id.firstLetter);
                firstLetter.setText(item.get("firstLetter").toString());
                TextView name = holder.getView(R.id.name);
                name.setText(item.get("name").toString());
            }

        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("firstLetter",commonAdapter.getList().get(position).get("firstLetter").toString());
                bundle.putString("name",commonAdapter.getList().get(position).get("name").toString());
                bundle.putString("price",commonAdapter.getList().get(position).get("price").toString());
                bundle.putString("type",commonAdapter.getList().get(position).get("type").toString());
                bundle.putString("info",commonAdapter.getList().get(position).get("info").toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }

            @Override
            public void onLongClick(int position) {
                commonAdapter.removeItem(position);
            }
        });


        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());

         final ListView mListView = (ListView) findViewById(R.id.shop_cart);
        Map<String,Object> temp = new LinkedHashMap<>();
        temp.put("firstLetter","*");
        temp.put("name","购物车");
        temp.put("price","价格");
        data1.add(temp);
        simpleAdapter = new SimpleAdapter(this,data1,R.layout.list_item,new String[] {"firstLetter","name","price"},new int [] {R.id.firstLetter,R.id.name,R.id.price});
        mListView.setAdapter(simpleAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                final int index = position;
                if(index>0) {
                    alertDialog.setTitle("移除商品")
                            .setMessage("从购物车移除" + data1.get(position).get("name").toString() + "?")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data1.remove(index);
                                    simpleAdapter.notifyDataSetChanged();
                                }
                            }).create().show();
                }

                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("firstLetter", data1.get(position).get("firstLetter").toString());
                    bundle.putString("name", data1.get(position).get("name").toString());
                    bundle.putString("price", data1.get(position).get("price").toString());
                    bundle.putString("type", data1.get(position).get("type").toString());
                    bundle.putString("info", data1.get(position).get("info").toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
        }
        });




        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fab.getTag().equals("0"))
                {
                    mRecyclerView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    fab.setTag("1");
                    fab.setImageResource(R.mipmap.mainpage);
                }else{
                    mListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    fab.setTag("0");
                    fab.setImageResource(R.mipmap.shoplist);
                }
            }
        });


        Random random = new Random();
        int index = random.nextInt(10);



        Intent intentBroadcast = new Intent("MyStaticFilter");
        Bundle bundle_static = new Bundle();
        bundle_static.putString("firstLetter",firstLetter[index]);
        bundle_static.putString("name",goodsName[index]);
        bundle_static.putString("price",price[index]);
        bundle_static.putString("type",type[index]);
        bundle_static.putString("info",info[index]);
        intentBroadcast.putExtras(bundle_static);
        sendBroadcast(intentBroadcast);


        EventBus.getDefault().register(this);










    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event){
        Map<String, Object> temp = new LinkedHashMap<>();
        for(int i = 0; i < 10; i++)
        {
            if(goodsName[i].equals(event.getMsg())) {
                temp.put("firstLetter", firstLetter[i]);
                temp.put("name", goodsName[i]);
                temp.put("price", price[i]);
                temp.put("type", type[i]);
                temp.put("info", info[i]);
                data1.add(temp);
                break;
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.id_recycle);
        ListView mListView = (ListView)findViewById(R.id.shop_cart);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        mRecyclerView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        fab.setImageResource(R.mipmap.mainpage);
        fab.setTag("1");
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            switch (resultCode)
            {
                case 0 :

                    break;
                case 1:
                    Bundle extras = data.getExtras();
                    if(extras != null) {
                        String name = extras.getString("name");
                        String firstLetter = extras.getString("firstLetter");
                        String price = extras.getString("price");
                        String type = extras.getString("type");
                        String info = extras.getString("info");
                        Map<String, Object> temp = new LinkedHashMap<>();
                        temp.put("firstLetter", firstLetter);
                        temp.put("name", name);
                        temp.put("price", price);
                        temp.put("type",type);
                        temp.put("info",info);
                        data1.add(temp);
                        simpleAdapter.notifyDataSetChanged();
                        break;
                    }
            }
        }
    }*/
}

