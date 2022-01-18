package com.example.weather.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import net.sf.json.JSONObject;
@Entity(tableName = "weather")
public class WeatherBean {
    @PrimaryKey(autoGenerate = true) //主键是否自动增长，默认为false
    private int id;
    private String city;
    private String gsonString;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getGsonString() {
        return gsonString;
    }
    public void setGsonString(String gsonString) {
        this.gsonString = gsonString;
    }
}
