package edu.dartmouth.cs.actiontabs;

/**
 * Created by xuehanyu on 4/19/16.
 */
public class databaseItem {
    public String ID = "";                //ID of the item
    public String Date = "";              //Date of the item
    public String Time = "";              //time of the item
    public double Duration = -1;          //the total time for the activity
    public double Distance = -1;          //the total distance for the activity
    public int Calories = -1;             //the total calories cost in the activity
    public int HeartRate = -1;            //the heart rate in the activity
    public String Comment = "";           //comments for the activity
    public String InputType = "";         //input type
    public String ActivityType = "";      //the activity type

    public databaseItem(String ID, String Date, String Time, double Duration, double Distance, int Calories,
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
