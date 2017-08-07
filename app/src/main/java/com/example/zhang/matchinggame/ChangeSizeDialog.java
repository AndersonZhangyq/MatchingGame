package com.example.zhang.matchinggame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Created by zhang on 2017-08-05.
 */

public class ChangeSizeDialog extends DialogFragment {

    private int size = -1;

    private MainActivity outSide;

    public void setOutSide(MainActivity outSide) {
        this.outSide = outSide;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_change_size, null);
        RadioGroup size_group = (RadioGroup) view.findViewById(R.id.sizeGroup);

        size_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton4:
                        size = 4;
                        break;
                    case R.id.radioButton6:
                        size = 6;
                        break;
                }
                if (size != -1) {
                    dismiss();
                    outSide.ChangeSize();
                }
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public int getSize() {
        return size;
    }
}
