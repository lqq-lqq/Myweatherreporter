package com.example.weather.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weather.R;
import com.example.weather.adapter.OneDayAdapter;
import com.example.weather.bean.WeatherBean;
import com.example.weather.database.WeatherDatabase;
import com.example.weather.pojo.Weather;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//最左边的按钮的fragment
public class HomeFragment extends Fragment {

    private RecyclerView cityRecycleView;
    private WeatherDatabase weatherDB;
    private String dataBaseName = "WeatherDatabase";
    private List<String> cityList;   //城市名称列表
    private List<WeatherBean> weatherBeanList;
    private List<View> viewList;    //tab页面的集合
    private List<Weather> weatherList;  //weather具体信息类的对象的集合

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initDatabase();
        initCityList();
        TabLayout tabLayout= view.findViewById(R.id.tab_layout);
        ViewPager viewPager=view.findViewById(R.id.view_pager);
        viewList = new ArrayList<>();
        for (int i=0;i<cityList.size();i++){
            View tabView=getLayoutInflater().inflate(R.layout.show_weather_layout,null);
            ImageView imageView=tabView.findViewById(R.id.picture);
            TextView description=tabView.findViewById(R.id.description);
            TextView temperature=tabView.findViewById(R.id.temperature);
            TextView todayDetail=tabView.findViewById(R.id.todayDetail);
            RecyclerView detailsRecycleView=tabView.findViewById(R.id.detailsRecycleView);
            if(weatherList.get(i).getResult().getRealtime().getInfo().contains("mai")){
                imageView.setImageResource(R.mipmap.mai);
            }
            else if(weatherList.get(i).getResult().getRealtime().getInfo().contains("阴")||weatherList.get(i).getResult().getRealtime().getInfo().contains("云")){
                imageView.setImageResource(R.mipmap.cloudy);
            }
            else if(weatherList.get(i).getResult().getRealtime().getInfo().contains("雨")){
                imageView.setImageResource(R.mipmap.rainy);
            }
            else if(weatherList.get(i).getResult().getRealtime().getInfo().contains("晴")){
                imageView.setImageResource(R.mipmap.sunny);
            }
            description.setText(weatherList.get(i).getResult().getRealtime().getInfo());
            temperature.setText(weatherList.get(i).getResult().getRealtime().getTemperature()+"℃");
            todayDetail.setText(weatherList.get(i).getResult().getRealtime().getString());
            //接下来是显示将来几天的天气
            List<Weather.ResultBean.FutureBean> detailsList=weatherList.get(i).getResult().getFuture();
            LinearLayoutManager resultLayoutManager=new LinearLayoutManager(getContext());
            detailsRecycleView.setLayoutManager(resultLayoutManager);
            OneDayAdapter moneyAdapter=new OneDayAdapter(detailsList);
            detailsRecycleView.setAdapter(moneyAdapter);
            //details.setText(weatherList.get(i).getResult().getFuture().toString());
            viewList.add(tabView);
        }
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return cityList.get(position);
            }
        });
        tabLayout.addOnTabSelectedListener(listener);

        return view;
    }
    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //选择的tab
            //Log.e("TT","onTabSelected:" + tab.getText().toString());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //离开的那个tab
            //Log.e("TT","onTabUnselected" + tab.getText().toString());
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //再次选择tab
            //Log.e("TT","onTabReselected" + tab.getText().toString());
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //初始化城市名称列表，为了view做准备
    private void initCityList(){
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
    //初始化数据库
    private  void initDatabase(){
        weatherDB = Room.databaseBuilder(getActivity(), WeatherDatabase.class, dataBaseName)
                .allowMainThreadQueries()
                .build();
    }
}