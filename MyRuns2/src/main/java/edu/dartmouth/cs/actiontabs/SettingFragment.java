package edu.dartmouth.cs.actiontabs;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingFragment extends PreferenceFragment {
	
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.settingfragment, container, false);
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}

