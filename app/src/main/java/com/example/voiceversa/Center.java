package com.example.voiceversa;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class Center extends Fragment {
    int bgColor,fontColor;
    EditText size;
    SwitchMaterial caption;
    RelativeLayout settings;
    Button black_font, white_font, grey_font, black_bg, white_bg, grey_bg;
    ImageButton icr_size, dcr_size;
    TextView example;
    float current_size,new_size;
    boolean white_font_selected = true, black_font_selected = false,grey_font_selected=false, black_bg_selected = true, white_bg_selected = false,grey_bg_selected=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, container, false);

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

        example = view.findViewById(R.id.example_caption);
        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resize();
            }
        });
        icr_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dcr_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "dcr", Toast.LENGTH_SHORT).show();
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
                settings.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                Toast.makeText(getContext(), b ? "Checked" : "Unchecked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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
        float currentSize = example.getTextSize();
        example.setTextSize((float) (currentSize + 2.0));
    }

    private void decreaseTextSize() {
        float currentSize = example.getTextSize();
        example.setTextSize((float) (currentSize - 2.0));
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

}
