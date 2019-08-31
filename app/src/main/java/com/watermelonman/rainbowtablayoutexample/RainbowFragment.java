package com.watermelonman.rainbowtablayoutexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RainbowFragment extends Fragment {
    public static final String ARG_COLOR = "ARG_COLOR";

    private int mColor;

    public static RainbowFragment newInstance(int color) {
        Bundle args = new Bundle();
        args.putInt(ARG_COLOR, color);
        RainbowFragment fragment = new RainbowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColor = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText("Color " + mColor);
        view.setBackgroundColor(mColor);
        return view;
    }
}
