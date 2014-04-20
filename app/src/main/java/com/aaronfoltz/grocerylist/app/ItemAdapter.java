package com.aaronfoltz.grocerylist.app;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private List<Item> mTasks;

    public ItemAdapter(final Context context, final List<Item> objects) {
        super(context, R.layout.row_item, objects);
        this.mContext = context;
        this.mTasks = objects;
    }

    public View getView(final int position, View convertView, final ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.row_item, null);
        }

        Item task = mTasks.get(position);

        TextView descriptionView = (TextView) convertView.findViewById(R.id.item_description);

        descriptionView.setText(task.getItemName());

        if(task.isRetrieved()){
            descriptionView.setPaintFlags(descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            descriptionView.setPaintFlags(descriptionView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        return convertView;
    }

}
