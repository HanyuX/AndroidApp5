package edu.dartmouth.cs.actiontabs;

import android.database.DatabaseUtils;

/**
 * Created by xuehanyu on 4/19/16.
 */
public class databaseItem {
    public int ID;
    public String Date;
    public String Time;
    public int Duration;
    public int Distance;
    public int Calories;
    public int HeartRate;
    public String Comment;

    public databaseItem(String Date, String Time, int Duration, int Distance, int Calories, int HeartRate, String Comment){
        this.Date = Date;
        this.Time = Time;
        this.Duration = Duration;
        this.Distance = Distance;
        this.Calories = Calories;
        this.HeartRate = HeartRate;
        this.Comment = Comment;
    }
}
