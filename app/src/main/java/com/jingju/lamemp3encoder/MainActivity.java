package com.jingju.lamemp3encoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jingju.lamemp3encoder.utils.Constant;
import com.jingju.lamemp3encoder.utils.permission.ActivityPermissionDispatcher;
import com.jingju.lamemp3encoder.utils.permission.PermissionCallback;

public class MainActivity extends AppCompatActivity implements PermissionCallback {

    private static final String TAG="mp3encoder";

    // Used to load the 'audioencoder' library on application startup.

    static {
        System.loadLibrary("audioencoder");
    }

    private ActivityPermissionDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO: 2019-11-18 请求权限
                dispatcher = ActivityPermissionDispatcher.getInstance();
                dispatcher.setPermissionCallback(MainActivity.this,MainActivity.this);
                dispatcher.checkedWithStorage(MainActivity.this);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    public void encodePcmToMp3(){
        Mp3Encoder encoder = new Mp3Encoder();
        String pcmPath = "/storage/emulated/0/jingju/vocal.pcm";
        int audioChannels = 2;
        int bitRate = 128 * 1024;
        int sampleRate = 44100;
        String mp3Path = "/storage/emulated/0/jingju/vocal.mp3";
        int ret = encoder.init(pcmPath, audioChannels, bitRate, sampleRate, mp3Path);
        if(ret >= 0) {
            encoder.encode();
            encoder.destroy();
            Log.i(TAG, "Encode Mp3 Success");
        } else {
            Log.i(TAG, "Encoder Initialized Failed...");
        }
    }

    @Override
    public void onPermission(int request, int state) {
        if (request == Constant.PERMISSION_STORAGE_REQUEST_CODE) {
            switch (state) {
                case SUCCESS:
                    // TODO: 2019-11-18 开始
                    encodePcmToMp3();
                case DENIED:
                case NEVER_ASK_AGAIN:

                    break;
                case EXPLAIN:

                    break;
            }
        }
    }
}
