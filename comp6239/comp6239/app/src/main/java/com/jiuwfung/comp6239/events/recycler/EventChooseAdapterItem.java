package com.jiuwfung.comp6239.events.recycler;

public class EventChooseAdapterItem {
    public String Group;
    public String Name;
    public boolean isCheck;
    public EventChooseAdapterItem(String Group , String Name , boolean isCheck){
        this.Group = Group;
        this.Name = Name;
        this.isCheck = isCheck;
    }

    public String getGroup(){
        return Group;
    }

    public String getName(){
        return  Name;
    }

    public boolean getCheck(){
        return isCheck;
    }
}
