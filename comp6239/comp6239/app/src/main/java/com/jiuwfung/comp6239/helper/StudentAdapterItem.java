package com.jiuwfung.comp6239.helper;

import java.util.Set;

public class StudentAdapterItem {
    public String Key;
    public String FirstName;
    public String LastName;
    public String Gender;
    public String FatherName;
    public String FatherNumber;
    public String MotherName;
    public String MotherNumber;
    public Set<String> Events;

    public StudentAdapterItem(String Key , String FirstName , String LastName , String Gender , String FatherName , String FatherNumber ,
                       String MotherName , String MotherNumber, Set<String> Events){
        this.Key = Key;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Gender = Gender;
        this.FatherName = FatherName;
        this.FatherNumber = FatherNumber;
        this.MotherName = MotherName;
        this.MotherNumber = MotherNumber;
        this.Events = Events;
    }
}
