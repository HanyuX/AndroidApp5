package edu.dartmouth.cs.actiontabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

    }
}
