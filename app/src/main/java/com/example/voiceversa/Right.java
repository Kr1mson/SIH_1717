package com.example.voiceversa;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


import java.util.ArrayList;

import java.util.HashSet;
import java.util.Locale;

import java.util.Objects;
import java.util.Set;


public class Right extends Fragment {
    int bgColor, fontColor, textColor = 0xFFFFFFFF, backgroundColor = 0xFF000000;
    Spinner source, target;
    EditText size;
    String finaltxt = "none yet";
    SwitchMaterial caption;
    RelativeLayout settings;
    Button black_font, white_font, grey_font, black_bg, white_bg, grey_bg, save;
    ImageButton icr_size, dcr_size;
    TextView example, header, captionTextView;
    float current_size, new_size, fontSize;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String TOGGLE_STATE_KEY = "toggleState";
    private SharedPreferences prefs;
    private SpeechRecognizer speechRecognizer;

    boolean white_font_selected = true, black_font_selected = false, grey_font_selected = false, black_bg_selected = true, white_bg_selected = false, grey_bg_selected = false;
    private static final String CHANNEL_ID = "my_channel";
    static final int NOTIFICATION_ID = 123;
    private static final int PERMISSION_REQUEST_CODE1 = 123;
    private static final int PERMISSION_REQUEST_CODE2 = 101;
    private static final int PERMISSION_REQUEST_CODE3 = 202;
    private Context mContext;
    private WindowManager windowManager;
    private View overlayView;
    int languageCode, fromlanguageCode, tolanguageCode = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_right, container, false);
        ConstraintLayout constraintLayout = view.findViewById(R.id.constraint_right);

        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        TransitionInflater tinflater = TransitionInflater.from(requireContext());

        setExitTransition(tinflater.inflateTransition(R.transition.slide_right));
        setEnterTransition(tinflater.inflateTransition(R.transition.slide_right));
        caption = view.findViewById(R.id.caption_switch);
        header = view.findViewById(R.id.header);
        settings = view.findViewById(R.id.caption_settings_layout);
        black_font = view.findViewById(R.id.black_font);
        grey_font = view.findViewById(R.id.grey_font);
        white_font = view.findViewById(R.id.white_font);
        black_bg = view.findViewById(R.id.black_bg);
        grey_bg = view.findViewById(R.id.grey_bg);
        white_bg = view.findViewById(R.id.white_bg);
        size = view.findViewById(R.id.size_edttxt);
        icr_size = view.findViewById(R.id.size_icr);
        dcr_size = view.findViewById(R.id.size_dcr);
        save = view.findViewById(R.id.save_settings);
        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean savedState = prefs.getBoolean(TOGGLE_STATE_KEY, false);
        caption.setChecked(savedState);
        example = view.findViewById(R.id.example_caption);
        fontSize = example.getTextSize();
        size.setText(Float.toString(example.getTextSize()));
        source = view.findViewById(R.id.source_spinner);
        target = view.findViewById(R.id.target_spinner);
        windowManager = (WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE);
        String[] source_list = {"From", "English", "Hindi", "Gujarati", "Kannada", "Marathi", "Telugu","Tamil","Punjabi"};
        String[] target_list = {"To", "English", "Hindi", "Gujarati", "Kannada", "Marathi", "Telugu","Tamil","Punjabi"};
        ArrayAdapter<String> source_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, source_list);
        ArrayAdapter<String> target_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, target_list);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        source_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        target_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        source.setAdapter(source_adapter);
        target.setAdapter(target_adapter);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textColor = example.getCurrentTextColor();
                ColorDrawable colorDrawable = (ColorDrawable) example.getBackground();
                backgroundColor = colorDrawable.getColor();
                fontSize = example.getTextSize();
                Toast.makeText(getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resize();
            }
        });
        icr_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseTextSize();
            }
        });
        dcr_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseTextSize();
            }
        });

        black_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                black_font_selected = true;
                white_font_selected = false;
                grey_bg_selected = false;
                toggleFontColor();
            }
        });
        grey_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                black_font_selected = false;
                white_font_selected = false;
                grey_bg_selected = true;
                toggleFontColor();
            }
        });

        white_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                black_font_selected = false;
                white_font_selected = true;
                grey_font_selected = true;
                toggleFontColor();
            }
        });

        black_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                black_bg_selected = true;
                white_bg_selected = false;
                grey_bg_selected = false;
                toggleBgColor();
            }
        });
        grey_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                black_bg_selected = false;
                white_bg_selected = false;
                grey_bg_selected = true;
                toggleBgColor();
            }
        });
        white_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                black_bg_selected = false;
                grey_bg_selected = false;
                white_bg_selected = true;
                toggleBgColor();
            }
        });
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
        caption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(TOGGLE_STATE_KEY, isChecked);
                editor.apply();

                if (isChecked) {
                    showNotification();
                    if (checkDrawOverlayPermission()) {
                        addOverlayView();
                        startSpeechRecognition();
                    }
                } else {
                    cancelNotification();
                    removeOverlayView();
                    stopSpeechRecognition();
                }
            }
        });

        if (caption.isChecked()) {
            startSpeechRecognition();
        }


        mContext = getContext();


        return view;
    }


    private void stopSpeechRecognition() {
        speechRecognizer.stopListening();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addOverlayView() {
        if (overlayView == null) {
            LayoutInflater inflater = LayoutInflater.from(requireContext());
            overlayView = inflater.inflate(R.layout.captions, null);
            captionTextView = overlayView.findViewById(R.id.caption_txt);
            captionTextView.setText(finaltxt);
            captionTextView.setTextColor(textColor);
            captionTextView.setBackgroundColor(backgroundColor);
            captionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);


            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            windowManager.addView(overlayView, params);
        }
    }

    private void startSpeechRecognition() {
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override

            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    finaltxt = matches.get(0);

                    if (captionTextView != null) {
                        captionTextView.setText("");
                        if (fromlanguageCode == 0) {
                            Toast.makeText(getContext(), "Please select the source language", Toast.LENGTH_SHORT).show();
                        } else if (tolanguageCode == 0) {
                            Toast.makeText(getContext(), "Please select the target language", Toast.LENGTH_SHORT).show();
                        } else {
                            translateText(fromlanguageCode, tolanguageCode, finaltxt);
                        }
                        captionTextView.setText(finaltxt);
                    }
                }
                // Continue listening for speech
                startSpeechRecognition();
            }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onSegmentResults(@NonNull Bundle segmentResults) {
                RecognitionListener.super.onSegmentResults(segmentResults);
            }

            @Override
            public void onEndOfSegmentedSession() {
                RecognitionListener.super.onEndOfSegmentedSession();
            }

            @Override
            public void onLanguageDetection(@NonNull Bundle results) {
                RecognitionListener.super.onLanguageDetection(results);
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });

        speechRecognizer.startListening(speechRecognizerIntent);
    }


    private void removeOverlayView() {
        if (overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restore visibility based on toggle button state
        boolean toggleState = prefs.getBoolean(TOGGLE_STATE_KEY, false);


        // Check permissions before showing notification
        if (toggleState) {
            checkPermissionsAndShowNotification();

        }
    }

    private void checkPermissionsAndShowNotification() {
        if (mContext.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                mContext.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permissions not granted, request them
            requestPermissions(new String[]{
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.INTERNET
            }, PERMISSION_REQUEST_CODE3);
        } else {
            // Permissions granted, show the notification
            showNotification();
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        // Save toggle button state
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(TOGGLE_STATE_KEY, caption.isChecked());
        editor.apply();
        cancelNotification();
    }


    private void toggleFontColor() {
        if (black_font_selected) {
            fontColor = getResources().getColor(R.color.black);
        } else if (white_font_selected) {
            fontColor = getResources().getColor(R.color.white);
        } else if (grey_font_selected) {
            fontColor = getResources().getColor(R.color.middle_grey);
        }
        example.setTextColor(fontColor);
    }


    private void toggleBgColor() {

        if (black_bg_selected) {
            bgColor = getResources().getColor(R.color.black);
        } else if (white_bg_selected) {
            bgColor = getResources().getColor(R.color.white);
        } else if (grey_bg_selected) {
            bgColor = getResources().getColor(R.color.middle_grey);
        }
        example.setBackgroundColor(bgColor);
    }

    private void increaseTextSize() {
        current_size = example.getTextSize();
        new_size = current_size + 1;
        example.setTextSize(TypedValue.COMPLEX_UNIT_PX, new_size);
        size.setText(Float.toString(new_size));
    }


    private void decreaseTextSize() {
        current_size = example.getTextSize();
        new_size = current_size - 1;
        example.setTextSize(TypedValue.COMPLEX_UNIT_PX, new_size);
        size.setText(Float.toString(new_size));
    }

    private void Resize() {
        String textSizeStr = size.getText().toString();
        if (!textSizeStr.isEmpty()) {
            float newSize = Float.parseFloat(textSizeStr);
            example.setTextSize(newSize);
        } else {
            // Handle empty input
            Toast.makeText(getContext(), "Please enter a valid size", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Both permissions granted, show the notification
                showNotification();
            } else {
                // Permissions denied, show a message or take appropriate action
                Toast.makeText(mContext, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showNotification() {
        // Check if the permission is granted
        if (mContext.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE1);
            return;
        }
        if (mContext.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE2);
            return;
        }


        // Get the shared preference value
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isCaptionsEnabled = prefs.getBoolean(TOGGLE_STATE_KEY, false);

        // Check if captions are enabled in shared preferences
        if (!isCaptionsEnabled) {
            // Captions are not enabled, so do not show the notification
            return;
        }


        // Notification code
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel
        createNotificationChannel(notificationManager);

        // Notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.voiceversa_no_bg)
                .setContentTitle("Captions Enabled")
                .setContentText("Live Captions have been Enabled")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to high
                .setOngoing(true); // Set notification as ongoing (non-dismissible)

        // Show notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    private void cancelNotification() {
        if (!prefs.getBoolean(TOGGLE_STATE_KEY, false)) {
            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        CharSequence name = "My Notification Channel";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_HIGH; // Set channel importance to high
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);

    }

    private static final int REQUEST_CODE = 1;

    private boolean checkDrawOverlayPermission() {
        if (!android.provider.Settings.canDrawOverlays(mContext)) {
            Log.d(TAG, "canDrawOverlays NOK");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + mContext.getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            Log.d(TAG, "canDrawOverlays OK");
        }
        return true;
    }


    private void startOverlayService() {

    }

    private void translateText(int fromlanguageCode, int tolanguageCode, String source_txt) {
        captionTextView.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromlanguageCode)
                .setTargetLanguage(tolanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translator.translate(source_txt).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        addModelNameToSharedPreferences(fromlanguageCode, tolanguageCode);
                        captionTextView.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to translate", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to download model", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language) {
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
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
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

            case "Turkish":
                languageCode = FirebaseTranslateLanguage.TR;
                break;
            default:
                languageCode = 0;
        }
        return languageCode;
    }

    private void addModelNameToSharedPreferences(int sourceLanguage, int targetLanguage) {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("downloaded_models", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create a copy of the set retrieved from SharedPreferences (avoid modifying original)
        Set<String> currentModelNames = new HashSet<>(sharedPreferences.getStringSet("downloaded_models", new HashSet<>()));

        // Convert language codes to model names (assuming a function for this)
        String model1 = String.valueOf(sourceLanguage);
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

