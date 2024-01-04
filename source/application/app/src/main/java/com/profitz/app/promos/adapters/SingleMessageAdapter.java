package com.profitz.app.promos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PictureMessageDialog;
import com.profitz.app.promos.User;
import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joooonho.SelectableRoundedImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class SingleMessageAdapter extends RecyclerView.Adapter<SingleMessageAdapter.ViewHolder> {
    private final ArrayList<MessageModel> messageModels;
    private final Context context;
    private final MyPreferenceManager preferenceManager;
    private View view;
    boolean isSelected;
    private final Dialog imageDetailDialog;
    private final String myId;
    Date nowTime;
    private final DownloadAttachmentListener downloadAttachmentListener;
    private final View.OnClickListener getMessageDetail;
    private Date createdDate;
    private Date currentDate;
    private onItemClickListener mItemClickListener;
    int selecteditem =0;

    private boolean haveIBeenClicked, haveIBeenClickedOther; //false by default


    public SingleMessageAdapter(ArrayList<MessageModel> messageModels, Context context, DownloadAttachmentListener downloadAttachmentListener, View.OnClickListener getMessageDetail) {
        this.messageModels = messageModels;
        this.context = context;
        this.downloadAttachmentListener = downloadAttachmentListener;
        this.getMessageDetail = getMessageDetail;

        isSelected = false;
        preferenceManager = new MyPreferenceManager(context);
        myId = String.valueOf(preferenceManager.getUser().getId());
        imageDetailDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Window window = imageDetailDialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        imageDetailDialog.setContentView(R.layout.layout_image_detail);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_single_message_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MessageModel messageModel = messageModels.get(position);
        User sendFrom = messageModel.getSend_from();
        String isManyMessagesInOneMin = messageModel.getManyMessagesInOneMin();



        String created_message_date = messageModel.getCreated();
/*
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date pasTime = dateFormat.parse(created_message_date);


            Date nowTime = new Date();
            long dateDiff = nowTime.getTime() - pasTime.getTime();
            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);


            if (second < 60) {
                isManyMessagesInOneMin = "true";

            }
            else{
                isManyMessagesInOneMin = "false";
            }

            Log.d("d", "last: "+String.valueOf(second));

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }


        */
        boolean isSentByMe = sendFrom.getIdd().equalsIgnoreCase(myId);

        String messageContent = messageModel.getContent();
        String datetime = covertTimeToText(messageModel.getCreated());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf2;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentdate= null;
        try {
            currentdate = sdf.parse(messageModel.getCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf_day = new SimpleDateFormat("MMM");
        Date d = new Date();
        String dayOfTheWeek = sdf_day.format(d);
        String dayOfTheWeek_model = sdf_day.format(currentdate);

        if (dayOfTheWeek.equals(dayOfTheWeek_model))
        {
            sdf2=new SimpleDateFormat("HH:mm");
        }
        else{
            sdf2=new SimpleDateFormat("MMM d HH:mm");
        }
        if(selecteditem==position){
            holder.messageDate.setVisibility(View.VISIBLE);
            holder.layout_my_message_drawable.setBackground(null);
            holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last_clicked));
            holder.read_or_not.setVisibility(View.VISIBLE);
            if (messageModel.is_read()){
                holder.read_or_not.setText("Wiadomość wyświetlona.");
            }
            else{
                holder.read_or_not.setText("Wiadomość wysłana.");
            }        }
        else{
            holder.read_or_not.setVisibility(View.GONE);
            holder.messageDate.setVisibility(View.GONE);
            holder.layout_my_message_drawable.setBackground(null);
            holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last));

        }



            if ((isManyMessagesInOneMin!=null) && (isManyMessagesInOneMin.equals("true"))){
                if (sdf2.format(currentdate)!= null){
                    holder.messageDate.setVisibility(View.GONE);
                    if  ((messageModel.getManyMessagesInOneMinFirstMessage()!=null) && (messageModel.getManyMessagesInOneMinFirstMessage().equals("true"))){
                        holder.messageDate.setText(sdf2.format(currentdate));
                        holder.messageDate.setVisibility(View.VISIBLE);

                        holder.message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                haveIBeenClicked=!haveIBeenClicked;
                                selecteditem=position;

                                if (haveIBeenClicked){
                                    holder.layout_my_message_drawable.setBackground(null);
                                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last_clicked));
                                    holder.read_or_not.setVisibility(View.VISIBLE);
                                    holder.messageDate.setVisibility(View.VISIBLE);

                                    if (messageModel.is_read()){
                                        holder.read_or_not.setText("Wiadomość wyświetlona.");
                                    }
                                    else{
                                        holder.read_or_not.setText("Wiadomość wysłana.");
                                    }

                                }
                                else{

                                    holder.read_or_not.setVisibility(View.GONE);
                                    holder.messageDate.setVisibility(View.GONE);
                                    holder.layout_my_message_drawable.setBackground(null);
                                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last));

                                }
                                notifyDataSetChanged();

                            }
                        });
                        if (!isSentByMe) {

                            holder.otherSenderName.setVisibility(View.VISIBLE);
                            holder.otherSenderName.setText(sendFrom.getUsername());
                            holder.layout_other_message_drawable.setBackground(null);
                            holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_first));

                        }
                        else{

                            holder.layout_my_message_drawable.setBackground(null);
                            holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_first));

                        }
                    }
                    else if  ((messageModel.getManyMessagesInOneMinFirstMessage()!=null) && (messageModel.getManyMessagesInOneMinLastMessage().equals("true"))){
                        holder.messageDate.setText(sdf2.format(currentdate));
                        holder.messageDate.setVisibility(View.GONE);
                        holder.message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                haveIBeenClicked=!haveIBeenClicked;
                                selecteditem=position;

                                if (haveIBeenClicked){

                                    holder.layout_my_message_drawable.setBackground(null);
                                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last_clicked));
                                    holder.read_or_not.setVisibility(View.VISIBLE);
                                    holder.messageDate.setVisibility(View.VISIBLE);

                                    if (messageModel.is_read()){
                                        holder.read_or_not.setText("Wiadomość wyświetlona.");
                                    }
                                    else{
                                        holder.read_or_not.setText("Wiadomość wysłana.");
                                    }

                                }
                                else{

                                    holder.read_or_not.setVisibility(View.GONE);
                                    holder.messageDate.setVisibility(View.GONE);
                                    holder.layout_my_message_drawable.setBackground(null);
                                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last));

                                }
                                notifyDataSetChanged();

                            }
                        });
                        holder.otherMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                haveIBeenClickedOther=!haveIBeenClickedOther;
                                selecteditem=position;

                                if (haveIBeenClickedOther){

                                    holder.layout_other_message_drawable.setBackground(null);
                                    holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_last_clicked));
                                    holder.messageDate.setVisibility(View.VISIBLE);


                                }else{

                                    holder.messageDate.setVisibility(View.GONE);
                                    holder.layout_other_message_drawable.setBackground(null);
                                    holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_last));

                                }
                                notifyDataSetChanged();

                            }
                        });
                        final MessageModel messageModelprev = messageModels.get(position-1);
                        messageModelprev.setManyMessagesInOneMinLastMessage("false");
                        if (!isSentByMe) {
                            holder.layout_other_message_drawable.setBackground(null);
                            holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_last));
                        }
                        else{
                            holder.layout_my_message_drawable.setBackground(null);
                            holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_last));
                        }



                    }
                    else{
                        holder.messageDate.setText(sdf2.format(currentdate));
                        holder.messageDate.setVisibility(View.GONE);
                        holder.message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                haveIBeenClicked=!haveIBeenClicked;
                                selecteditem=position;


                                if (haveIBeenClicked){

                                    holder.layout_my_message_drawable.setBackground(null);
                                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_between_clicked));
                                    holder.messageDate.setVisibility(View.VISIBLE);

                                    holder.read_or_not.setVisibility(View.VISIBLE);
                                    if (messageModel.is_read()){
                                        holder.read_or_not.setText("Wiadomość wyświetlona.");
                                    }
                                    else{
                                        holder.read_or_not.setText("Wiadomość wysłana.");
                                    }


                                }
                                else{

                                    holder.read_or_not.setVisibility(View.GONE);
                                    holder.messageDate.setVisibility(View.GONE);
                                    holder.layout_my_message_drawable.setBackground(null);
                                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_between));

                                }
                                notifyDataSetChanged();


                            }
                        });

                        holder.otherMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                haveIBeenClickedOther=!haveIBeenClickedOther;
                                selecteditem=position;

                                if (haveIBeenClickedOther){

                                    holder.layout_other_message_drawable.setBackground(null);
                                    holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_between_clicked));
                                    holder.messageDate.setVisibility(View.VISIBLE);


                                }else{

                                    holder.messageDate.setVisibility(View.GONE);
                                    holder.layout_other_message_drawable.setBackground(null);
                                    holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_between));

                                }
                                notifyDataSetChanged();


                            }
                        });

                        if (!isSentByMe) {
                            holder.layout_other_message_drawable.setBackground(null);
                            holder.layout_other_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_gray_other_message_between));
                        }
                        else{
                            holder.layout_my_message_drawable.setBackground(null);
                            holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_between));

                        }
                    }

                }
            }
            else{
                if (sdf2.format(currentdate)!= null){
                holder.messageDate.setText(sdf2.format(currentdate));
                    holder.messageDate.setVisibility(View.VISIBLE);
                    holder.layout_my_message_drawable.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg_orange_my_message_first));

                }
            }


        if (isSentByMe) {

            if (messageModel.isSystemMessage() != null && messageModel.isSystemMessage().equalsIgnoreCase("1")) {
                holder.message.setVisibility(View.GONE);
                holder.system_message.setVisibility(View.VISIBLE);
                holder.system_message.setText(messageContent);
            }
            else{
                holder.system_message.setVisibility(View.GONE);
                //holder.message.setText(messageModel.getManyMessagesInOneMinFirstMessage()+" // "+messageModel.getManyMessagesInOneMinLastMessage());
                holder.message.setText(messageContent);
            }
            holder.senderName.setText(sendFrom.getUsername());

            if (Utility.isEmpty(messageModel.getAttachment())) {
                holder.attachment.setVisibility(View.GONE);
            } else {
                holder.attachment.setVisibility(View.VISIBLE);
                holder.attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PictureMessageDialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                                .setDownloadAttachmentListener(downloadAttachmentListener)
                                .setMessageModel(messageModel)
                                .show();
                    }
                });
