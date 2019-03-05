package com.example.a58204.lab10;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    List<Map<String,Object>> data = new ArrayList<>();
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add_content = (Button) findViewById(R.id.add_content);
        ListView mlistView = (ListView) findViewById(R.id.list);

        add_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Additem.class);
                startActivity(intent);
            }
        });
        simpleAdapter = new SimpleAdapter(this,data,R.layout.item,new String[] {"name","birth","gift"},new int [] {R.id.item_name,R.id.item_birth,R.id.item_gift});
        mlistView.setAdapter(simpleAdapter);



        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Map<String,Object> temp = new LinkedHashMap<>();
                temp = data.get(position);
                final String name = temp.get("name").toString();
                final String birth = temp.get("birth").toString();
                final String gift = temp.get("gift").toString();

                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                final View view_in = factor.inflate(R.layout.dialoglayout, null);
                final TextView dia_name = (TextView) view_in.findViewById(R.id.name);
                final EditText dia_birth = (EditText) view_in.findViewById(R.id.birth);
                final EditText dia_gift = (EditText) view_in.findViewById(R.id.gift);
                final TextView dia_number = (TextView) view_in.findViewById(R.id.number);

                dia_name.setText(name);
                dia_birth.setText(birth);
                dia_gift.setText(gift);

                //得到ContentResolver对象
                ContentResolver cr = getContentResolver();
                //取得电话本中开始一项的光标
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                //向下移动光标
                while(cursor.moveToNext())
                {
                    //取得联系人名字
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    String contact = cursor.getString(nameFieldColumnIndex);
                    //取得电话号码
                    String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

                    String PhoneNumber = null;
                    if(phone != null) {
                        while (phone.moveToNext()) {
                            PhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //格式化手机号
                            PhoneNumber = PhoneNumber.replace("-", "");
                            PhoneNumber = PhoneNumber.replace(" ", "");
                        }
                    }

                    if (contact.equals(name))
                    {
                        dia_number.setText(PhoneNumber);
                    }
                }


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);


                alertDialog.setView(view_in)
                        .setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDB my_db = new myDB(getApplicationContext());

                                String birth1 = dia_birth.getText().toString();
                                String gift1 = dia_gift.getText().toString();

                                my_db.update(name,birth1,gift1);
                                Map<String,Object> temp = new LinkedHashMap<>();
                                temp.put("name",name);
                                temp.put("birth",birth1);
                                temp.put("gift",gift1);
                                data.set(position,temp);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        }).create().show();
            }
        });

        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB my_db = new myDB(getApplicationContext());
                        Map<String,Object> temp = new LinkedHashMap<>();
                        temp = data.get(position);
                        String name = temp.get("name").toString();
                        my_db.delete(name);
                        data.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();


                return true;
            }
        });


    }








    @Override
    protected void onResume() {
        super.onResume();
        ListView mlistView = (ListView) findViewById(R.id.list);

        myDB my_db = new myDB(getApplicationContext());
        data.clear();
        data.addAll(my_db.queryAll()) ;
        simpleAdapter.notifyDataSetChanged();
        mlistView.setAdapter(simpleAdapter);

    }
}
