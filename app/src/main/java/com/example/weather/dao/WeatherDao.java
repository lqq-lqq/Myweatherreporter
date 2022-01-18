package com.example.weather.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weather.bean.WeatherBean;

import java.util.List;
@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather")
    List<WeatherBean> getAllWeatherBean();
    @Insert
    void insert(WeatherBean WeatherBean);
    @Update
    void update(WeatherBean WeatherBean);
    @Delete
    void delete(WeatherBean WeatherBean);
    @Query("DELETE FROM weather WHERE city=:cityName")
    void deleteSome(String cityName);
    //清空数据库
    @Query("DELETE FROM weather")
    void clear();
}
