package edu.dartmouth.cs.actiontabs;

import android.database.DatabaseUtils;

/**
 * Created by xuehanyu on 4/19/16.
 */
public class databaseItem {
    public String ID = "";
    public String Date = "";
    public String Time = "";
    public int Duration = -1;
    public int Distance = -1;
    public int Calories = -1;
    public int HeartRate = -1;
    public String Comment = "";
    public String InputType = "";
    public String ActivityType = "";

    public databaseItem(String ID, String Date, String Time, int Duration, int Distance, int Calories,
                        int HeartRate, String Comment, String InputType, String ActivityType){
        this.ID = ID;
        this.Date = Date;
        this.Time = Time;
        this.Duration = Duration;
        this.Distance = Distance;
        this.Calories = Calories;
        this.HeartRate = HeartRate;
        this.Comment = Comment;
        this.InputType = InputType;
        this.ActivityType = ActivityType;
    }

    public databaseItem(){}
}
