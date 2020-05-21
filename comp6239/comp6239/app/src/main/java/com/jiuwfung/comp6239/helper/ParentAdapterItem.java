package com.jiuwfung.comp6239.helper;

public class ParentAdapterItem {

    public String Key;
    public String FirstName;
    public String LastName;
    public String ChildID;
    public String Number;
    public String Relation;

    ParentAdapterItem(String Key , String FirstName , String LastName , String ChildID,
                      String Number , String Relation){
        this.Key = Key;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.ChildID = ChildID;
        this.Number = Number;
        this.Relation = Relation;
    }


}
