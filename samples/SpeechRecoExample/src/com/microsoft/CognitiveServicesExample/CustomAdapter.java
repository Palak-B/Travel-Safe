package com.microsoft.CognitiveServicesExample;

/**
 * Created by rishavg on 2/22/18.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by rishavg on 6/8/17.
 */

public class CustomAdapter extends BaseAdapter {

    String name[];
    String number[];
    LayoutInflater inflater;
    Context context;

    CustomAdapter(Context context, String number[], String name[])
    {
        this.context=context;
        this.name=name;
        this.number=number;
        inflater= LayoutInflater.from(context);//*************
    }
    @Override
    public int getCount() {
        return number.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.custom_layout,null);
        TextView tv=(TextView)convertView.findViewById(R.id.name);
        TextView tv1=(TextView)convertView.findViewById(R.id.number);
        tv.setText(name[position]);
        tv1.setText(number[position]);
        return convertView;
    }
}
