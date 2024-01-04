package com.profitz.app.promos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.profitz.app.R;import com.profitz.app.promos.adapters.ECCardContentListItemAdapterPro;
import com.profitz.app.promos.pojo.CommentCard;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.profitz.app.ProfitzApp.getContext;

@SuppressLint({"SetTextI18n", "InflateParams"})
public class CommentCardArrayAdapter extends ECCardContentListItemAdapterPro<CommentCard> {
String level;
int position_new;
    public CommentCardArrayAdapter(@NonNull Context context, @NonNull List<CommentCard> objects, @NonNull String user_level, @NonNull int position_neww) {
        super(context, R.layout.list_element, objects);
        level = user_level;
        position_new = position_neww;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            rowView = inflater.inflate(R.layout.list_element, null);

            // configure view holder
            viewHolder = new ViewHolder();

            if (level.equals("0")){
                if (position_new == 0){
                    RelativeLayout level_zero_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_zero_partner);
                    level_zero_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 1){
                    RelativeLayout level_one_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_one_partner);
                    level_one_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 2){
                    RelativeLayout level_two_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_two_partner);
                    level_two_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 3){
                    RelativeLayout level_three_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_three_partner);
                    level_three_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 4){
                    RelativeLayout level_four_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_four_partner);
                    level_four_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 5){
                    RelativeLayout level_five_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_five_partner);
                    level_five_partner.setVisibility(View.VISIBLE);
                }
            }
            else if (level.equals("1")){
                if (position_new == 0){
                    RelativeLayout level_one_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_one_partner);
                    level_one_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 1){
                    RelativeLayout level_two_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_two_partner);
                    level_two_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 2){
                    RelativeLayout level_three_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_three_partner);
                    level_three_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 3){
                    RelativeLayout level_four_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_four_partner);
                    level_four_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 4){
                    RelativeLayout level_five_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_five_partner);
                    level_five_partner.setVisibility(View.VISIBLE);
                }
            }
            else if (level.equals("2")){
                if (position_new == 0){
                    RelativeLayout level_two_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_two_partner);
                    level_two_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 1){
                    RelativeLayout level_three_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_three_partner);
                    level_three_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 2){
                    RelativeLayout level_four_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_four_partner);
                    level_four_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 3){
                    RelativeLayout level_five_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_five_partner);
                    level_five_partner.setVisibility(View.VISIBLE);
                }
            }
            else if (level.equals("3")){
                if (position_new == 0){
                    RelativeLayout level_three_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_three_partner);
                    level_three_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 1){
                    RelativeLayout level_four_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_four_partner);
                    level_four_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 2){
                    RelativeLayout level_five_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_five_partner);
                    level_five_partner.setVisibility(View.VISIBLE);
                }
            }
            else if (level.equals("4")){
                if (position_new == 0){
                    RelativeLayout level_four_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_four_partner);
                    level_four_partner.setVisibility(View.VISIBLE);
                }
                else if (position_new == 1){
                    RelativeLayout level_five_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_five_partner);
                    level_five_partner.setVisibility(View.VISIBLE);
                }
            }
            else if (level.equals("5")){
               if (position_new == 0){
                   RelativeLayout level_five_partner = viewHolder.line1 = (RelativeLayout) rowView.findViewById(R.id.level_five_partner);
                   level_five_partner.setVisibility(View.VISIBLE);
               }
            }


            //viewHolder.line1 = (TextView) rowView.findViewById(R.id.firstLine);
            //viewHolder.line2 = (TextView) rowView.findViewById(R.id.secondLine);
           // viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final CommentCard objectItem = getItem(position);
        if (objectItem != null) {
            //viewHolder.line1.setText(objectItem.getCommentPersonName() + ":");
            //viewHolder.line2.setText(objectItem.getCommentText());
            //viewHolder.icon.setImageResource(objectItem.getCommentPersonPictureRes());
        }



        return rowView;
    }

    static class ViewHolder {
        TextView date;
        RelativeLayout line1;
        TextView line2;
        ImageView icon;
    }

}
