package com.example.zhang.matchinggame;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhang on 2017-08-07.
 */

public class TimeTextView extends android.support.v7.widget.AppCompatTextView {

    long baseTime, beforePause;
    Timer updateTime = new Timer();
    private MainActivity activity;
    private boolean islastCallPause = false;
    SimpleDateFormat simpleDateFormat;
    {
        simpleDateFormat = new SimpleDateFormat("HH : mm : ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        islastCallPause = false;
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        islastCallPause = false;
    }

    public TimeTextView(Context context) {
        super(context);
        islastCallPause = false;
    }

    private void Reset() {
        islastCallPause = false;
        if (updateTime != null)
            updateTime.cancel();
        updateTime = new Timer();
        baseTime = new Date().getTime();
        beforePause = 0;
    }

    public void Start() {
        islastCallPause = false;
        Reset();
        updateTime.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateUI(new Date().getTime());
            }
        }, 0,1000);
    }

    public void UpdateUI(final long cur) {
        islastCallPause = false;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long interval = cur - baseTime + beforePause;
                setText(simpleDateFormat.format(interval));
            }
        });
    }

    public void Pause(){
        islastCallPause = true;
        try {
            beforePause = simpleDateFormat.parse(getText().toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (updateTime != null)
            updateTime.cancel();
        updateTime = null;
    }

    public void Resume(){
        if (!islastCallPause)
            return;
        islastCallPause = false;
        updateTime = new Timer();
        baseTime = new Date().getTime();
        updateTime.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateUI(new Date().getTime());
            }
        }, 0,1000);
    }

    public void Stop() {
        islastCallPause = false;
        if (updateTime != null)
            updateTime.cancel();
        updateTime = null;
    }

    public void setActivity(MainActivity activity) {
        islastCallPause = false;
        this.activity = activity;
    }
}
