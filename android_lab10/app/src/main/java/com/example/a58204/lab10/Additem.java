package com.example.a58204.lab10;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 58204 on 2017/12/6.
 */

public class Additem extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        final Button add_button = (Button) findViewById(R.id.add);
        final EditText name_text = (EditText) findViewById(R.id.name);
        final EditText birth_text = (EditText) findViewById(R.id.birth);
        final EditText gift_text = (EditText) findViewById(R.id.gift);




        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_text.getText().toString();
                String birth = birth_text.getText().toString();
                String gift = gift_text.getText().toString();
                myDB my_db = new myDB(getApplicationContext());
                my_db.insert(name, birth, gift);
                finish();
            }
        });









    }
}
