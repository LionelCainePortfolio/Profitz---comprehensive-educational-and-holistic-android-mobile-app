package com.profitz.app.promos.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.data.model.Reply;
import com.profitz.app.promos.fragments.BottomSheetDialogAddComment;
import com.profitz.app.promos.fragments.BottomSheetDialogDeleteCommentConfirm;
import com.profitz.app.promos.fragments.BottomSheetDialogReportComment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReply extends RecyclerView.Adapter<AdapterReply.ReplyViewHolder> {

    private final Context mContext;
    private final LayoutInflater inflater;
    private final FragmentManager fragmentManager;
    List<Reply> mData= Collections.emptyList();
    private final List<Reply> dataListFull;
    private ArrayList<String> replyList;
    private final int promoId;
    private final int airdropId;
    private final double user_id;
    private final String username;
    private final String comm_id;
    private final String isAirdrop;
    String url_like_dislike;
    public AdapterReply (Context mContext, List<Reply> mData, FragmentManager fragmentManager, int promoId, int airdropId, String isAirdrop, double user_id, String username, String comm_id) {
        this.mContext = mContext;
        inflater= LayoutInflater.from(mContext);
        this.mData = mData;
        this.fragmentManager = fragmentManager;
        dataListFull = new ArrayList<>(mData);
        this.promoId = promoId;
        this.airdropId = airdropId;
        this.isAirdrop = isAirdrop;
        this.user_id = user_id;
        this.username = username;
        this.comm_id = comm_id;

    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy hh:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "temu";

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date pasTime = dateFormat.parse(dataDate);


            Date nowTime = new Date();


            long dateDiff = nowTime.getTime() - pasTime.getTime();



            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                if (second <=10){
                    convTime = "kilka sekund  "+suffix;

                }
                else{
                    convTime = "chwilę "+suffix;

                }
            } else if (minute < 60) {
                if (minute == 1){
                    convTime = " minutę "+suffix;

                }
                else if ((minute >= 2 ) && (minute <=4)){
                    convTime = minute + " minuty "+suffix;

                }
                else if ((minute >= 22 ) && (minute <=24)){
                    convTime = minute + " minuty "+suffix;

                }
                else if ((minute >= 32 ) && (minute <=34)){
                    convTime = minute + " minuty "+suffix;

                }
                else if ((minute >= 42 ) && (minute <=44)){
                    convTime = minute + " minuty "+suffix;

                }
                else if ((minute >= 52 ) && (minute <=54)){
                    convTime = minute + " minuty "+suffix;

                }
                else{
                    convTime = minute + " minut "+suffix;

                }
            } else if (hour < 24) {
                if (hour == 1){
                    convTime = " godzinę "+suffix;

                }
                else if ((hour >= 2 ) && (hour <=4)){
                    convTime = hour + " godziny "+suffix;

                }
                else if ((hour >= 22 ) && (hour <= 23)){
                    convTime = hour + " godziny "+suffix;

                }
                else{
                    convTime = hour + " godzin "+suffix;

                }

            } else if (day >= 7) {




                convTime = parseDateToddMMyyyy(dataDate);
                Log.d("ddddd",convTime);


            } else if (day < 7) {
                if (day == 1)
                {
                    convTime = "dzień "+suffix;

                }
                else{
                    convTime = day+" dni "+suffix;

                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layout_reply, parent,false);

        return new ReplyViewHolder(view);

    }
    @Override
    public void onBindViewHolder(ReplyViewHolder holder, int fposition ) {
     //   String post=replyList.get(fposition).replace("\\n","\n");
       // holder.txtPost.setText(post);

        String imgUrl = mData.get(fposition).getUimg();
        Picasso.with( mContext )
                .load( imgUrl )
                .placeholder( mContext.getDrawable(  R.drawable.default_avatar ) )
                .error( mContext.getDrawable( R.drawable.default_avatar ) )
                .into( holder.reply_user_img );

        //  Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.img_user);
        holder.tv_name.setText(mData.get(fposition).getUname());

        if (mData.get(fposition).getReplyToReplyId().equals("0")){
            holder.tv_content.setText(mData.get(fposition).getContent());
        }
        else{
            Spanned content_add_username = Html.fromHtml("<font color=\"#2E51A7\">@"+mData.get(fposition).getReplyToUsername()+"</font><font color=\"#0B0B0B\"> "+mData.get(fposition).getContent()+"</font>");
            holder.tv_content.setText(content_add_username);

        }       // holder.tv_date.setText(timestampToString((Long)mData.get(position).getTimestamp()));
        String datetime = covertTimeToText(mData.get(fposition).getReplyDate());

        holder.tv_date.setText(datetime);

        String url_check_like_dislike = "https://yoururl.com/api/check_comment_like_dislike.php?comment_id="+mData.get(fposition).getUid()+"&user_id="+user_id;

        JsonObjectRequest jsonObjectRequest21 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url_check_like_dislike, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String response2 = response.getString("response");

                            if (response2.equals("0")) {
                                holder.like_full.setVisibility(View.GONE);
                                holder.like_outline.setVisibility(View.VISIBLE);

                            } else if (response2.equals("1"))
                            {
                                holder.like_outline.setVisibility(View.GONE);
                                holder.like_full.setVisibility(View.VISIBLE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest21);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_like_dislike = "https://yoururl.com/api/update_comment_like_dislike.php?is_airdrop=1&airdrop_id="+airdropId+"&comment_id="+mData.get(fposition).getUid()+"&user_id="+user_id;

                }
                else{
                    url_like_dislike = "https://yoururl.com/api/update_comment_like_dislike.php?is_airdrop=0&promo_id="+promoId+"&comment_id="+mData.get(fposition).getUid()+"&user_id="+user_id;

                }

                JsonObjectRequest jsonObjectRequest22 = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_like_dislike, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String response2 = response.getString("response");
                                    String likes = response.getString("likes");

                                    if (response2.equals("0")) {
                                        // String likes = mData.get(fposition).getLikes();

                                        int new_likes = Integer.parseInt(likes.trim())-1;
                                        String new_likes_string;

                                        if (new_likes == 1){
                                            new_likes_string = new_likes + " polubienie";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else if (new_likes == 2){
                                            new_likes_string = new_likes + " polubienia";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else if (new_likes == 3){
                                            new_likes_string = new_likes + " polubienia";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else if (new_likes == 4){
                                            new_likes_string = new_likes + " polubienia";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else {
                                            new_likes_string = new_likes + " polubień";
                                            holder.count.setText(new_likes_string);
                                        }


                                        holder.like_full.setVisibility(View.GONE);
                                        holder.like_outline.setVisibility(View.VISIBLE);

                                    } else if (response2.equals("1"))
                                    {
                                        holder.like_outline.setVisibility(View.GONE);
                                        holder.like_full.setVisibility(View.VISIBLE);
                                        // String likes = mData.get(fposition).getLikes();
                                        int new_likes = Integer.parseInt(likes.trim())+1;
                                        String new_likes_string;

                                        if (new_likes == 1){
                                            new_likes_string = new_likes + " polubienie";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else if (new_likes == 2){
                                            new_likes_string = new_likes + " polubienia";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else if (new_likes == 3){
                                            new_likes_string = new_likes + " polubienia";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else if (new_likes == 4){
                                            new_likes_string = new_likes + " polubienia";
                                            holder.count.setText(new_likes_string);
                                        }
                                        else {
                                            new_likes_string = new_likes + " polubień";
                                            holder.count.setText(new_likes_string);
                                        }



                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
                Volley.newRequestQueue(mContext).add(jsonObjectRequest22);






            }
        });
        String likes = String.valueOf(mData.get(fposition).getLikes());

        if (likes.equals("1")){
            String new_likes_string = likes + " polubienie";
            holder.count.setText(new_likes_string);
        }
        else if (likes.equals("2")){
            String new_likes_string = likes + " polubienia";
            holder.count.setText(new_likes_string);
        }
        else if (likes.equals("3")){
            String new_likes_string = likes + " polubienia";
            holder.count.setText(new_likes_string);
        }
        else if (likes.equals("4")){
            String new_likes_string = likes + " polubienia";
            holder.count.setText(new_likes_string);
        }
        else {
            String new_likes_string = likes + " polubień";
            holder.count.setText(new_likes_string);
        }
        if (mData.get(fposition).getUname().equals(username)){
            holder.delete_comment.setVisibility(View.VISIBLE);
            holder.edit_comment.setVisibility(View.VISIBLE);
            holder.report_comment.setVisibility(View.GONE);

            holder.delete_comment.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();

                    BottomSheetDialogDeleteCommentConfirm bottomSheetComment = new BottomSheetDialogDeleteCommentConfirm();
                    args.putString("commentId",mData.get(fposition).getUid());
                    args.putString("promoId",String.valueOf(promoId));
                    args.putString("airdropId",String.valueOf(airdropId));
                    args.putString("isAirdrop",isAirdrop);
                    args.putString("username",username);
                    args.putString("userid", String.valueOf(user_id));
                    args.putString("content",mData.get(fposition).getContent());
                    args.putString("isreply","yes");

                    bottomSheetComment.setArguments(args);
                    bottomSheetComment.show(fragmentManager, bottomSheetComment.getTag());





                }
            });
            holder.edit_comment.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();

                    BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                    args.putString("commentId",mData.get(fposition).getUid());
                    args.putString("userId", String.valueOf(user_id));
                    args.putString("promoId",String.valueOf(promoId));
                    args.putString("airdropId",String.valueOf(airdropId));
                    args.putString("isAirdrop",isAirdrop);
                    args.putString("username",username);
                    args.putString("is_reply","yes");
                    args.putString("global_comm","no");
                    args.putString("reply_username",mData.get(fposition).getUname());
                    args.putString("reply_username_true","true");
                    args.putString("replyId",mData.get(fposition).getUid());
                    args.putString("comment_action","edit");
                    args.putString("comment_content",mData.get(fposition).getContent());

                    bottomSheetComment.setArguments(args);
                    bottomSheetComment.show(fragmentManager, bottomSheetComment.getTag());





                }
            });

        }
        else{
            holder.delete_comment.setVisibility(View.GONE);
            holder.edit_comment.setVisibility(View.GONE);
            holder.report_comment.setVisibility(View.VISIBLE);
            holder.report_comment.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();

                    BottomSheetDialogReportComment bottomSheetReportComment = new BottomSheetDialogReportComment();
                    args.putString("commentId",mData.get(fposition).getUid());
                    args.putString("userId", String.valueOf(user_id));
                    args.putString("airdropId",String.valueOf(airdropId));
                    args.putString("isAirdrop",isAirdrop);
                    args.putString("promoId",String.valueOf(promoId));
                    args.putString("username",username);

                    bottomSheetReportComment.setArguments(args);
                    bottomSheetReportComment.show(fragmentManager, bottomSheetReportComment.getTag());





                }
            });




        }

        holder.reply.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();

                BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                args.putString("commentId",comm_id);
                args.putString("userId", String.valueOf(user_id));
                args.putString("promoId",String.valueOf(promoId));
                args.putString("airdropId",String.valueOf(airdropId));
                args.putString("isAirdrop",isAirdrop);
                args.putString("username",username);
                args.putString("is_reply","yes");
                args.putString("global_comm","no");
                args.putString("reply_username",mData.get(fposition).getUname());
                args.putString("reply_username_true","true");
                args.putString("replyId",mData.get(fposition).getUid());
                args.putString("comment_action","add");
                args.putString("comment_content",mData.get(fposition).getContent());
                bottomSheetComment.setArguments(args);
                bottomSheetComment.show(fragmentManager, bottomSheetComment.getTag());


            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_user,first_reply_img;
        TextView tv_name,tv_content,tv_date,first_reply_username, first_reply_content, response, count, reply;
        RelativeLayout replies;
        View line2;
        ImageView list_item_genre_arrow;
        RelativeLayout like;
        ImageView like_outline;
        ImageView like_full;
        TextView delete_comment;
        TextView edit_comment;
        TextView report_comment;
        CircleImageView reply_user_img;
        public ReplyViewHolder(View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.comment_user_img);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
            first_reply_img = itemView.findViewById(R.id.first_reply_user_img);
            first_reply_username = itemView.findViewById(R.id.first_reply_username);
            first_reply_content = itemView.findViewById(R.id.first_reply_content);
            replies = itemView.findViewById(R.id.replies);
            line2 = itemView.findViewById(R.id.line2);
            response = itemView.findViewById(R.id.response);
            list_item_genre_arrow = itemView.findViewById(R.id.list_item_genre_arrow);
            like = itemView.findViewById(R.id.like);
            like_full = itemView.findViewById(R.id.like_full);
            like_outline = itemView.findViewById(R.id.like_outline);
            count = itemView.findViewById(R.id.count);
            reply = itemView.findViewById(R.id.reply);
            delete_comment = itemView.findViewById(R.id.delete);
            edit_comment = itemView.findViewById(R.id.edit);
            report_comment = itemView.findViewById(R.id.report);
            reply_user_img = itemView.findViewById(R.id.reply_user_img);
            //   childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
        }
    }




    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;


    }


}
