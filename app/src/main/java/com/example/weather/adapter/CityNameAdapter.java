package com.example.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.interface1.OnItemClickListener;

import java.util.List;
//城市名字TextView的adapter
public class CityNameAdapter extends RecyclerView.Adapter<CityNameAdapter.ViewHolder>{
    private List<String> cityList;   //城市名称列表
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityText;
        public ViewHolder(View view){
            super(view);
            cityText=view.findViewById(R.id.one_city_name);
        }
    }
    public CityNameAdapter(List<String> cl){
        cityList=cl;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_city_name,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String city=cityList.get(position);
        holder.cityText.setText(city);
        if (mOnItemClickListener != null) {
            holder.cityText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }
    @Override
    public int getItemCount(){
        return cityList.size();
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
