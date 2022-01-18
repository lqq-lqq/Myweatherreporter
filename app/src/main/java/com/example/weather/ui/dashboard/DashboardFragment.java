package com.example.weather.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.example.weather.MainActivity;
import com.example.weather.R;
import com.example.weather.adapter.CityNameAdapter;
import com.example.weather.bean.WeatherBean;
import com.example.weather.database.WeatherDatabase;
import java.util.ArrayList;
import java.util.List;

import com.example.weather.interface1.OnItemClickListener;

//在这里填充“城市列表”，并可以进行城市的“增加”和“删除”功能
public class DashboardFragment extends Fragment {


    private RecyclerView cityRecycleView;
    private WeatherDatabase weatherDB;
    private String dataBaseName = "WeatherDatabase";
    //private ArrayList<View> cityViewList;
    private List<String> cityList;   //城市名称列表

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initDatabase();
        //城市列表的填充
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        cityRecycleView=view.findViewById(R.id.cityRecycleView);
        LinearLayoutManager cityLayoutManager=new LinearLayoutManager(getContext());
        cityLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cityRecycleView.setLayoutManager(cityLayoutManager);
        initCityList();
        System.out.println("hahah"+cityList);
        CityNameAdapter cityNameAdapter=new CityNameAdapter(cityList);
        cityRecycleView.setAdapter(cityNameAdapter);
        cityNameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String clickCity=cityList.get(position);
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(getActivity());
                normalDialog.setMessage("确认删除？");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                weatherDB.getWeatherDao().deleteSome(clickCity);
                                Toast.makeText(getActivity(), "已成功删除", Toast.LENGTH_SHORT).show();
                                //删除后必须重新显示页面
                                initCityList();
                                CityNameAdapter cityNameAdapter=new CityNameAdapter(cityList);
                                cityRecycleView.setAdapter(cityNameAdapter);
                                //System.out.println(cityList);
                                //moneyAdapter.notifyDataSetChanged();
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                // 显示
                normalDialog.show();
            }
        });
        //System.out.println("create something");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    //初始化城市名称列表，为了view做准备
    private void initCityList(){
        cityList=new ArrayList<>();
        List<WeatherBean> weatherBeanList=new ArrayList<>();
        weatherBeanList=weatherDB.getWeatherDao().getAllWeatherBean();
        for (int i=0;i< weatherBeanList.size();i++){
            if(!cityList.contains(weatherBeanList.get(i).getCity())){
                cityList.add(weatherBeanList.get(i).getCity());
            }
        }
    }
    //为了同步主页的天气显示
    public List<String> getCityList(){
        System.out.println("getCityList"+cityList);
        return cityList;
    }
    //初始化数据库
    private  void initDatabase(){
        weatherDB = Room.databaseBuilder(getActivity(), WeatherDatabase.class, dataBaseName)
                .allowMainThreadQueries()
                .build();
    }
}