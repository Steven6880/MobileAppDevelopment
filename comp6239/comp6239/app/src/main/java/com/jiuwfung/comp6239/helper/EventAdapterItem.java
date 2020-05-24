package com.jiuwfung.comp6239.helper;

import java.io.File;
import java.util.List;

public class EventAdapterItem {
    public String Title;
    public String Date;
    public String Time;
    public String Location;
    public String ShortDescription;
    public String Detail;
    public List<String> StudentID;

    public EventAdapterItem(String Title , String Date , String Time, String Location , String ShortDescription, String Detail ,
                     List<String> StudentID){
        this.Title = Title;
        this.Date = Date;
        this.Time = Time;
        this.Location = Location;
        this.ShortDescription = ShortDescription;
        this.Detail = Detail;
        this.StudentID = StudentID;
    }
}