//                Utility.showImage(context, Utility.BASE_URL + "/" + messageModel.getAttachment(), holder.attachment);

                String imagePath = Utility.BASE_URL + "/" + messageModel.getAttachment();
                if (!Utility.isEmpty(imagePath)) {

                    Glide.with(context)
                            .asBitmap()
                            .load(imagePath)
                            .placeholder(R.drawable.placeholder_logo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(holder.attachment);

                }
            }

            holder.myMessageLayout.setVisibility(View.VISIBLE);
            holder.otherMessageLayout.setVisibility(View.GONE);
        } else {
            if (messageModel.isSystemMessage() != null && messageModel.isSystemMessage().equalsIgnoreCase("1")) {
                holder.otherMessage.setVisibility(View.GONE);
                holder.system_message.setVisibility(View.VISIBLE);
                holder.system_message.setText(messageContent);
            }
            else{
                holder.system_message.setVisibility(View.GONE);
                holder.otherMessage.setText(messageContent);
            }
            if (Utility.isEmpty(messageModel.getContent())) {
                holder.otherMessage.setVisibility(View.GONE);
            }
            else{
                holder.otherMessage.setVisibility(View.VISIBLE);
            }
            if (Utility.isEmpty(messageModel.getAttachment())) {
                holder.otherAttachment.setVisibility(View.GONE);
            } else {
                holder.otherAttachment.setVisibility(View.VISIBLE);
                holder.otherAttachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PictureMessageDialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                                .setDownloadAttachmentListener(downloadAttachmentListener)
                                .setMessageModel(messageModel)
                                .show();
                    }
                });
