package com.example.voiceversa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Left extends Fragment {
    public Left(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left,
                container, false);
        TransitionInflater tinflater = TransitionInflater.from(requireContext());
        setExitTransition(tinflater.inflateTransition(R.transition.slide_left));
        setEnterTransition(tinflater.inflateTransition(R.transition.slide_left));

        return view;
    }
}