package com.example.robert.ph_prototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ActivityModelArrayAdapter extends ArrayAdapter<ActivityModel> {
    private List<ActivityModel> cardList = new ArrayList<ActivityModel>();

    static class CardViewHolder {
        TextView name;
        TextView time;
        TextView room;
        TextView capacity;
    }

    public ActivityModelArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ActivityModelArrayAdapter(Context context, int textViewResourceId,
                                     ArrayList<ActivityModel> cardList) {
        super(context, textViewResourceId);
        this.cardList = cardList;
    }

    @Override
    public void add(ActivityModel object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public ActivityModel getItem(int index) {
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
            viewHolder.room = (TextView) row.findViewById(R.id.room);
            viewHolder.capacity = (TextView) row.findViewById(R.id.capacity);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        ActivityModel card = getItem(position);
        viewHolder.name.setText("Title: "+card.getName());
        viewHolder.time.setText("Time: "+card.getStartTime()+" - "+card.getEndTime());
        viewHolder.room.setText("Room: "+card.getRoom());
        viewHolder.capacity.setText("Capacity: "+card.getCapacity());

        // Coloring scheme
        switch (ActivityModel.Type.valueOf(card.getType())) {
            case COURSE_HELP:
                row.setBackground(ContextCompat.getDrawable(this.getContext(),
                        R.drawable.rounded_corners_dkgray));
                break;
            case SELF_GUIDED:
                row.setBackground(ContextCompat.getDrawable(this.getContext(),
                        R.drawable.rounded_corners_blue));
                break;
            case CLUB:
                row.setBackground(ContextCompat.getDrawable(this.getContext(),
                        R.drawable.rounded_corners_magenta));
                break;
        }

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void reset() {
        cardList.clear();
        notifyDataSetChanged();
    }

    /**
     *  Comparator for the adapter to sort each activity by its start time
     */
    public static Comparator<ActivityModel> getComparator() {
        return new Comparator<ActivityModel>() {
            @Override
            public int compare(ActivityModel a, ActivityModel b) {
                // Time Frame = "MM/DD/YYYY HH:MM:SS MM/DD/YYYY HH:MM:SS"
                return a.getStartTime().compareTo(b.getEndTime());
            }
        };
    }

}