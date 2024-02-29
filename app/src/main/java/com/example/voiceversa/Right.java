package com.example.voiceversa;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class Right extends Fragment {
    public Right(){}

    Spinner source, target;
    EditText source_txt;
    TextView target_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        TransitionInflater tinflater = TransitionInflater.from(requireContext());
        setExitTransition(tinflater.inflateTransition(R.transition.slide_right));
        setEnterTransition(tinflater.inflateTransition(R.transition.slide_right));

        source = view.findViewById(R.id.source_spinner);
        target = view.findViewById(R.id.target_spinner);
        source_txt = view.findViewById(R.id.source_txt);
        target_txt = view.findViewById(R.id.target_txt);

        String[] source_list = {"English", "Hindi", "Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Mongolian", "Slovenian", "Swedish", "Tamil", "Turkish", "Chinese"};
        String[] target_list = {"English", "Hindi", "Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Mongolian", "Slovenian", "Swedish", "Tamil", "Turkish", "Chinese"};

        ArrayAdapter<String> source_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, source_list);
        ArrayAdapter<String> target_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, target_list);

        source_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        target_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        source.setAdapter(source_adapter);
        target.setAdapter(target_adapter);

        source_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update target_txt when text changes
                target_txt.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        return view;
    }
}