//                Utility.showImage(context, Utility.BASE_URL + "/" + messageModel.getAttachment(), holder.otherAttachment);

                String imagePath = Utility.BASE_URL + "/" + messageModel.getAttachment();
                if (!Utility.isEmpty(imagePath)) {

                    Glide.with(context)
                            .asBitmap()
                            .load(imagePath)
                            .placeholder(R.drawable.placeholder_logo)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(holder.attachment);

                }
            }

            holder.myMessageLayout.setVisibility(View.GONE);
            holder.otherMessageLayout.setVisibility(View.VISIBLE);
        }
    }


    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position, ArrayList<MessageModel> messageModels);
    }
    public void changeImage(int index) {
        messageModels.get(index).setImageChanged(true);
        notifyItemChanged(index);
    }
    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public void setFilter(ArrayList<MessageModel> filter) {
        messageModels.clear();
        messageModels.addAll(filter);
        notifyDataSetChanged();
    }

    public void addItem(MessageModel item) {
        messageModels.add(item);
        notifyDataSetChanged();
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
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
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        private final TextView message;
        private TextView time;
        private final TextView senderName;
        private final TextView system_message;
        private final TextView messageDate;
        private TextView messageDateOther;
        private final View onlineNotifier;
        private final SelectableRoundedImageView attachment;
        private final LinearLayout main;
        private final RelativeLayout myMessageLayout;
        private final TextView read_or_not;
        private final RelativeLayout otherMessageLayout;
        private final TextView otherMessage;
        private final TextView otherSenderName;
        private final SelectableRoundedImageView otherAttachment;
        private final RelativeLayout layout_my_message_drawable;
        private final RelativeLayout layout_other_message_drawable;

        public ViewHolder(View itemView) {
            super(itemView);
            layout_my_message_drawable = itemView.findViewById(R.id.layout_my_message_drawable);
            layout_other_message_drawable = itemView.findViewById(R.id.layout_other_message_drawable);
            read_or_not = view.findViewById(R.id.read_or_not);
            myMessageLayout = itemView.findViewById(R.id.myMessageLayout);

            message = itemView.findViewById(R.id.message);
           // time = itemView.findViewById(R.id.time);
            onlineNotifier = itemView.findViewById(R.id.onlineNotifier);
            main = itemView.findViewById(R.id.main);
            senderName = itemView.findViewById(R.id.senderName);
            attachment = itemView.findViewById(R.id.attachment);
            system_message = itemView.findViewById(R.id.system_message);
            messageDate = itemView.findViewById(R.id.messageDate);
            otherMessageLayout = itemView.findViewById(R.id.otherMessageLayout);
            otherMessage = itemView.findViewById(R.id.otherMessage);
            otherSenderName = itemView.findViewById(R.id.otherSenderName);
            otherAttachment = itemView.findViewById(R.id.otherAttachment);


            if (getMessageDetail != null) {
                itemView.setOnClickListener(getMessageDetail);
            }

        }
        @Override
        public void onClick(View view) {



        }
    }
}
