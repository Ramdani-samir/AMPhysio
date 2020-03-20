package com.example.amphysio;

public class DataModel {
    String id_;
    String image_Url;
    String name;
    String batt;
    String temp;
    String Msg;

    public DataModel(String name, String id, String message, String Url) {
        this.name = name;
        this.id_ = id;
        this.image_Url = Url;
        this.Msg = message;
    }

    public DataModel(String input_name, String input_id, String input_battement,
                     String input_temperature, String input_image_url) {
        this.name = input_name;
        this.id_ = input_id;
        this.batt = input_battement;
        this.temp = input_temperature;
        this.image_Url = input_image_url;
    }


    public String getPatientName() {
        return this.name;
    }

    public String getPatientImageUrl() {
        return this.image_Url;
    }

    public String getPatientId() {
        return this.id_;
    }

    public String getBatt() {
        return batt;
    }

    public String getTemp() {
        return temp;
    }
    public String getMsg(){ return Msg;}
}
