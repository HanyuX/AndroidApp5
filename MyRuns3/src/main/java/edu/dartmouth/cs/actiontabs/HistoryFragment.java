package edu.dartmouth.cs.actiontabs;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuehanyu on 4/5/16.
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<databaseItem>>{

    private ListView listview;
    private List<databaseItem> list;
    private MyAdapter adapter;
    private String res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.history_layout, container, false);
        listview = (ListView) view.findViewById(R.id.datalist);
        adapter = new MyAdapter(getActivity(), list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", list.get(position).ID);
                bundle.putString("ActivityType", list.get(position).ActivityType);
                bundle.putString("DateTime", list.get(position).Time + " " + list.get(position).Date);
                bundle.putDouble("Duration", list.get(position).Duration);
                bundle.putDouble("Distance", list.get(position).Distance);
                bundle.putInt("Calories", list.get(position).Calories);
                bundle.putInt("HeartRate", list.get(position).HeartRate);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        List<databaseItem> list;
        private LayoutInflater layoutInflater;
        public MyAdapter(Context context, List<databaseItem> list) {
            this.context = context;
            this.list = list;
            layoutInflater = LayoutInflater.from(this.context);
        }
        /*
         * return the number of the items in the list
         */
        @Override
        public int getCount() {
            return list.size();
        }

        /*
         * return the item object;
         */
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        /*
         * return the id of the selected item
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * return the image view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_historylayout, null);
            }
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            res = sharedPreferences.getString("measure", "Imperial (Miles)");

            TextView textview1 = (TextView) convertView.findViewById(R.id.method_time);
            TextView textview2 = (TextView) convertView.findViewById(R.id.mile_time);

            textview1.setText("Manual Entry: " + list.get(position).ActivityType + "," + list.get(position).Time + " " + list.get(position).Date);
            int minute = (int)list.get(position).Duration;
            int second = (int)((list.get(position).Duration - minute) * 60);
            int dis = (int)list.get(position).Distance;
            int kdis = (int)(list.get(position).Distance * 1.61);
            if (res.equals("Imperial (Miles)")) {
                if (minute != 0) {
                    if (dis == list.get(position).Distance)
                        textview2.setText(dis + " Miles, " + minute + "mins " + second + "secs");
                    else
                        textview2.setText(list.get(position).Distance + " Miles, " + minute + "mins " + second + "secs");
                }
                else {
                    if (dis == list.get(position).Distance)
                        textview2.setText(dis + " Miles, " + second + "secs");
                    else
                        textview2.setText(list.get(position).Distance + " Miles, " + second + "secs");
                }
            }
            else {
                if (minute != 0) {
                    if (kdis == (list.get(position).Distance * 1.61))
                        textview2.setText(kdis + " Kilometers, " + minute + "mins " + second + "secs");
                    else
                        textview2.setText((list.get(position).Distance * 1.61) + " Kilometers, " + minute + "mins " + second + "secs");
                }
                else {
                    if (kdis == (list.get(position).Distance * 1.61))
                        textview2.setText((list.get(position).Distance * 1.61) + " Kilometers, " + second + "secs");
                    else
                        textview2.setText((list.get(position).Distance * 1.61) + " Kilometers, " + second + "secs");
                }
            }
            return convertView;
        }
    }

    @Override
    public Loader<ArrayList<databaseItem>> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(getActivity()); // DataLoader is your AsyncTaskLoader.
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<databaseItem>> loader, ArrayList<databaseItem> items) {
        //Put your code here.
        list.clear();
        for(databaseItem item : items) {
            list.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<databaseItem>> loader) {
        //Put your code here.
    }

    public void reLoadData(){
        getLoaderManager().restartLoader(0, null, this);
    }

    public static class DataLoader extends AsyncTaskLoader<ArrayList<databaseItem>>{
        private DataBaseHelper helper = new DataBaseHelper(getContext());

        public DataLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            forceLoad(); //Force an asynchronous load.
        }
        @Override
        public ArrayList<databaseItem> loadInBackground() {
            return (ArrayList<databaseItem>)helper.allItems();
        }
    }//end class
}
