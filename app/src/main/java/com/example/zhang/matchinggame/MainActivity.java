package com.example.zhang.matchinggame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int size;
    private TableLayout tableLayout;
    private ChangeSizeDialog changeSizeDialog;
    @Nullable
    private SquareButton first;
    @Nullable
    private SquareButton second;
    private CountDownTimer timer;
    private List<SquareButton> buttonList;

    private int countDown = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout_pg);

        changeSizeDialog = new ChangeSizeDialog();
        changeSizeDialog.setOutSide(this);
        changeSizeDialog.show(getFragmentManager(), "");
    }

    public void ChangeSize() {
        buttonList = new ArrayList<>();

        size = changeSizeDialog.getSize();

        View.OnClickListener clickListener_iconButton = new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                if (((SquareButton) v).isOpened())
                    return;
                if (countDown == 0) {
                    if (timer != null) {
                        timer.cancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (first != null)
                                    first.changeState();
                                if (second != null)
                                    second.changeState();
                                first = second = null;
                            }
                        });
                    }
                    countDown++;
                    first = (SquareButton) v;
                    first.changeState();
                } else {
                    countDown = 0;
                    second = (SquareButton) v;
                    second.changeState();

                    // Don't match
                    if (!second.getText().equals(first.getText())) {
                        timer = new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.w("Timer", "Timer is running");
                            }

                            @Override
                            public void onFinish() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        first.changeState();
                                        second.changeState();
                                        first = second = null;
                                    }
                                });
                            }
                        }.start();
                    } else {
                        first.setMatched();
                        second.setMatched();
                        first = second = null;
                        for (SquareButton button : buttonList) {
                            if (button.isNotMatched())
                                return;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Congratulations! All matched!");
                        builder.show();
                    }
                }
            }
        };

        IconAssigner.getInstance().setNum(size * size).ResetAll();

        // Set button layout

        for (int i = 0; i < size; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < size; j++) {
                SquareButton button = new SquareButton(this, size);
                button.setOnClickListener(clickListener_iconButton);
                buttonList.add(button);
                row.addView(button);
            }
            row.setDividerPadding(10);
            tableLayout.addView(row);
        }
    }
}
