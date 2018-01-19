package com.example.android.quakereport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ntumba on 18-1-15.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


    }





    public static Intent getIntent(FragmentActivity activity) {
        return new Intent(activity , SettingsActivity.class);
    }





    public static class EarthquakePreferenceFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_main);

            Preference min = findPreference(getString(R.string.setting_min_magnitude_key));
            bindPreferenceToValue(min);
        }



        private void bindPreferenceToValue(Preference min) {
            min.setOnPreferenceChangeListener(this);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String prefString = sp.getString(min.getKey() , "");
            onPreferenceChange(min , prefString);
        }




        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
           String value = newValue.toString();
           preference.setSummary(value);
           return true;
        }
    }
}
