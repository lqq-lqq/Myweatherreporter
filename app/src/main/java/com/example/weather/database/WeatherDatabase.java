package com.example.weather.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.weather.bean.WeatherBean;
import com.example.weather.dao.WeatherDao;

@Database(entities = { WeatherBean.class }, version = 1,exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    private static final String DB_NAME = "WeatherDatabase.db";
    private static volatile WeatherDatabase instance;
    static synchronized WeatherDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
    private static WeatherDatabase create(final Context context) {
        return Room.databaseBuilder(
                context, WeatherDatabase.class, DB_NAME).build();
    }
    public abstract WeatherDao getWeatherDao();
}
