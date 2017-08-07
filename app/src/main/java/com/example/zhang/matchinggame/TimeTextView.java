package com.example.zhang.matchinggame;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhang on 2017-08-07.
 */

public class TimeTextView extends android.support.v7.widget.AppCompatTextView {

    long baseTime;
    Timer updateTime = new Timer();
    private TimeTextView _this;
    private MainActivity activity;
    SimpleDateFormat simpleDateFormat;
    {
        simpleDateFormat = new SimpleDateFormat("HH : mm : ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _this = this;
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _this = this;
    }

    public TimeTextView(Context context) {
        super(context);
        _this = this;
    }

    private void Reset() {
        updateTime.cancel();
        updateTime.purge();
        updateTime = new Timer();
        baseTime = new Date().getTime();
    }

    public void Start() {
        Reset();
        updateTime.schedule(new TimerTask() {
            @Override
            public void run() {
                _this.UpdateUI(new Date().getTime());
            }
        }, 0,1000);
    }

    public void UpdateUI(final long cur) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long interval = cur - baseTime;
                setText(simpleDateFormat.format(interval));
            }
        });
    }

    public void Stop() {
        updateTime.cancel();
        updateTime.purge();
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}
