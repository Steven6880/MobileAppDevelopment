package com.jiuwfung.comp6239.helper;

import java.util.List;

public class ParentAdapterItem {

    public String Key;
    public String FirstName;
    public String LastName;
    public List<String> ChildID;
    public String Number;
    public String Relation;

    public ParentAdapterItem(String Key , String FirstName , String LastName , List<String> ChildID,
                      String Number , String Relation){
        this.Key = Key;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.ChildID = ChildID;
        this.Number = Number;
        this.Relation = Relation;
    }


}
