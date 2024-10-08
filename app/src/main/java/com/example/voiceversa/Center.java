    package com.example.voiceversa;
    
    import static android.app.Activity.RESULT_OK;
    import static android.content.ContentValues.TAG;
    import static android.content.Context.CLIPBOARD_SERVICE;
    import static androidx.core.content.ContextCompat.startForegroundService;
    
    import android.Manifest;

    import android.annotation.SuppressLint;
    import android.annotation.TargetApi;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;

    import android.content.ClipData;
    import android.content.ClipboardManager;
    import android.content.Context;
    
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;

    import android.graphics.PixelFormat;
    import android.graphics.drawable.AnimationDrawable;
    import android.graphics.drawable.ColorDrawable;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.Settings;
    import android.speech.RecognitionListener;
    import android.speech.RecognizerIntent;
    import android.speech.SpeechRecognizer;
    import android.speech.tts.TextToSpeech;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.transition.TransitionInflater;
    import android.util.Log;
    import android.util.TypedValue;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.WindowManager;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.CompoundButton;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.RelativeLayout;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;
    
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.constraintlayout.widget.ConstraintLayout;
    import androidx.core.app.NotificationCompat;
    import androidx.core.content.ContextCompat;
    import androidx.fragment.app.Fragment;


    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.material.switchmaterial.SwitchMaterial;
    import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
    import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
    import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
    import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
    import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
    import com.google.mlkit.nl.languageid.LanguageIdentification;
    import com.google.mlkit.nl.languageid.LanguageIdentifier;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.Locale;
    import java.util.Objects;
    import java.util.Set;

    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.RequestBody;
    import okhttp3.Response;


    public class Center extends Fragment {
        public Center(){}
        private static final int REQUEST_CODE_SPEECH_INPUT = 1;
        private static final int REQUEST_PERMISSION_CODE = 1;
        int languageCode, fromlanguageCode, tolanguageCode = 0;
        Spinner source, target;
        EditText source_txt;
        ImageButton mic,speakbtn,paste;
        TextView target_txt;
        String input_txt,output_text;
        TextToSpeech mTTS;
        ClipboardManager clipboard;
        ClipData clip;



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
                            addModelNameToSharedPreferences(fromlanguageCode,tolanguageCode);
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
        private String getApiKey() {
            // Replace with your preferred method of retrieving the API key
            return System.getProperty("API_KEY");
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
                case "Gujarati":
                    languageCode = FirebaseTranslateLanguage.GU;
                    break;
                case "Kannada":
                    languageCode = FirebaseTranslateLanguage.KN;
                    break;
                case "Marathi":
                    languageCode = FirebaseTranslateLanguage.MR;
                    break;
                case "Telugu":
                    languageCode = FirebaseTranslateLanguage.TE;
                    break;
                case "Tamil":
                    languageCode = FirebaseTranslateLanguage.TA;
                    break;
                case "Punjabi":
                    languageCode = 69; //abhi filhal
                    break;

                default:
                    languageCode = 0;
            }
            return languageCode;
        }
        public String getLanguageName(int languageCode){
            switch (languageCode) {
                case FirebaseTranslateLanguage.EN:
                    return "en";
                case FirebaseTranslateLanguage.HI:
                    return "hi";
                case FirebaseTranslateLanguage.GU:
                    return "gu";
                case FirebaseTranslateLanguage.KN:
                    return "kn";
                case FirebaseTranslateLanguage.MR:
                    return "mr";
                case FirebaseTranslateLanguage.TE:
                    return "te";
                case FirebaseTranslateLanguage.TA:
                    return "TA";
                default:
                    return "en";
            }
        }
        public int getDetectedLanguageCode(String detectedLang) {
            switch (detectedLang) {
                case "en":
                    return FirebaseTranslateLanguage.EN;
                case "hi":
                    return FirebaseTranslateLanguage.HI;
                case "gu":
                    return FirebaseTranslateLanguage.GU;
                case "kn":
                    return FirebaseTranslateLanguage.KN;
                case "mr":
                    return FirebaseTranslateLanguage.MR;
                case "te":
                    return FirebaseTranslateLanguage.TE;
                case "ta":
                    return FirebaseTranslateLanguage.TA;
                case "pa": // Assuming 69 is for Punjabi
                    return 69;
                default:
                    return FirebaseTranslateLanguage.EN; // Default to English if language is not supported
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_center, container, false);
//            ConstraintLayout constraintLayout = view.findViewById(R.id.constraint_center);
//
//            AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
//            animationDrawable.setEnterFadeDuration(2500);
//            animationDrawable.setExitFadeDuration(5000);
//            animationDrawable.start();

            TransitionInflater tinflater = TransitionInflater.from(requireContext());
            setExitTransition(tinflater.inflateTransition(R.transition.fade_out));
            setEnterTransition(tinflater.inflateTransition(R.transition.fade_out));

            source = view.findViewById(R.id.source_spinner);
            target = view.findViewById(R.id.target_spinner);
            source_txt = view.findViewById(R.id.source_txt);
            target_txt = view.findViewById(R.id.target_txt);
            mic=view.findViewById(R.id.mic_btn);
            speakbtn = view.findViewById(R.id.speak_btn);
            paste=view.findViewById(R.id.paste_btn);

            String[] source_list = {"Auto-detect","English", "Hindi","Gujarati", "Kannada", "Marathi", "Telugu","Tamil"};
            String[] target_list = {"Target","English", "Hindi","Gujarati", "Kannada", "Marathi", "Telugu","Tamil"};

            ArrayAdapter<String> source_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, source_list);
            ArrayAdapter<String> target_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, target_list);

            source_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            target_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            source.setAdapter(source_adapter);
            target.setAdapter(target_adapter);
            LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();


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
            paste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    clip = ClipData.newPlainText("text", target_txt.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            input_txt = source_txt.getText().toString();

            speakbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String apiKey = getApiKey();
                    mTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS)
                            {
                                String langcode = getLanguageName(tolanguageCode);
                                mTTS.setLanguage(new Locale(langcode));
                                Locale locale = new Locale(langcode);

                                // Set the language for the TextToSpeech engine
                                int result = mTTS.setLanguage(locale);

                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Toast.makeText(getContext(),"Could not generate output",Toast.LENGTH_SHORT).show();
                                    // You might want to provide a default language or show an error message
                                } else {
                                    // Language is supported, proceed with speaking
                                    output_text = target_txt.getText().toString();

                                    mTTS.speak(output_text, TextToSpeech.QUEUE_FLUSH, null);

                                    Toast.makeText(getContext(),output_text,Toast.LENGTH_SHORT).show();
                                }

                                // Text-to-speech engine initialized successfully
                                // Set language, rate, pitch, etc., if needed
                            } else {
                                // Text-to-speech engine failed to initialize
                            }

                        }
                    });
                }
            });


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
                    if (source_txt.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Please enter text for translation", Toast.LENGTH_SHORT).show();
                    } else if (fromlanguageCode == 69 || tolanguageCode == 69) {
                        // Use Bhashini API for Punjabi translation or any other language not supported by Firebase
                        Toast.makeText(getContext(),"Under development",Toast.LENGTH_SHORT).show();
                        } else {
                        // Use Firebase ML Kit for other translations
                        translateText(fromlanguageCode, tolanguageCode, source_txt.getText().toString());
                        languageIdentifier.identifyLanguage(source_txt.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode.equals("und")) {
                                    Log.i(TAG, "Can't identify language.");

                                } else {
                                    Log.i(TAG, "Language: " + languageCode);
                                    fromlanguageCode = getDetectedLanguageCode(languageCode);
                                    String selection;
                                    int position;
                                    switch (languageCode){
                                        case "en":
                                            selection="English";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                        case "hi":
                                            selection="Hindi";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                        case "gu":
                                            selection="Gujarati";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                        case "kn":
                                            selection="Kannada";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                        case "mr":
                                            selection="Marathi";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                        case "te":
                                            selection="Telugu";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                        case "ta":
                                            selection="Tamil";
                                            position=source_adapter.getPosition(selection);
                                            source.setSelection(position);
                                            break;
                                    }
                                }
                            }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

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
        private void addModelNameToSharedPreferences(int sourceLanguage, int targetLanguage) {

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("downloaded_models", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Create a copy of the set retrieved from SharedPreferences (avoid modifying original)
            Set<String> currentModelNames = new HashSet<>(sharedPreferences.getStringSet("downloaded_models", new HashSet<>()));

            // Convert language codes to model names (assuming a function for this)
            String model1= String.valueOf(sourceLanguage);
            String model2 = String.valueOf(targetLanguage);

            // Add the new model names to the copy
            currentModelNames.add(model1);
            if (!model1.equals(model2)) {
                currentModelNames.add(model2);
            }

            // Store the updated set back to SharedPreferences
            editor.putStringSet("downloaded_models", currentModelNames);

            editor.apply();
        }

    }
