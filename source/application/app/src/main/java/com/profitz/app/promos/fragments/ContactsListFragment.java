package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.PrivateChatActivity;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.interfaces.StopHandlerMessages;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ContactsListFragment extends Fragment implements StopHandlerMessages {
    private View view;
    private ArrayList<User> userModels = new ArrayList<>();
    private HttpManager httpManager;
    private RecyclerView rv;
    private TextView noRecordFound;
    private CustomLoadingDialog customLoadingDialog;
    private MembersListAdapter membersListAdapter;
    private RelativeLayout main;
    private Context context;
    private User user;
    Handler handler;
    Runnable updateChats;
    private EditText search_edit_text;
    private TextWatcher textWatcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        userModels = new ArrayList<>();
        httpManager = new HttpManager(getActivity());
        user = MyPreferenceManager.getInstance(context).getUser();
        customLoadingDialog = new CustomLoadingDialog(getActivity());
        noRecordFound = view.findViewById(R.id.noRecordFound);
        main = view.findViewById(R.id.main);
        noRecordFound.setVisibility(View.GONE);
        membersListAdapter = new MembersListAdapter(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopHandler();
                int index = rv.getChildAdapterPosition(view);
                Intent intent = new Intent(getActivity(), PrivateChatActivity.class);
                intent.putExtra("userId", userModels.get(index).getId());
                Utility.gotoActivity((AppCompatActivity) getActivity(), intent);
            }
        });

        context = inflater.getContext();

        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(membersListAdapter);


        search_edit_text = view.findViewById(R.id.search_contact_messages_edit_text);

        getFriendsChat();
        startHandler();

        textWatcher=new TextWatcher() {
            String oldText = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                this.oldText = charSequence.toString();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_edit_text.removeTextChangedListener(textWatcher);
                membersListAdapter.getFilter().filter(charSequence.toString());
                search_edit_text.addTextChangedListener(textWatcher);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                stopHandler();

            }
        };
        search_edit_text.addTextChangedListener(textWatcher);
        return view;
    }

public void startHandler(){
    handler = new Handler();

    updateChats = new Runnable() {
        @Override
        public void run() {
            getFriendsChat();
            handler.postDelayed(updateChats, 3000); //100 ms you should do it 4000
        }
    };

    handler.postDelayed(updateChats, 100);
}
    public void stopHandler() {
        handler = new Handler();
        handler.removeCallbacksAndMessages(null);
    }
    private void getFriendsChat() {
        httpManager.getFriendsChat(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                    userModels = gson.fromJson(response, listType);
                    membersListAdapter.notifyDataSetChanged();

                    if (userModels.size() == 0) {
                        noRecordFound.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                customLoadingDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                Log.i("my_log", error);
            }
        });
    }
    public String parseDateToddMMyyyy(String time, int year) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "";

        if (year == 1){
            outputPattern = "dd MMM yyyy hh:mm";
        }
        else if (year == 0)
        {
            outputPattern = "dd MMM hh:mm";
        }
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

            } else if ((day >= 7) && (day <=365)) {

                convTime = parseDateToddMMyyyy(dataDate, 0);


            } else if (day > 365){

                convTime = parseDateToddMMyyyy(dataDate, 1);


            }
                else if (day < 7) {
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
    private class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.ViewHolder> {
        private final Context context;
        private final View.OnClickListener onUserSelected;
        private final ArrayList<User> dataListFull;

        MembersListAdapter(Context context, View.OnClickListener onUserSelected) {
            this.context = context;
            this.onUserSelected = onUserSelected;
            dataListFull = new ArrayList<>(userModels);

        }

        @NonNull
        @Override
        public MembersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_friends_list_single, parent, false);
            return new MembersListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MembersListAdapter.ViewHolder holder, final int position) {
            final User userModel = userModels.get(position);
            if (userModel != null) {
                Utility.toggleUserStatus(context, holder.onlineNotifier, userModel.isIs_online());
            holder.username.setText(userModel.getUsername());
            if ((userModel.getLastMsgSentByMe()!=null) && (userModel.getLastMsgSentByMe().equals("true"))){
                String last_msg = "Ty: "+userModel.getLastMsg();
                holder.last_msg.setText(last_msg);
            }
            else{
                holder.last_msg.setText(userModel.getLastMsg());
            }
                if (userModel.getLastMsgDate() != null && userModel.getLastMsgDate().length() > 0) {
                    String datetime = covertTimeToText(userModel.getLastMsgDate());
                    holder.last_msg_date.setText(datetime);
                }
            Utility.showImageAnimated(context, userModel.getPicture(), holder.image, holder.imageAnimated);
            Utility.toggleUserStatus(context, holder.onlineNotifier, userModel.isIs_online());
            if (userModel.getUnread_messages() > 0) {
                String unread_messages = String.valueOf(userModel.getUnread_messages());
                holder.unreadMessage.setText(unread_messages);
                holder.unreadMessage.setVisibility(View.VISIBLE);
                holder.username.setTypeface(null, Typeface.BOLD);
                holder.last_msg_date.setTypeface(null, Typeface.BOLD);
                holder.last_msg.setTypeface(null, Typeface.BOLD);
            } else {
                holder.unreadMessage.setVisibility(View.GONE);
            }
            }
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }
        public Filter getFilter() {
            return dataFilter;
        }
        private final Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<User> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(dataListFull);
                    startHandler();
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (User item : dataListFull) {
                        if (item.getUsername().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    userModels.clear();
                    userModels.addAll((ArrayList) filterResults.values);
                    notifyDataSetChanged();
                    noRecordFound.setVisibility(View.GONE);

                } else {
                    noRecordFound.setVisibility(View.VISIBLE);

                }

            }
        };
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView username, unreadMessage;
            ImageView image, imageAnimated;
            TextView last_msg, last_msg_date;
            View onlineNotifier;

            ViewHolder(View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.username);
                unreadMessage = itemView.findViewById(R.id.unreadMessage);
                image = itemView.findViewById(R.id.image);
                imageAnimated = itemView.findViewById(R.id.imageAnimated);
                onlineNotifier = itemView.findViewById(R.id.onlineNotifier);
                last_msg = itemView.findViewById(R.id.lastUnreadMessage);
                last_msg_date = itemView.findViewById(R.id.lastUnreadMessageDate);

                if (onUserSelected != null) {
                    itemView.setOnClickListener(onUserSelected);
                }
            }
        }
    }
}

