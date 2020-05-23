package com.jiuwfung.comp6239.events.recycler;

public class EventChooseAdapterItem {
    public String ID;
    public String Name;
    public boolean isCheck;
    public EventChooseAdapterItem(String ID , String Name ){
        this.ID = ID;
        this.Name = Name;
    }

    public String getGroup(){
        return ID;
    }

    public String getName(){
        return  Name;
    }

    public boolean getCheck(){
        return isCheck;
    }
}
