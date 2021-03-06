package edu.dartmouth.cs.actiontabs;

/**
 * Created by xuehanyu on 4/23/16.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

/** the fragment used to show the dialog */
public class MyAlertDialogFragment extends DialogFragment {
    private databaseItem item;

    public void doCancelClick() {
    }

    /*
     * called when the user clicks the save button
     */
    public void doOkClick(String title, String data) {
        int dataInt = 0;
        double dataDouble = 0;
        if (title.equals("Comment")) ;
        else if (title.equals("Duration") || title.equals("Distance"))
            dataDouble = Double.parseDouble(data);
        else
            dataInt = Integer.parseInt(data);
        switch (title) {
            case "Duration":
                item.setDuration(dataDouble);
                break;
            case "Distance":
                item.setDistance(dataDouble);
                break;
            case "Calories":
                item.setCalories(dataInt);
                break;
            case "Heart Rate":
                item.setHeartRate(dataInt);
                break;
            case "Comment":
                item.setComment(data);
        }
    }

    public MyAlertDialogFragment newInstance(String title, databaseItem item) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setDataItem(item);
        return frag;
    }

    public void setDataItem(databaseItem item){
        this.item = item;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        final EditText editText = new EditText(getActivity());
        if(title.equals("Comment")) {
            editText.setHint("How did it go? Notes here.");
            editText.setHeight(400);
        }else if(title.equals("Duration") || title.equals("Distance"))
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        else
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        //show the result in the dialog
        switch (title){
            case "Duration":
                editText.setText(item.getDuration() < 0 ? "" : item.getDuration()+"");
                break;
            case "Distance":
                editText.setText(item.getDistance() < 0 ? "" : item.getDistance()+"");
                break;
            case "Calories":
                editText.setText(item.getCalories() < 0 ? "" : item.getCalories()+"");
                break;
            case "Heart Rate":
                editText.setText(item.getHeartRate() < 0 ? "" : item.getHeartRate()+"");
                break;
            case "Comment":
                editText.setText(item.getComment()+"");
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                        doOkClick(title, editText.getText().toString());
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                doCancelClick();
                            }
                        }).create();
    }
}
