package edu.dartmouth.cs.actiontabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.view.ViewPager;


public class SettingFragment extends PreferenceFragment {

    /**
     * called when the fragment is created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        //get the result of the preference
        PreferenceScreen preferenceScreen = (PreferenceScreen) getPreferenceScreen();
        ListPreference res = (ListPreference) preferenceScreen.findPreference("Unit Preference");
        res.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //store the result into the sharedpreference
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("measure", o.toString());
                editor.commit();
                ViewPager v = (ViewPager)getActivity().findViewById(R.id.viewpager);
                ActionTabsViewPagerAdapter adapter = (ActionTabsViewPagerAdapter)v.getAdapter();
                HistoryFragment fragment = (HistoryFragment)adapter.getItem(1);
                //refresh the list view
                fragment.reLoadData();
                return true;
            }
        });
    }
}

