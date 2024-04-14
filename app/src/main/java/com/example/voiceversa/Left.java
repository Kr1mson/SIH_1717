package com.example.voiceversa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

import java.util.HashSet;
import java.util.Set;


public class Left extends Fragment {
    public Left(){}
    TextView display;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left,
                container, false);
        TransitionInflater tinflater = TransitionInflater.from(requireContext());
        setExitTransition(tinflater.inflateTransition(R.transition.slide_left));
        setEnterTransition(tinflater.inflateTransition(R.transition.slide_left));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("downloaded_models", Context.MODE_PRIVATE);
        TextView display = view.findViewById(R.id.title);

        Set<String> modelNames = sharedPreferences.getStringSet("downloaded_models", new HashSet<>());

        StringBuilder downloadedModelsText = new StringBuilder();
        for (String model : modelNames) {
            if (model != null) {
                model=getLanguageName(Integer.parseInt(model));

                downloadedModelsText.append(model).append("\n");
            }
        }

        display.setText(downloadedModelsText.toString());

        return view;
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