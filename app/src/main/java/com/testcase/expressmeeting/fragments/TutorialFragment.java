package com.testcase.expressmeeting.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testcase.expressmeeting.R;

public class TutorialFragment extends Fragment {

    public static TutorialFragment newInstance(int section) {
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("currentPage", section);
        tutorialFragment.setArguments(bundle);

        return tutorialFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = null;
        final int fragment = getArguments().getInt("currentPage");

        switch (fragment) {
            case 0:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial_0, container, false);
                break;
            case 1:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial_1, container, false);
                break;
            case 2:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial_2, container, false);
                break;
            case 3:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tutorial_3, container, false);
                break;
        }
        return rootView;
    }
}
