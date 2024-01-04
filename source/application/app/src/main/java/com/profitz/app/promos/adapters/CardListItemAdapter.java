package com.profitz.app.promos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.profitz.app.R;import java.util.List;

public class CardListItemAdapter extends ECCardContentListItemAdapterPro<String> {

    public CardListItemAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, R.layout.container_myinformations_partnership_level, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.container_myinformations_partnership_level, null);
            viewHolder = new ViewHolder();
           viewHolder.itemText = (TextView) rowView.findViewById(R.id.list_item_text);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String item = getItem(position);
        if (item != null) {
            viewHolder.itemText.setText(item);
        }
        return rowView;
    }


    static class ViewHolder {
        TextView itemText;
    }

}