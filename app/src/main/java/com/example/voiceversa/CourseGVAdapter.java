package com.example.voiceversa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.voiceversa.CourseModel;

import java.util.ArrayList;

public class CourseGVAdapter extends ArrayAdapter<CourseModel> {

    private ArrayList<Integer> selectedPositions = new ArrayList<>();

    public CourseGVAdapter(@NonNull Context context, ArrayList<CourseModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.model_download_item, parent, false);
        }

        CourseModel courseModel = getItem(position);

        TextView courseTV = listItemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listItemView.findViewById(R.id.idIVcourse);
        TextView verTV = listItemView.findViewById(R.id.idTVVer);
        ImageView tickIV = listItemView.findViewById(R.id.tick);

        verTV.setText(courseModel.getVer());
        courseTV.setText(courseModel.getCourse_name());
        courseIV.setImageResource(courseModel.getImgid());

        // Show tick if this position is selected
        if (selectedPositions.contains(position)) {
            tickIV.setVisibility(View.VISIBLE);
        } else {
            tickIV.setVisibility(View.GONE);
        }

        listItemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                // Deselect if already selected
                selectedPositions.remove(Integer.valueOf(position));
                tickIV.setVisibility(View.GONE);
            } else {
                // Select only if less than 2 are selected
                if (selectedPositions.size() < 2) {
                    selectedPositions.add(position);
                    tickIV.setVisibility(View.VISIBLE);
                } else {
                    // Optionally, show a message indicating only 2 selections are allowed
                    Toast.makeText(getContext(), "You can only select up to 2 items", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return listItemView;
    }
}
