package com.example.voiceversa;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Settings extends AppCompatActivity {
    ImageButton back;
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        back=findViewById(R.id.bacc);

        header = findViewById(R.id.header);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home_Main.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences =getSharedPreferences("downloaded_models", Context.MODE_PRIVATE);


        Set<String> modelNames = sharedPreferences.getStringSet("downloaded_models", new HashSet<>());
        List<String> modelNamesList = new ArrayList<>();
        for (String model : modelNames) {
            if (model != null) {
                String languageName = getLanguageName(Integer.parseInt(model));
                modelNamesList.add(languageName);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingsAdapter adapter = new SettingsAdapter(this, modelNamesList);
        recyclerView.setAdapter(adapter);

    }
    public static String getLanguageName(int code) {
        switch (code) {
            case FirebaseTranslateLanguage.AF:
                return "Afrikaans";
            case FirebaseTranslateLanguage.AR:
                return "Arabic";
            case FirebaseTranslateLanguage.BE:
                return "Belarusian";
            case FirebaseTranslateLanguage.BG:
                return "Bulgarian";
            case FirebaseTranslateLanguage.BN:
                return "Bengali";
            case FirebaseTranslateLanguage.CA:
                return "Catalan";
            case FirebaseTranslateLanguage.CS:
                return "Czech";
            case FirebaseTranslateLanguage.CY:
                return "Welsh";
            case FirebaseTranslateLanguage.DA:
                return "Danish";
            case FirebaseTranslateLanguage.DE:
                return "German";
            case FirebaseTranslateLanguage.EL:
                return "Greek";
            case FirebaseTranslateLanguage.EN:
                return "English";
            case FirebaseTranslateLanguage.EO:
                return "Esperanto";
            case FirebaseTranslateLanguage.ES:
                return "Spanish";
            case FirebaseTranslateLanguage.ET:
                return "Estonian";
            case FirebaseTranslateLanguage.FA:
                return "Persian";
            case FirebaseTranslateLanguage.FI:
                return "Finnish";
            case FirebaseTranslateLanguage.FR:
                return "French";
            case FirebaseTranslateLanguage.GA:
                return "Irish";
            case FirebaseTranslateLanguage.GL:
                return "Galician";
            case FirebaseTranslateLanguage.GU:
                return "Gujarati";
            case FirebaseTranslateLanguage.HE:
                return "Hebrew";
            case FirebaseTranslateLanguage.HI:
                return "Hindi";
            case FirebaseTranslateLanguage.HR:
                return "Croatian";
            case FirebaseTranslateLanguage.HT:
                return "Haitian Creole";
            case FirebaseTranslateLanguage.HU:
                return "Hungarian";
            case FirebaseTranslateLanguage.ID:
                return "Indonesian";
            case FirebaseTranslateLanguage.IS:
                return "Icelandic";
            case FirebaseTranslateLanguage.IT:
                return "Italian";
            case FirebaseTranslateLanguage.JA:
                return "Japanese";
            case FirebaseTranslateLanguage.KA:
                return "Georgian";
            case FirebaseTranslateLanguage.KN:
                return "Kannada";
            case FirebaseTranslateLanguage.KO:
                return "Korean";
            case FirebaseTranslateLanguage.LT:
                return "Lithuanian";
            case FirebaseTranslateLanguage.LV:
                return "Latvian";
            case FirebaseTranslateLanguage.MK:
                return "Macedonian";
            case FirebaseTranslateLanguage.MR:
                return "Marathi";
            case FirebaseTranslateLanguage.MS:
                return "Malay";
            case FirebaseTranslateLanguage.MT:
                return "Maltese";
            case FirebaseTranslateLanguage.NL:
                return "Dutch";
            case FirebaseTranslateLanguage.NO:
                return "Norwegian";
            case FirebaseTranslateLanguage.PL:
                return "Polish";
            case FirebaseTranslateLanguage.PT:
                return "Portuguese";
            case FirebaseTranslateLanguage.RO:
                return "Romanian";
            case FirebaseTranslateLanguage.RU:
                return "Russian";
            case FirebaseTranslateLanguage.SK:
                return "Slovak";
            case FirebaseTranslateLanguage.SL:
                return "Slovenian";
            case FirebaseTranslateLanguage.SQ:
                return "Albanian";
            case FirebaseTranslateLanguage.SV:
                return "Swedish";
            case FirebaseTranslateLanguage.SW:
                return "Swahili";
            case FirebaseTranslateLanguage.TA:
                return "Tamil";
            case FirebaseTranslateLanguage.TE:
                return "Telugu";
            case FirebaseTranslateLanguage.TH:
                return "Thai";
            case FirebaseTranslateLanguage.TL:
                return "Tagalog";
            case FirebaseTranslateLanguage.TR:
                return "Turkish";
            case FirebaseTranslateLanguage.UK:
                return "Ukrainian";
            case FirebaseTranslateLanguage.UR:
                return "Urdu";
            case FirebaseTranslateLanguage.VI:
                return "Vietnamese";
            case FirebaseTranslateLanguage.ZH:
                return "Chinese";
            default:
                return "Unknown Language";
        }
    }
}