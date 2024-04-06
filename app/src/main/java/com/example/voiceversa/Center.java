    package com.example.voiceversa;
    
    import static android.content.ContentValues.TAG;
    import static androidx.core.content.ContextCompat.startForegroundService;
    
    import android.Manifest;

    import android.annotation.SuppressLint;
    import android.annotation.TargetApi;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    
    import android.content.Context;
    
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;

    import android.graphics.PixelFormat;
    import android.graphics.drawable.ColorDrawable;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.Settings;
    import android.transition.TransitionInflater;
    import android.util.Log;
    import android.util.TypedValue;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.WindowManager;
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
    import androidx.core.app.NotificationCompat;
    import androidx.core.content.ContextCompat;
    import androidx.fragment.app.Fragment;
    
    
    
    import com.google.android.material.switchmaterial.SwitchMaterial;

    public class Center extends Fragment {

        int bgColor,fontColor,textColor=0xFFFFFFFF,backgroundColor=0xFF000000;
        Spinner source,target;
        EditText size;
        SwitchMaterial caption;
        RelativeLayout settings;
        Button black_font, white_font, grey_font, black_bg, white_bg, grey_bg, save;
        ImageButton icr_size, dcr_size;
        TextView example;
        float current_size,new_size,fontSize;
        private static final String PREFS_NAME = "MyPrefs";
        private static final String TOGGLE_STATE_KEY = "toggleState";
        private SharedPreferences prefs;
    
        boolean white_font_selected = true, black_font_selected = false,grey_font_selected=false, black_bg_selected = true, white_bg_selected = false,grey_bg_selected=false;
        private static final String CHANNEL_ID = "my_channel";
        static final int NOTIFICATION_ID = 123;
        private static final int PERMISSION_REQUEST_CODE1 = 123;
        private static final int PERMISSION_REQUEST_CODE2 = 101;
        private static final int PERMISSION_REQUEST_CODE3 = 202;
        private Context mContext;
        private WindowManager windowManager;
        private View overlayView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_center, container, false);
            TransitionInflater tinflater = TransitionInflater.from(requireContext());
            setExitTransition(tinflater.inflateTransition(R.transition.fade_out));
            setEnterTransition(tinflater.inflateTransition(R.transition.fade_out));
            caption = view.findViewById(R.id.caption_switch);
            settings = view.findViewById(R.id.caption_settings_layout);
            black_font = view.findViewById(R.id.black_font);
            grey_font=view.findViewById(R.id.grey_font);
            white_font = view.findViewById(R.id.white_font);
            black_bg = view.findViewById(R.id.black_bg);
            grey_bg=view.findViewById(R.id.grey_bg);
            white_bg = view.findViewById(R.id.white_bg);
            size=view.findViewById(R.id.size_edttxt);
            icr_size=view.findViewById(R.id.size_icr);
            dcr_size=view.findViewById(R.id.size_dcr);
            save=view.findViewById(R.id.save_settings);
            prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean savedState = prefs.getBoolean(TOGGLE_STATE_KEY, false);
            caption.setChecked(savedState);
            example = view.findViewById(R.id.example_caption);
            fontSize=example.getTextSize();
            size.setText(Float.toString(example.getTextSize()));
            source=view.findViewById(R.id.source_spinner);
            target=view.findViewById(R.id.target_spinner);
            windowManager = (WindowManager) requireActivity().getSystemService(Context.WINDOW_SERVICE);
            String[] source_list = {"English","Hindi","Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Mongolian", "Slovenian", "Swedish", "Tamil", "Turkish", "Chinese"};
            String[] target_list = {"English","Hindi","Arabic", "Catalan", "Welsh", "German", "Estonian", "Persian", "Indonesian", "Japanese", "Latvian", "Mongolian", "Slovenian", "Swedish", "Tamil", "Turkish", "Chinese"};
            ArrayAdapter<String> source_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, source_list);
            ArrayAdapter<String> target_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, target_list);

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
                    grey_bg_selected=false;
                    toggleFontColor();
                }
            });
            grey_font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    black_font_selected = false;
                    white_font_selected = false;
                    grey_bg_selected=true;
                    toggleFontColor();
                }
            });
    
            white_font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    black_font_selected = false;
                    white_font_selected = true;
                    grey_font_selected=true;
                    toggleFontColor();
                }
            });
    
            black_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    black_bg_selected = true;
                    white_bg_selected = false;
                    grey_bg_selected=false;
                    toggleBgColor();
                }
            });
            grey_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    black_bg_selected = false;
                    white_bg_selected = false;
                    grey_bg_selected=true;
                    toggleBgColor();
                }
            });
            white_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    black_bg_selected = false;
                    grey_bg_selected=false;
                    white_bg_selected = true;
                    toggleBgColor();
                }
            });
            caption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(TOGGLE_STATE_KEY, b);
                    editor.apply();


                    if (b) {
                        showNotification();
                        if (checkDrawOverlayPermission()) {
                            addOverlayView();
                        }
                    } else {
                        cancelNotification();
                        removeOverlayView();
                    }

                }
            });
    
    
            mContext = getContext();
    
    
    
    
            return view;
        }
        @SuppressLint("ClickableViewAccessibility")
        private void addOverlayView() {
            if (overlayView == null) {
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                overlayView = inflater.inflate(R.layout.captions, null);
                TextView captionTextView = overlayView.findViewById(R.id.caption_txt);
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
            if (mContext.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permissions not granted, request them
                requestPermissions(new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.POST_NOTIFICATIONS
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
            new_size=current_size+1;
            example.setTextSize(TypedValue.COMPLEX_UNIT_PX, new_size);
            size.setText(Float.toString(new_size));
        }
    
    
        private void decreaseTextSize() {
            current_size = example.getTextSize();
            new_size=current_size-1;
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
            if (mContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE1);
                return;
            }
            if (mContext.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
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
        private static int REQUEST_CODE = 1;
        private boolean checkDrawOverlayPermission() {
            if (!Settings.canDrawOverlays(mContext)) {
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

















    }
