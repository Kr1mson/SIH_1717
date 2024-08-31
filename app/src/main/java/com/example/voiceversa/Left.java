package com.example.voiceversa;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


public class Left extends Fragment {
    public Left(){}
    GridView coursesGV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left,
                container, false);
        coursesGV = view.findViewById(R.id.model_container);
        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();

        courseModelArrayList.add(new CourseModel("English", R.drawable.english_script,"1.0.0.0"));
        courseModelArrayList.add(new CourseModel("Hindi", R.drawable.hindi_script,"1.0.0.0"));
        courseModelArrayList.add(new CourseModel("Gujarati", R.drawable.gujarati_script,"1.0.0.0"));
        courseModelArrayList.add(new CourseModel("Marathi", R.drawable.marathi_script,"1.0.0.0"));
        courseModelArrayList.add(new CourseModel("Tamil", R.drawable.tamil_script,"1.0.0.0"));
        courseModelArrayList.add(new CourseModel("Telugu", R.drawable.telugu_script,"1.0.0.0"));
        courseModelArrayList.add(new CourseModel("Kannada", R.drawable.kannada_script, "1.0.0.0"));

        CourseGVAdapter adapter = new CourseGVAdapter(getContext(), courseModelArrayList);
        coursesGV.setAdapter(adapter);
        TransitionInflater tinflater = TransitionInflater.from(requireContext());
        setExitTransition(tinflater.inflateTransition(R.transition.slide_left));
        setEnterTransition(tinflater.inflateTransition(R.transition.slide_left));
//
        return view;
    }
}