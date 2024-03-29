package com.profitz.app.promos.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.profitz.app.R;import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private final Context context;
    // group titles
    private final List<String> listDataGroup;
    // child data
    private final HashMap<String, List<String>> listDataChild;
    public ExpandableListViewAdapter(Context context, List<String> listDataGroup,
                                     HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
    }
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition)).get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_child_help, null);
        }
        TextView textViewChild = convertView
                .findViewById(R.id.textViewChild);
        textViewChild.setText(childText);

        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_group_help, null);
        }
        TextView textViewGroup = convertView
                .findViewById(R.id.textViewGroup);
        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);

        String listTitle = (String) getGroup(groupPosition);
/*
        ImageView indicator = (ImageView) convertView.findViewById(R.id.groupIndicator);
        if (isExpanded) {
            indicator.setImageResource(R.drawable.arrowup);
        } else {
            indicator.setImageResource(R.drawable.arrowdown);
        } */

        ImageView image = (ImageView)convertView.findViewById(R.id.imageGroup);
        switch((String)getGroup(groupPosition))
        {
            case "Konto":
                image.setImageResource(context.getResources().getIdentifier("help_account", "drawable", context.getPackageName()));

                break;
            case "Premium":
                image.setImageResource(context.getResources().getIdentifier("help_premium", "drawable", context.getPackageName()));

                break;
            case "Zarabianie i refback":
                image.setImageResource(context.getResources().getIdentifier("help_earning", "drawable", context.getPackageName()));

                break;
            case "Promocje i licencje":
                image.setImageResource(context.getResources().getIdentifier("help_promo", "drawable", context.getPackageName()));

                break;
            case "Program partnerski i poleceni":
                image.setImageResource(context.getResources().getIdentifier("help_refferals", "drawable", context.getPackageName()));

                break;
            case "Punkty, nagrody i osiągnięcia":
                image.setImageResource(context.getResources().getIdentifier("help_awards", "drawable", context.getPackageName()));

                break;
            case "Płatności i transfery":
                image.setImageResource(context.getResources().getIdentifier("help_payments", "drawable", context.getPackageName()));

                break;
            case "Ustawienia aplikacji":
                image.setImageResource(context.getResources().getIdentifier("help_settings", "drawable", context.getPackageName()));

                break;
            case "Prywatność i bezpieczeństwo":
                image.setImageResource(context.getResources().getIdentifier("help_privacy_security", "drawable", context.getPackageName()));

                break;
            case "Opinia":
                image.setImageResource(context.getResources().getIdentifier("help_feedback", "drawable", context.getPackageName()));

                break;

        }




        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}