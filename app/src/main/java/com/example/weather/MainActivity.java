package com.example.weather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.contentcapture.DataShareRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.bean.WeatherBean;
import com.example.weather.database.WeatherDatabase;
import com.example.weather.pojo.Weather;
import com.example.weather.ui.dashboard.DashboardFragment;
import com.example.weather.ui.home.HomeFragment;
import com.example.weather.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;


import com.google.gson.Gson;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private WeatherDatabase weatherDB;
    private String dataBaseName = "WeatherDatabase";
    private List<String> cityList;   //城市列表
    private List<String> allCityList;   //所有城市的列表
    private DashboardFragment dashboardFragment;
    private HomeFragment homeFragment;
    private NotificationsFragment notificationsFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private List<WeatherBean> weatherBeanList;  //weatherBeanList是数据库中所有数据的集合，一个数据=id+city+gsonString
    private List<Weather> weatherList;  //weather具体信息类的对象的集合
    // 天气情况查询接口地址
    public static String API_URL = "http://apis.juhe.cn/simpleWeather/query";
    // 接口请求Key
    public static String API_KEY = "078d07f24e5416f9c9e1449474f20a05";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAllCityList();
        initDatabase();
        initWeatherInfor();
        //三个主要页面的fragment初始化
        homeFragment=new HomeFragment();
        dashboardFragment=new DashboardFragment();
        notificationsFragment=new NotificationsFragment();
        //下方导航按键的一些初始化
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                initWeatherInfor();
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //hideFragment(transaction);
                        transaction.replace(R.id.nav_host_fragment_activity_main, homeFragment);
                        transaction.commit();
                        System.out.println("1");
                        break;
                    case R.id.navigation_cityList:
                        //hideFragment(transaction);
                        transaction.replace(R.id.nav_host_fragment_activity_main, dashboardFragment);
                        transaction.commit();
                        System.out.println("2");
                        break;
                    case R.id.navigation_notifications:
                        //hideFragment(transaction);
                        transaction.replace(R.id.nav_host_fragment_activity_main, notificationsFragment);
                        transaction.commit();
                        System.out.println("3");
                        break;
                }
                return true;
            }
        });


        //一些初始化


        //已经获得“重庆”和“杭州”，直接先用数据库中的数据进行测试


    }

    //初始化数据库
    private  void initDatabase(){
        weatherDB = Room.databaseBuilder(this, WeatherDatabase.class, dataBaseName)
                .allowMainThreadQueries()
                .build();
    }


    void initAllCityList(){
        allCityList=new ArrayList<>();
        allCityList.add("杭州");
        allCityList.add("重庆");
        allCityList.add("北京");
        allCityList.add("上海");
        allCityList.add("广州");
        allCityList.add("深圳");
        allCityList.add("南京");
        allCityList.add("成都");
//        allCityList.add("");
//        allCityList.add("杭州");



    }

    //从数据库中初始化天气信息和城市列表
    void initWeatherInfor(){
        weatherBeanList=new ArrayList<>();
        weatherBeanList=weatherDB.getWeatherDao().getAllWeatherBean();
        weatherList=new ArrayList<>();  //weather具体信息类的对象的集合
        cityList=new ArrayList<>();
        for (int i=0;i< weatherBeanList.size();i++){
            Gson gson=new Gson();
            Weather weather=gson.fromJson(weatherBeanList.get(i).getGsonString(),Weather.class);
            weatherList.add(weather);
            if(!cityList.contains(weatherBeanList.get(i).getCity())){
                cityList.add(weatherBeanList.get(i).getCity());
            }
        }
    }

    //以下是添加城市的按钮操作
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //创建Menu菜单
        getMenuInflater().inflate(R.menu.add_city_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //对菜单项点击内容进行设置
        int id = item.getItemId();
        if (id == R.id.addCity) {
            showMyDialog();
        }else if(id==R.id.flush){ //重新从http获取信息
            getWeatherService(cityList);
        }
        return super.onOptionsItemSelected(item);
    }

    void showMyDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.add_city_layout,null,false);
        final AlertDialog my_dialog= new AlertDialog.Builder(this).setView(view).create();
        EditText addCityName=view.findViewById(R.id.addCityName);
        Button decideAdd=view.findViewById(R.id.decideAdd);
        Context context=this;
        decideAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName=addCityName.getText().toString();
                if(!allCityList.contains(cityName)){
                    Toast t = Toast.makeText(context,"添加的城市不存在", Toast.LENGTH_SHORT);
                    t.show();
                }
                else if(cityList.contains(cityName)){
                    Toast t = Toast.makeText(context,"该城市已经存在", Toast.LENGTH_SHORT);
                    t.show();
                }
                else if(allCityList.contains(cityName)){
                    cityList.add(cityName);
                    getWeatherService(cityList);
                    Toast t = Toast.makeText(context,"添加成功", Toast.LENGTH_SHORT);
                    t.show();
                    my_dialog.dismiss();
                }
            }
        });
        my_dialog.show();
    }


    //http请求数据，装入数据库
    void getWeatherService(List<String> cityList1){
        //下面是进行一次天气预报接口的请求
        new Thread(new Runnable(){
            @Override
            public void run() {
                //先清空数据库，再进行装入
                weatherDB.getWeatherDao().clear();
                for(int i=0;i<cityList1.size();i++){
                    String cityName=cityList1.get(i);
                    JSONObject weatherObject=queryWeather(cityName);
                    WeatherBean weatherBean=new WeatherBean();
                    weatherBean.setCity(cityName);
                    String gsonString=weatherObject.toString();
                    weatherBean.setGsonString(gsonString);
                    weatherDB.getWeatherDao().insert(weatherBean);
                    //System.out.println(weatherObject);
                }
            }
        }).start();

    }




    /**
     * 根据城市名查询天气情况
     *
     * @param cityName
     */
    public static JSONObject queryWeather(String cityName) {
        Map<String, Object> params = new HashMap<>();//组合参数
        params.put("city", cityName);
        params.put("key", API_KEY);
        String queryParams = urlencode(params);
        System.out.println("queryParams   "+queryParams);
        String response = doGet(API_URL, queryParams);
        try {
            JSONObject jsonObject = JSONObject.fromObject(response);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                System.out.println("调用接口成功");
                return jsonObject;
            } else {
                System.out.println("调用接口失败：" + jsonObject.getString("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get方式的http请求
     *
     * @param httpUrl 请求地址
     * @return 返回结果
     */
    public static String doGet(String httpUrl, String queryParams) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(new StringBuffer(httpUrl).append("?").append(queryParams).toString());
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(5000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(6000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                // 封装输入流，并指定字符集
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                // 存放数据
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append(System.getProperty("line.separator"));
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();// 关闭远程连接
            }
        }
        return result;
    }

    /**
     * 将map型转为请求参数型
     *
     * @param data
     * @return
     */
    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String result = sb.toString();
        result = result.substring(0, result.lastIndexOf("&"));
        return result;
    }


}