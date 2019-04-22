package com.example.speedlimitretrofit.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.example.speedlimitretrofit.R;

import java.util.Locale;

public class Notifications extends Activity {

    private TextToSpeech mTTS;
    private TextView maxSpeedTextView;
    private TextView currentSpeedTextView;
    private Switch vibrationSwitch;
    private Switch audioSwitch;
    //private int delay;
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // set the user interface layout for this activity
        setContentView(R.layout.activity_notifications);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("notifications error", "Language not supported");
                    }
                }
                else {
                    Log.e("notifications error", "Initialization failed");
                }
            }
        });
    }

    protected void vibrate() {
        vibrationSwitch = getVibrationSwitch();
        if (vibrationSwitch.isChecked()) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(750);
        }
    }

    protected void textToSpeech(String textWarning) {
        audioSwitch = getAudioSwitch();
        if (audioSwitch.isChecked()) {
            mTTS.speak(textWarning, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    // If the person goes over 9mph. It will vibrate and send a voice notification.
    protected void warning() {
        MainActivity data = new MainActivity();
        maxSpeedTextView = data.getTextViewMaxSpeed();
        currentSpeedTextView = data.getTextViewCurrentSpeed();
        int maxSpeed;
        int currentSpeed;
        try {
            maxSpeed = Integer.parseInt(maxSpeedTextView.getText().toString());
            currentSpeed = Integer.parseInt(currentSpeedTextView.getText().toString());
            if (currentSpeed >= maxSpeed + 10) {
                textToSpeech("Slow down");
                vibrate();
            }
        }
        catch (Exception e)
        {
            Log.e("notifications warning", "Error speed");
        }
    }

    protected Switch getAudioSwitch()
    {
        return findViewById(R.id.audioSwitch);
    }

    protected Switch getVibrationSwitch()
    {
        return findViewById(R.id.vibrationSwitch);
    }
}
