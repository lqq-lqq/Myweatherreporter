package com.example.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.pojo.Weather;
import com.example.weather.R;

import java.util.List;

public class OneDayAdapter extends RecyclerView.Adapter<OneDayAdapter.ViewHolder>{
    private List<Weather.ResultBean.FutureBean> weatherList;
    //private AdapterView.OnItemClickListener onItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView dayDetailTextView;
        View weatherView;
        public ViewHolder(View view){
            super(view);
            weatherView=view;
            dateTextView=view.findViewById(R.id.date);
            dayDetailTextView=view.findViewById(R.id.dayDetail);
        }
    }
    public OneDayAdapter(List<Weather.ResultBean.FutureBean> ml){
        weatherList=ml;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_day,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Weather.ResultBean.FutureBean thisWeather=weatherList.get(position);
        holder.dateTextView.setText(thisWeather.getDate());
        holder.dayDetailTextView.setText(thisWeather.getString());
    }
    @Override
    public int getItemCount(){
        return weatherList.size();
    }

}
