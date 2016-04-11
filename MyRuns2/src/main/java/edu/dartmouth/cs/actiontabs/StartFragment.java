package edu.dartmouth.cs.actiontabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xuehanyu on 4/5/16.
 */
public class StartFragment extends Fragment {
    @Override

    /** called when the fragment is created */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.start, container, false);
    }
}
