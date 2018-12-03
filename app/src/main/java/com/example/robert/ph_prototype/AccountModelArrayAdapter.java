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

public class AccountModelArrayAdapter extends ArrayAdapter<AccountModel> {
    private List<AccountModel> cardList = new ArrayList<AccountModel>();

    static class CardViewHolder {
        TextView name;
        TextView id;
        TextView email;
        TextView activities;
    }

    public AccountModelArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AccountModelArrayAdapter(Context context, int textViewResourceId,
                                    ArrayList<AccountModel> cardList) {
        super(context, textViewResourceId);
        this.cardList = cardList;
    }

    @Override
    public void add(AccountModel object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public AccountModel getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.account_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.name);
            viewHolder.id = (TextView) row.findViewById(R.id.id);
            viewHolder.email = (TextView) row.findViewById(R.id.email);
            viewHolder.activities = (TextView) row.findViewById(R.id.activities);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        AccountModel card = getItem(position);
        viewHolder.name.setText(card.getName());
        viewHolder.id.setText("ID: "+card.getId());
        viewHolder.email.setText("Email: "+card.getEmail());
        viewHolder.activities.setText("Activities: "+card.getActivities());

        // Coloring scheme
        if (card.getActivities() == null || card.getActivities().isEmpty()) {
            row.setBackground(ContextCompat.getDrawable(this.getContext(),
                    R.drawable.rounded_corners_dkgray));
        } else {
            row.setBackground(ContextCompat.getDrawable(this.getContext(),
                    R.drawable.rounded_corners_green));
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


}