package com.example.zhang.matchinggame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhang on 2017-08-06.
 */

public class SquareButton extends Button{

    private int size;

    private boolean matched = false;
    private boolean opened = false;
    private final Drawable hide = getResources().getDrawable(R.color.iconButton,null);

    public boolean isOpened() {
        return opened;
    }

    public boolean isNotMatched() {
        return !matched;
    }

    public void setMatched() {
        this.matched = true;
    }

    public SquareButton(Context context,int size) {
        super(context);
        this.size = size;
        setText(IconAssigner.getInstance().getIcon());
        setTextSize(30 * (6 / size));
        setForeground(hide);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

    /**
     * Set the layout parameters associated with this view. These supply
     * parameters to the <i>parent</i> of this view specifying how it should be
     * arranged. There are many subclasses of ViewGroup.LayoutParams, and these
     * correspond to the different subclasses of ViewGroup that are responsible
     * for arranging their children.
     *
     * @param params The layout parameters for this view, cannot be null
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {

        TableRow.LayoutParams buttonLayout = new TableRow.LayoutParams();
        buttonLayout.weight = 1.0f / size;
        buttonLayout.setMargins(10,10,10,10);
        super.setLayoutParams(buttonLayout);
    }

    public void changeState(){
        if (matched) return;
        if (opened){
            setForeground(hide);
            opened = false;
        }else{
            setForeground(null);
            opened = true;
        }
    }
}
