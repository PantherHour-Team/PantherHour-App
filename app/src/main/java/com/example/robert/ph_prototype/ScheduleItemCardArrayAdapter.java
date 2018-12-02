package com.example.robert.ph_prototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleItemCardArrayAdapter extends ArrayAdapter<ScheduleItemCard> {
    private static final String TAG = "ScheduleItemCardArrayAdapter";
    private List<ScheduleItemCard> cardList = new ArrayList<ScheduleItemCard>();

    static class CardViewHolder {
        TextView name;
        TextView time;
        TextView duration;
        TextView capacity;
    }

    public ScheduleItemCardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(ScheduleItemCard object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public ScheduleItemCard getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.schedule_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.name);
            viewHolder.time = (TextView) row.findViewById(R.id.time);
            viewHolder.duration = (TextView) row.findViewById(R.id.duration);
            viewHolder.capacity = (TextView) row.findViewById(R.id.capacity);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        ScheduleItemCard card = getItem(position);
        viewHolder.name.setText("Name: "+card.getName());
        viewHolder.time.setText("Time: "+card.getTime());
        viewHolder.duration.setText("Duration: "+card.getDuration());
        viewHolder.capacity.setText("Capacity: "+card.getCapacity());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void reset() {
        cardList.clear();
        notifyDataSetChanged();
    }
}