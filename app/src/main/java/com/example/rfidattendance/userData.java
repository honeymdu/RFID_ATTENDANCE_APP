package com.example.rfidattendance;

public class userData {
    String name,time;
    public userData(){
        
    }

    public userData(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
