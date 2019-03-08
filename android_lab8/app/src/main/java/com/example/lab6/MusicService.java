package com.example.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MusicService extends Service {
    public MediaPlayer player = new MediaPlayer();
    public MyBinder binder = new MyBinder();

    public MusicService() {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/melt.mp3";
            player.setDataSource(path);
            player.prepare();
            player.setLooping(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play_pause() {
        if (player.isPlaying()) {
            player.pause();
        }
        else {
            player.start();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            try {
                player.prepare();
                player.seekTo(0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    play_pause();
                    break;
                case 102:
                    stop();
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
}
