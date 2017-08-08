package com.example.zhang.matchinggame;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TimeTextView textViewTime;
    private TableLayout tableLayout;
    private ChangeSizeDialog changeSizeDialog;
    private boolean noStop = false;
    @Nullable
    private SquareButton first;
    @Nullable
    private SquareButton second;
    private CountDownTimer timer;
    private List<SquareButton> buttonList;
    private int countDown = 0;
    private boolean isStopped = false, isFinished = false;

    View.OnClickListener clickListener_iconButton = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View v) {
            if (isStopped || ((SquareButton) v).isOpened())
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
                    textViewTime.Stop();
                    isFinished = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Congratulations! All matched!");
                    builder.show();
                }
            }
        }
    };
    private Button button_resume;

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenubar, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshItem:
                noStop = true;
                ((RelativeLayout) findViewById(R.id.layout_cover)).setVisibility(View.VISIBLE);
                textViewTime.Pause();
                isStopped = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("重新开始游戏").setMessage("确认重新开始？当前进度将丢失！").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((RelativeLayout) findViewById(R.id.layout_cover)).setVisibility(View.INVISIBLE);
                        textViewTime.Stop();
                        changeSizeDialog.show(getFragmentManager(), "");
                        isStopped = false;
                        noStop = false;
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((RelativeLayout) findViewById(R.id.layout_cover)).setVisibility(View.INVISIBLE);
                        textViewTime.Resume();
                        isStopped = false;
                        noStop = false;
                    }
                }).show();
                break;
            case R.id.pause:
                if (isFinished)
                    break;
                ((RelativeLayout) findViewById(R.id.layout_cover)).setVisibility(View.VISIBLE);
                textViewTime.Pause();
                isStopped = true;
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout_pg);
        textViewTime = (TimeTextView) findViewById(R.id.textViewTime);
        textViewTime.setActivity(this);

        changeSizeDialog = new ChangeSizeDialog();
        changeSizeDialog.setOutSide(this);
        changeSizeDialog.show(getFragmentManager(), "");

        button_resume = (Button) findViewById(R.id.button_resume);
        button_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((RelativeLayout) findViewById(R.id.layout_cover)).setVisibility(View.INVISIBLE);
                        textViewTime.Resume();
                        isStopped = false;
                    }
                });

            }
        });
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.w("State", "onStop");
        if (noStop)
            return;
        isStopped = true;
        ((RelativeLayout) findViewById(R.id.layout_cover)).setVisibility(View.VISIBLE);
        textViewTime.Pause();
    }

    public void ChangeSize() {
        isFinished = false;
        tableLayout.removeAllViewsInLayout();

        buttonList = new ArrayList<>();

        int size = changeSizeDialog.getSize();

        IconAssigner.getInstance().setNum(size * size).ResetAll();

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
        textViewTime.Start();
    }
}
