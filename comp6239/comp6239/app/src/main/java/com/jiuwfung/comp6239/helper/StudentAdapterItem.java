package com.jiuwfung.comp6239.helper;

import java.util.Set;

public class StudentAdapterItem {
    public String StudentID;
    public String Year;
    public String Group;
    public String FirstName;
    public String LastName;
    public Set<String> Events;

    public StudentAdapterItem(String StudentID ,String FirstName , String LastName , String Year, String Group, Set Events){
        this.StudentID = StudentID;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Year = Year;
        this.Group = Group;
        this.Events = Events;
    }
}
