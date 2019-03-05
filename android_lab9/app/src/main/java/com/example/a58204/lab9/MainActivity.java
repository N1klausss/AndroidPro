package com.example.a58204.lab9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCE_NAME = "MY_PREFERENCE";
    public static int MODE = Context.MODE_PRIVATE + Context.MODE_APPEND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirm = (EditText) findViewById(R.id.confirm);
        Button okay = (Button) findViewById(R.id.okay);
        Button clear = (Button) findViewById(R.id.clear);

        Context context = MainActivity.this;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = sharedPreferences.getInt("count",0);
        final String sp_password = sharedPreferences.getString("password","");
        if(count != 0)
        {
            password.setHint("Password");
            confirm.setVisibility(View.GONE);
            confirm.setText(sharedPreferences.getString("confirm",""));
        }

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                }
                if(!password.getText().toString().equals(confirm.getText().toString()))
                {
                    Toast.makeText(MainActivity.this,"Password mismatch",Toast.LENGTH_SHORT).show();
                }
                if(password.getText().toString().equals(confirm.getText().toString()) && !password.getText().toString().equals(""))
                {
                    editor.putString("password",password.getText().toString());
                    editor.putString("confirm",confirm.getText().toString());
                    editor.putInt("count",1);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,FileEdit.class);
                    startActivity(intent);
                }
                 if(password.getText().toString().equals(sp_password) && !password.getText().toString().equals(""))
                 {
                     Intent intent = new Intent(MainActivity.this,FileEdit.class);
                     startActivity(intent);
                 }
            }
        });

        clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                password.setText("");
                confirm.setText("");
            }
        });

    }
}
