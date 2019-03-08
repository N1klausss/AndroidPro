package com.example.lab6;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    public static boolean hasPermission;

    private ImageView img;
    private TextView state, start, length;
    private SeekBar seekBar;
    private Button play_pause, stop, quit;

    private MusicService musicService;
    private ServiceConnection serviceConnection;
    private IBinder binder;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private ObjectAnimator animator;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 123:
                    seekBar.setProgress(musicService.player.getCurrentPosition());
                    start.setText(time.format(musicService.player.getCurrentPosition()));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(MainActivity.this);

        img = (ImageView)findViewById(R.id.img);
        state = (TextView)findViewById(R.id.state);
        start = (TextView)findViewById(R.id.start);
        length = (TextView)findViewById(R.id.length);
        seekBar = (SeekBar)findViewById(R.id.progress);
        play_pause = (Button)findViewById(R.id.play_pause);
        stop = (Button)findViewById(R.id.stop);
        quit = (Button)findViewById(R.id.quit);

        animator = ObjectAnimator.ofFloat(img, "rotation", 0f, 360.0f);
        animator.setDuration(12000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicService = ((MusicService.MyBinder)(service)).getService();
                binder = service;
                seekBar.setMax(musicService.player.getDuration());
                length.setText(time.format(musicService.player.getDuration()));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceConnection = null;
            }
        };

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(100);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (hasPermission == true) {
                                handler.obtainMessage(123).sendToTarget();
                            }
                        }
                    }
                };
                try {
                    int code = 101;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    binder.transact(code, data, reply, 0);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }

                if (state.getText().toString().equals("") || state.getText().toString().equals("STOPPED")) {
                    animator.start();
                }
                else if (state.getText().toString().equals("PAUSED")) {
                    animator.resume();
                }
                else {
                    animator.pause();
                }

                if (play_pause.getText().toString().equals("PLAY")) {
                    state.setText("PLAYING");
                    play_pause.setText("PAUSE");
                }
                else if (play_pause.getText().toString().equals("PAUSE")) {
                    state.setText("PAUSED");
                    play_pause.setText("PLAY");
                }

                thread.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission == true) {
                    try {
                        int code = 102;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        binder.transact(code, data, reply, 0);
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    animator.end();
                    state.setText("STOPPED");
                    play_pause.setText("PLAY");
                    seekBar.setProgress(0);
                    start.setText("00:00");
                }
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
                serviceConnection = null;
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else {
                hasPermission = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            System.exit(0);
        }
        return;
    }
}
