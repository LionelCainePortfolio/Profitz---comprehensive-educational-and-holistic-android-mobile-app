package com.profitz.app.promos.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.profitz.app.R;import com.profitz.app.data.model.MessagesModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.activities.GroupDetailActivity;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.MovableFloatingActionButton;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MessagesFragment extends Fragment {
    private View view;
    public ArrayList<MessagesModel> messagesModels;
    public RecyclerView rv;
    public MessagesAdapter messagesAdapter;
    private CustomLoadingDialog customLoadingDialog;
    private HttpManager httpManager;

    private TextView someoneTyping;
    private MovableFloatingActionButton btnAdd;
    private TextView noRecordFound;

    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messages_layout, container, false);
        messagesModels = new ArrayList<>();
        httpManager = new HttpManager(getActivity());

        SocketManager.getInstance();

        customLoadingDialog = new CustomLoadingDialog(getActivity());
        noRecordFound = view.findViewById(R.id.noRecordFound);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        noRecordFound.setVisibility(View.GONE);
        someoneTyping = view.findViewById(R.id.someoneTyping);
        someoneTyping.setVisibility(View.GONE);

        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(layoutManager);

        messagesAdapter = new MessagesAdapter(getActivity(), messagesModels, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = rv.getChildAdapterPosition(view);
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra("messageId", messagesModels.get(position).getId());
                Utility.gotoActivity((AppCompatActivity) getActivity(), intent);
            }
        });
        rv.setAdapter(messagesAdapter);

        customLoadingDialog.show();
        getAllMessages();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllMessages();
            }
        });
/*
        SocketManager.attachGroupDeletedListener(new SocketListener() {
            @Override
            public void onResponse(String response) {
                for (int a = 0; a < messagesModels.size(); a++) {
                    if (messagesModels.get(a).getId().equalsIgnoreCase(response)) {
                        messagesModels.remove(a);
                        break;
                    }
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messagesAdapter.setFilter(messagesModels);
                            if (messagesModels.size() == 0) {
                                noRecordFound.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

        SocketManager.attachGroupCreatedListener(new SocketListener() {
            @Override
            public void onResponse(String response) {
                MessagesModel messagesModel = new Gson().fromJson(response, MessagesModel.class);
                messagesModels.add(messagesModel);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messagesAdapter.setFilter(messagesModels);
                            noRecordFound.setVisibility(View.GONE);
                        }
                    });
                }
     httpManager       }
        });
*/
        return view;
    }

    private void getAllMessages() {
        if (getActivity() != null) {
            httpManager.getAllMessages(new HttpResponse() {
                @Override
                public void onSuccess(String response) {
                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<MessagesModel>>() {}.getType();
                        messagesModels = gson.fromJson(response, listType);
                        messagesAdapter.setFilter(messagesModels);

                        if (messagesModels.size() == 0) {
                            noRecordFound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    swipeRefresh.setRefreshing(false);
                    customLoadingDialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    Log.i("my_log", error);
                }
            });
        } else {
            customLoadingDialog.dismiss();
        }
    }

    public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
        private final ArrayList<MessagesModel> messages;
        private final Context context;
        private final View.OnClickListener onClickListener;

        public MessagesAdapter(Context context, ArrayList<MessagesModel> messages, View.OnClickListener onClickListener) {
            this.messages = messages;
            this.context = context;
            this.onClickListener = onClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_single_messager, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MessagesModel messagesModel = messages.get(position);
            holder.messageName.setText(messagesModel.getMessages_name());

                if (messagesModel.getNum_unread() > 0) {
                    holder.numUnRead.setText(String.valueOf(messagesModel.getNum_unread()));
                    holder.numUnRead.setVisibility(View.VISIBLE);
                } else {
                    holder.numUnRead.setVisibility(View.GONE);
                }


            if (Utility.isEmpty(messagesModel.getPicture())) {
                holder.messageImage.setVisibility(View.GONE);
            } else {
                Utility.showImage(context, messagesModel.getPicture(), holder.messageImage);
                //Utility.showImage(context, Utility.BASE_URL + "/" + messagesModel.getPicture(), holder.messageImage);
                holder.messageImage.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public void setFilter(ArrayList<MessagesModel> messages) {
            this.messages.clear();
            this.messages.addAll(messages);
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView messageName;
            private final TextView numUnRead;
            private final ImageView iconLock;
            private final ImageView messageImage;

            public ViewHolder(View itemView) {
                super(itemView);

                messageImage = itemView.findViewById(R.id.messageImage);
                messageName = itemView.findViewById(R.id.messageName);
                numUnRead = itemView.findViewById(R.id.numUnRead);
                iconLock = itemView.findViewById(R.id.iconLock);

                itemView.setOnClickListener(onClickListener);
            }
        }
    }
}
