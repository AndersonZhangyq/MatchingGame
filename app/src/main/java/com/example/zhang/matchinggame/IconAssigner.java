package com.example.zhang.matchinggame;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by zhang on 2017-08-06.
 */

public class IconAssigner {
    private static class IconAssignerHolder {
        private static final IconAssigner INSTANCE = new IconAssigner();
    }

    private IconAssigner() {
    }

    @NonNull
    static public IconAssigner getInstance() {
        return IconAssignerHolder.INSTANCE;
    }


    private int num;

    @NonNull
    public IconAssigner setNum(int num) {
        this.num = num;
        return this;
    }

    private Random random;
    private final String[] character_ = new String[]{
            "\uD83C\uDF40", "\uD83C\uDF40",
            "\uD83C\uDF57", "\uD83C\uDF57",
            "\uD83C\uDF61", "\uD83C\uDF61",
            "\uD83C\uDF81", "\uD83C\uDF81",
            "\uD83C\uDF8F", "\uD83C\uDF8F",
            "\u231B", "\u231B",
            "\uD83D\uDD13", "\uD83D\uDD13",
            "\uD83C\uDFA7", "\uD83C\uDFA7",
            "\uD83C\uDFAF", "\uD83C\uDFAF",/**/
            "\uD83C\uDF1C", "\uD83C\uDF1C",
            "\uD83D\uDEB2", "\uD83D\uDEB2",
            "\u26A1", "\u26A1",
            "\uD83C\uDFC7", "\uD83C\uDFC7",/**/
            "\uD83C\uDF33", "\uD83C\uDF33",
            "\uD83D\uDC2C", "\uD83D\uDC2C",
            "\uD83D\uDC8E", "\uD83D\uDC8E",
            "\uD83C\uDFBC", "\uD83C\uDFBC",
            "\uD83C\uDF3D", "\uD83C\uDF3D"};
    private List<String> character;
    private List<String> tmp;

    public void ResetAll() {
        character = new ArrayList<>(Arrays.asList(character_));
        tmp = character.subList(0, num);
        random = new Random();
    }

    public String getIcon() {
        int index = random.nextInt(tmp.size());
        String ret = tmp.get(index);
        Log.w("IconIndex: ", ret);
        tmp.remove(index);
        return ret;
    }
}
