package com.aware.plugin.SmartVolume;

import android.content.Intent;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.aware.Accelerometer;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Light;
import com.aware.Proximity;
import com.aware.Rotation;
import com.aware.providers.Accelerometer_Provider;
import com.aware.utils.Aware_Plugin;

public class Plugin extends Aware_Plugin{
    private MediaPlayer mediaPlayer;
    private Button controlPlayerButton;
    private AudioManager audio;
    public static final int UPDATE_THE_VOLUME = 0;
    //control the volume
    public VolumeHandler volumeHandler;
    //Accelerometer
    public static double acc_x;
    public static double acc_y;
    public static double acc_z;
    public static long timestamp_acc;
    //rotation
    public static double rotation_x;
    public static double rotation_y;
    public static double rotation_z;
    public static double rotation_cos;
    public static long timestamp_rot;
    //light
    public static double light;
    public static long timestamp_lig;
    //proximity
    public static double proximity;
    public static long timestamp_prx;
    //microphone
    public static double microphone;
    //volume
    public static int volume;
    public static long timestamp_vol;

    //Handler to update the volume
    private final class VolumeHandler extends Handler {

        public VolumeHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_THE_VOLUME: {

                    Log.d("VOLUME","SET VOLUME to " + msg.arg1);

//                    Toast t = Toast.makeText(context, (String) msg.obj,
//                            Toast.LENGTH_SHORT);
//                    t.show();
                }
                default:
                    break;
            }
        }
    }
    //Listener for tougle button start/stop play
    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        Log.d("PLAYER", "Button toggled");
        if (on) {
            // Start play
            mediaPlayer.start();

            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LIGHT, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, true);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_PROXIMITY, true);

            IntentFilter filter = new IntentFilter();
            filter.addAction(Accelerometer.ACTION_AWARE_ACCELEROMETER);
            filter.addAction(Light.ACTION_AWARE_LIGHT);
            filter.addAction(Rotation.ACTION_AWARE_ROTATION);
            filter.addAction(Proximity.ACTION_AWARE_PROXIMITY);
            registerReceiver(contextBR, filter);


            sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
        } else {
            // Stop play
            mediaPlayer.stop();
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LIGHT, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ROTATION, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_PROXIMITY, false);


            if(contextBR != null)
                unregisterReceiver(contextBR);
            sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
        }
    }

    private static ContextReceiver contextBR = new ContextReceiver();
    public static class ContextReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {
            //Sensors Data
            //Get the raw data
            ContentValues acc_data = (ContentValues)intent.getParcelableExtra(Accelerometer.EXTRA_DATA);
            acc_x = (double)acc_data.get("double_values_0");
            acc_y = (double)acc_data.get("double_values_1");
            acc_z = (double)acc_data.get("double_values_2");
            timestamp_acc = (long)acc_data.get("timestamp");
            //Rotation data
            ContentValues rotation_data = (ContentValues)intent.getParcelableExtra(Rotation.EXTRA_DATA);
            rotation_x = (double)rotation_data.get("double_values_0");
            rotation_y = (double)rotation_data.get("double_values_1");
            rotation_z = (double)rotation_data.get("double_values_2");
            rotation_cos = (double)rotation_data.get("double_values_3");
            timestamp_rot = (long)rotation_data.get("timestamp");
            //Light
            ContentValues light_data = (ContentValues)intent.getParcelableExtra(Light.EXTRA_DATA);
            light = (double)light_data.get("double_light_lux");
            timestamp_lig = (long)light_data.get("timestamp");
            //Proximity
            ContentValues proximity_data = (ContentValues)intent.getParcelableExtra(Proximity.EXTRA_DATA);
            proximity = (double)proximity_data.get("double_proximity");
            timestamp_prx = (long)proximity_data.get("timestamp");
            volume=audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            //Process raw data
            Log.d("DEMO", "ACC: " + acc_data.get("timestamp").toString()+ "Light: " + light_data.get("timestamp").toString());
        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Do something
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

            Log.d("PLAYER","Down " + String.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC)));
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            //Do something

            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            Log.d("PLAYER", "UP " + String.valueOf(audio.getStreamVolume(AudioManager.STREAM_MUSIC)));
            return true;
        }
        return false;



    }
    public Thread SmartVolume_thread = new Thread(){
        public void run() {
            while (Aware.getSetting(getApplicationContext(), Settings.STATUS_PLUGIN_SMARTVOLUME).equals("true")) {


            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        if( DEBUG ) Log.d(TAG, "plugin running");

        //Initialize our plugin's settings
        if( Aware.getSetting(this, Settings.STATUS_PLUGIN_SMARTVOLUME).length() == 0 ) {
            Aware.setSetting(this, Settings.STATUS_PLUGIN_SMARTVOLUME, true);
        }
        setContentView(R.layout.card);
        controlPlayerButton = (Button)findViewById(R.id.togglebutton);
        //initializing the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeHandler = new VolumeHandler(Looper.getMainLooper());
        //Activate any sensors/plugins you need here
        //...

        //Any active plugin/sensor shares its overall context using broadcasts
        CONTEXT_PRODUCER = new ContextProducer() {
            @Override
            public void onContext() {
                //Broadcast your context here
            }
        };

        //To sync data to the server, you'll need to set this variables from your ContentProvider
        //DATABASE_TABLES =
        //TABLES_FIELDS =
        //CONTEXT_URIS = new Uri[]{ }
        SmartVolume_thread.start();
        //Ask AWARE to apply your settings
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
        TAG = "Template";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if( DEBUG ) Log.d(TAG, "plugin terminated");
        Aware.setSetting(this, Settings.STATUS_PLUGIN_SMARTVOLUME, false);

        //Deactivate any sensors/plugins you activated here
        //...

        //Ask AWARE to apply your settings
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));
    }
}
