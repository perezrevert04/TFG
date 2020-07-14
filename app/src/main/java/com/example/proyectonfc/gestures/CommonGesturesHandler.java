package com.example.proyectonfc.gestures;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class CommonGesturesHandler {

    private float x1, x2;
    private static final int MIN_DISTANCE = 200;
    private static final int REQ_CODE = 1006;

    private Activity context;

    public CommonGesturesHandler(Activity context) {
        this.context = context;
    }

    public void onTouchEvent(Activity activity, MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        Log.d("AppLog", "Left to Right swipe [Previous]");
                        activity.onBackPressed();
                    }

                    // Right to left swipe action
                    else {
                        Log.d("AppLog", "Right to Left swipe [Command voice]");

                        openCommandVoice();
                    }

                }

                break;
        }
    }

    public void onTouchEvent(Activity activity, MotionEvent event, boolean onBack) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        Log.d("AppLog", "Left to Right swipe [Previous]");
                        if (onBack) activity.onBackPressed();
                    }

                    // Right to left swipe action
                    else {
                        Log.d("AppLog", "Right to Left swipe [Command voice]");
                        openCommandVoice();
                    }

                }

                break;
        }
    }

    private void openCommandVoice() {
        Intent intent = new Intent(context, CommandVoiceActivity.class);
        context.startActivity(intent);
    }
}
