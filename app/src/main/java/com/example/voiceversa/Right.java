package com.example.voiceversa;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Right extends Fragment {
    public Right(){}
    Spinner source,target;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right,
                container, false);
        source=view.findViewById(R.id.source_spinner);
        target=view.findViewById(R.id.target_spinner);
        String[] source_list = {"English","Hindi","Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Mongolian", "Slovenian", "Swedish", "Tamil", "Turkish", "Chinese"};
        String[] target_list = {"English","Hindi","Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Mongolian", "Slovenian", "Swedish", "Tamil", "Turkish", "Chinese"};
        ArrayAdapter<String> source_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, source_list);
        ArrayAdapter<String> target_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, target_list);

        source_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        target_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        source.setAdapter(source_adapter);
        target.setAdapter(target_adapter);
        return view;
    }
}