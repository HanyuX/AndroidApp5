package edu.dartmouth.cs.actiontabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;


public class SettingFragment extends PreferenceFragment {

    /**
     * called when the fragment is created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        PreferenceScreen preferenceScreen = (PreferenceScreen) getPreferenceScreen();
        ListPreference res = (ListPreference) preferenceScreen.findPreference("Unit Preference");
        res.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences.Editor sharedPreferences = preference.getEditor();
                sharedPreferences.putString("measure", o.toString());
                sharedPreferences.commit();
                return true;
            }
        });
    }
}

