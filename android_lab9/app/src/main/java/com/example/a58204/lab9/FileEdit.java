package com.example.a58204.lab9;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 58204 on 2017/12/1.
 */

public class FileEdit extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);

        final EditText file_name = (EditText) findViewById(R.id.file_name);
        final EditText file_content = (EditText) findViewById(R.id.file_content);
        Button save = (Button) findViewById(R.id.save);
        Button load = (Button) findViewById(R.id.load);
        Button clear = (Button) findViewById(R.id.clear1);
        Button delete = (Button) findViewById(R.id.delete);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FILE_NAME = file_name.getText().toString();
                String FILE_CONTENT = file_content.getText().toString();
                try(FileOutputStream fileOutputStream = openFileOutput(FILE_NAME,MODE_PRIVATE)){
                    fileOutputStream.write(FILE_CONTENT.getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(FileEdit.this,"Successfully saved file.",Toast.LENGTH_SHORT).show();
                }
                catch (IOException e)
                {
                    Toast.makeText(FileEdit.this,"Fail to save file.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FILE_NAME = file_name.getText().toString();
                try(FileInputStream fileInputStream = openFileInput(FILE_NAME)){
                    byte[] content = new byte[fileInputStream.available()];
                    while(fileInputStream.read(content) != -1)
                    {
                        String text = new String(content);
                        file_content.setText(text);
                        Toast.makeText(FileEdit.this,"Successfully loaded file.",Toast.LENGTH_SHORT).show();
                    }
                    fileInputStream.close();
                }
                catch (IOException e)
                {
                    Toast.makeText(FileEdit.this,"Fail to load file.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_content.setText("");
                file_name.setText("");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FILE_NAME = file_name.getText().toString();
                if(deleteFile(FILE_NAME)) {
                    Toast.makeText(FileEdit.this,"Successfully deleted file.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FileEdit.this,"Fail to delete file.",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
