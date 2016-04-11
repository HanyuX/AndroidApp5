package edu.dartmouth.cs.actiontabs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Dialog;
import java.util.Calendar;

/**
 * Created by xuehanyu on 4/5/16.
 */
public class ManualEntry extends ListActivity {
    static final String[] content = new String[] { "Date", "Time", "Duration",
            "Distance", "Calories","Heart Rate", "Comment" };
    Calendar mDateAndTime = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't have to do this anymore
        setContentView(R.layout.manualentry_layout);

        // Define a new adapter
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, content);

        // Assign the adapter to ListView
        setListAdapter(mAdapter);

        // Define the listener interface
        AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selected = ((TextView) view).getText().toString();
                switch(selected){
                    case "Date":
                        onDateClicked();
                        break;
                    case "Time":
                        onTimeClicked();
                        break;
                    default:
                        showDialog(selected);
                }
            }
        };

        // Get the ListView and wired the listener
        ListView listView = getListView();
        listView.setOnItemClickListener(mListener);
    }

    /** called when the date is clicked */
    private void onDateClicked() {
        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        new DatePickerDialog(ManualEntry.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    /** called when the time is clicked */
    private void onTimeClicked() {

        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDateAndTime.set(Calendar.MINUTE, minute);
            }
        };

        new TimePickerDialog(ManualEntry.this, mTimeListener,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), true).show();
    }

    /** showing the dialog*/
    private void showDialog(String title){
        MyAlertDialogFragment mydialog = new MyAlertDialogFragment();
        DialogFragment newFragment = mydialog
                .newInstance(title);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void doCancleClick() {
    }

    public void doOkClick() {
    }


    /** the fragment used to show the dialog */
    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(String title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            EditText editText = new EditText(getActivity());
            if(title.equals("Comment"))
                editText.setHint("How did it go? Notes here.");
            else
                editText.setInputType(2);

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setView(editText)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((ManualEntry) getActivity())
                                            .doOkClick();
                                }
                            })
                    .setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((ManualEntry) getActivity())
                                            .doCancleClick();
                                }
                            }).create();
        }
    }

    /** called when the save button is clicked */
    public void onEntrySaveClicked(View v) {
        finish();
    }

    /** called when the cancel button is clicked */
    public void onEntryCancelClicked(View v) {
        Toast.makeText(getApplicationContext(), "Entry discarded.",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
