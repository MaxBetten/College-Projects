package com.mycompany.vitamiotest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.BitmapFactory;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.CenterLayout;
import io.vov.vitamio.widget.VideoView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private String path;
    private VideoView mVideoView;
    private GestureDetector mGestureDetector;
    private GestureDetector gDetect;
    KeyEvent event;
    int keycode;
    ImageView imageView;
    //the Sensor Manager
    private SensorManager sManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_main);

        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // RelativeLayout coverLayout = (RelativeLayout) inflater.inflate(R.layout.activity_main, null);
        //get the TextView from the layout file
        TextView text = (TextView) findViewById(R.id.tv);
        //text.setText("Hello");
        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        path = "http://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        //path = "http://mojtube.com@200.76.77.237/LIVE/H01/CANAL516/PROFILE03.m3u8?";
        //path = "http://tv.life.ru/lifetv/360p/index.m3u8";
        mVideoView.setVideoPath(path);
        //mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
       // mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
       /* {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });*/

    }
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {

            Log.v(TAG, "Hello");
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            long currentPosition = mVideoView.getCurrentPosition();
            if (Build.VERSION.SDK_INT >= 14)
                retriever.setDataSource(path, new HashMap<String, String>());
            else
                retriever.setDataSource(path);
            Bitmap bmp = retriever.getFrameAtTime(currentPosition*1000, MediaMetadataRetriever.OPTION_CLOSEST);
            Log.v(TAG, "Got here");
            AlertDialog.Builder myCaptureDialog = new AlertDialog.Builder(MainActivity.this);
            ImageView capturedImageView = new ImageView(MainActivity.this);
            capturedImageView.setImageBitmap(bmp);
            myCaptureDialog.setView(capturedImageView);
            myCaptureDialog.show();


            return true;
        }
        return super.onKeyDown(keycode, event);



    }
    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        //else it will output the Roll, Pitch and Yawn values
        /*Log.v(TAG,"Orientation X (Roll) :"+ Float.toString(event.values[2])+
                "Orientation Y (Pitch) :"+ Float.toString(event.values[1])+
                "Orientation Z (Yaw) :"+ Float.toString(event.values[0]));*/
        TextView text = (TextView) findViewById(R.id.tv);
        text.setText("X (Roll) :" + Float.toString(event.values[2])+ " Y (Pitch) :"+ Float.toString(event.values[1])+" Z (Yaw) :"+ Float.toString(event.values[0]));
        //"X (Roll) :" + Float.toString(event.values[2])+ " Y (Pitch) :"+ Float.toString(event.values[1])+
    }


}
