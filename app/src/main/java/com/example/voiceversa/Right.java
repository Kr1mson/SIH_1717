package com.example.voiceversa;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.transition.TransitionInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


import java.util.ArrayList;

import java.util.Locale;

import java.util.Objects;


public class Right extends Fragment {
    public Right(){}
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_PERMISSION_CODE = 1;
    int languageCode, fromlanguageCode, tolanguageCode = 0;
    Spinner source, target;
    EditText source_txt;
    ImageButton mic;
    TextView target_txt;

    String url = "http://127.0.0.1:5000/translate";

    private void translateText(int fromlanguageCode, int tolanguageCode, String source_txt){
        target_txt.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromlanguageCode)
                .setTargetLanguage(tolanguageCode)
                .build();
        FirebaseTranslator translator= FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translator.translate(source_txt).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        target_txt.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Failed to translate",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed to download model",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getLanguageCode(String language){
        int languageCode =0;
        switch (language){
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "Welsh":
                languageCode = FirebaseTranslateLanguage.CY;
                break;
            case "German":
                languageCode = FirebaseTranslateLanguage.DE;
                break;
            case "Estonian":
                languageCode = FirebaseTranslateLanguage.ET;
                break;
            case "Persian":
                languageCode = FirebaseTranslateLanguage.FA;
                break;
            case "Indonesian":
                languageCode = FirebaseTranslateLanguage.ID;
                break;
            case "Japanese":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            case "Latvian":
                languageCode = FirebaseTranslateLanguage.LV;
                break;
            case "Slovenian":
                languageCode = FirebaseTranslateLanguage.SL;
                break;
            case "Swedish":
                languageCode = FirebaseTranslateLanguage.SV;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
                break;
            case "Turkish":
                languageCode = FirebaseTranslateLanguage.TR;
                break;
            default:
                languageCode = 0;
        }
        return languageCode;
    }


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
        mic=view.findViewById(R.id.mic_btn);

        String[] source_list = {"From","English", "Hindi", "Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Slovenian", "Swedish", "Tamil", "Turkish"};
        String[] target_list = {"To","English", "Hindi", "Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Slovenian", "Swedish", "Tamil", "Turkish"};

        ArrayAdapter<String> source_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, source_list);
        ArrayAdapter<String> target_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, target_list);

        source_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        target_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        source.setAdapter(source_adapter);
        target.setAdapter(target_adapter);

        source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromlanguageCode = getLanguageCode(source_list[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        target.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tolanguageCode = getLanguageCode(target_list[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        String input_txt = source_txt.getText().toString();
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast
                            .makeText(getContext(), " " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });




        source_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                target_txt.setText("");
                if(source_txt.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please enter text for translation",Toast.LENGTH_SHORT).show();
                }else if (fromlanguageCode ==0) {
                    Toast.makeText(getContext(),"Please select the source language",Toast.LENGTH_SHORT).show();
                }
                else if (tolanguageCode ==0) {
                    Toast.makeText(getContext(),"Please select the target language",Toast.LENGTH_SHORT).show();
                }
                else{
                    translateText(fromlanguageCode,tolanguageCode,source_txt.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {

                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                source_txt.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }

}

