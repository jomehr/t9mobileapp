package com.matchfinder.jan.t9_mobileapp.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matchfinder.jan.t9_mobileapp.R;

/**
 * Created by tarekashi on 1/22/18.
 */

public class CustomDeveloperList extends ArrayAdapter<String> {

    private String [] developerName;
    private Integer [] imgid;
    private Activity context;

    public CustomDeveloperList(Activity context, String [] developerName, Integer [] imgid) {
        super(context, R.layout.item_developer, developerName);
        this.context = context;
        this.developerName = developerName;
        this.imgid = imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.item_developer, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
          viewHolder = (ViewHolder)r.getTag();
        }
        viewHolder.ivw.setImageResource(imgid[position]);
        viewHolder.tvw.setText(developerName[position]);
        return r;
    }

    class ViewHolder
    {
        TextView tvw;
        ImageView ivw;
        ViewHolder(View v)
        {
            tvw = v.findViewById(R.id.developer_name);
            ivw = v.findViewById(R.id.developer_pic);
        }
    }
}
