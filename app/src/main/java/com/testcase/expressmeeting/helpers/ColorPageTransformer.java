package com.testcase.expressmeeting.helpers;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ColorPageTransformer implements ViewPager.PageTransformer {

    private int position;
    private int[] reds;
    private int[] greens;
    private int[] blues;
    private ViewPager mPager;

    public ColorPageTransformer(ViewPager mPager) {
        position = 0;
        reds = new int[]{245, 10, 0, 69};
        greens = new int[]{124, 126, 151, 94};
        blues = new int[]{0, 7, 167, 222};
        this.mPager = mPager;
    }

    @Override
    public void transformPage(View view, float v) {
        position = mPager.getCurrentItem();
        if (v <= -1) {
            if (position > 0) {
                view.setTag(position - 1);
                view.setBackgroundColor(Color.rgb(reds[position - 1], greens[position - 1], blues[position - 1]));
            }
        } else if (v < 0) {
            if ((view.getTag() == null) && (position > 0)) {
                view.setTag(position - 1);
                view.setBackgroundColor(Color.rgb(reds[position - 1], greens[position - 1], blues[position - 1]));
            } else {
                int i = (Integer) view.getTag();

                float diffV = 1 - ((-1 - v) * -1);
                int diffRed = (int) ((reds[i + 1] - reds[i]) * diffV);
                int diffGreen = (int) ((greens[i + 1] - greens[i]) * diffV);
                int diffBlue = (int) ((blues[i + 1] - blues[i]) * diffV);

                view.setBackgroundColor(Color.rgb(reds[i] + diffRed, greens[i] + diffGreen, blues[i] + diffBlue));
            }
        } else if (v == 0) {
            view.setTag(position);
            view.setBackgroundColor(Color.rgb(reds[position], greens[position], blues[position]));
        } else if (v < 1) {
            if ((view.getTag() == null) && (position < mPager.getChildCount())) {
                view.setTag(position + 1);
                view.setBackgroundColor(Color.rgb(reds[position + 1], greens[position + 1], blues[position + 1]));
            } else {
                int i = (Integer) view.getTag();

                float diffV = 1 - (1 - v);
                int diffRed = (int) ((reds[i - 1] - reds[i]) * diffV);
                int diffGreen = (int) ((greens[i - 1] - greens[i]) * diffV);
                int diffBlue = (int) ((blues[i - 1] - blues[i]) * diffV);

                view.setBackgroundColor(Color.rgb(reds[i] + diffRed, greens[i] + diffGreen, blues[i] + diffBlue));
            }
        } else if (v >= 1) {
            if (position < mPager.getChildCount()) {
                view.setTag(position + 1);
                view.setBackgroundColor(Color.rgb(reds[position + 1], greens[position + 1], blues[position + 1]));
            }
        }
    }
}
