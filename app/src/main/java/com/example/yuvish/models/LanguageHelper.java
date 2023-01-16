package com.example.yuvish.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LanguageHelper {
    private static final String SELECTED_LANGUAGE = "Language.Helper.Selected.Language";

    public static Context setLocale(Context context, String language){
        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return updateResources(context, language);
        }
        return updateResources(context, language);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();

    }

    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();

        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        res.updateConfiguration(config, res.getDisplayMetrics());
        return context;
    }
}
