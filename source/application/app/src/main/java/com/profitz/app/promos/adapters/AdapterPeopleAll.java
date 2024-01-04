package com.profitz.app.promos.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.IMethodCaller;
import com.profitz.app.promos.data.DataFavorite;
import com.profitz.app.promos.data.DataPeople;
import com.profitz.app.promos.fragments.BottomSheetDialogAdminUser;
import com.profitz.app.promos.fragments.MyInformationsTab2Fragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterPeopleAll extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final Context context;
    private final LayoutInflater inflater;
    List<DataPeople> data= Collections.emptyList();
    DataFavorite current;
    private List<Promo> mPromos;
    int currentPos=0;
    View view;
    DataPeople mRecentlyDeletedItem;
    int user_id;
    private final List<DataPeople> dataListFull;

    private final IMethodCaller listener2;
    private final FragmentManager fragmentManager;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterPeopleAll(Context context, List<DataPeople> data, IMethodCaller listener2, int user_id, FragmentManager fragmentManager){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.listener2 = listener2;
        this.user_id=user_id;
        this.fragmentManager = fragmentManager;
        dataListFull = new ArrayList<>(data);


    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.container_admin_people, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        DataPeople current=data.get(position);
        myHolder.textName.setText( current.peopleUserNickname);
        String imgUrl = current.peopleUserImageUrl;
        Picasso.with( context )
                .load( imgUrl )
                .placeholder( context.getDrawable( R.mipmap.ic_launcher_foreground ) )
                .error( context.getDrawable( R.mipmap.empty_view ) )
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into( myHolder.ivUser);
        Bundle args = new Bundle();

        myHolder.rl_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogAdminUser bottomSheet = new BottomSheetDialogAdminUser();
                args.putInt("peopleUserId", current.peopleUserId);
                args.putString("peopleUserName", current.peopleUserName);
                args.putString("peopleUserNickname", current.peopleUserNickname);
                args.putInt("peopleUserLevel", current.peopleUserLevel);
                args.putDouble("peopleUserPoints", current.peopleUserPoints);
                args.putDouble("peopleUserTotalEarn", current.peopleUserTotalEarn);
                args.putInt("peopleUserDones", current.peopleUserDones);
                args.putInt("peopleUserFavorites", current.peopleUserFavorites);
                args.putInt("peopleUserComments", current.peopleUserComments);
                args.putInt("peopleUserReviews", current.peopleUserReviews);
                args.putInt("peopleUserLicences", current.peopleUserLicences);
                args.putInt("peopleUserBoughts", current.peopleUserBoughts);
                args.putInt("peopleUserRefferals", current.peopleUserRefferalCount);
                args.putInt("peopleUserRefferalSuccessCount", current.peopleUserRefferalSuccessCount);
                args.putDouble("peopleUserReviews", current.peopleUserRefferalEarn);
                args.putInt("peopleUserActualDay", current.peopleUserActualDay);
                args.putString("peopleUserImageUrl", current.peopleUserImageUrl);
                args.putString("peopleUserRegisterDate", current.peopleUserRegisterDate);
                args.putString("peopleUserLastLogin", current.peopleUserLastLogin);
                bottomSheet.setArguments(args);
                bottomSheet.show(fragmentManager, bottomSheet.getTag());
            }
        });
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public List<Promo> getList() {
        return mPromos;
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }
    private final Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DataPeople> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(dataListFull);

            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DataPeople item : dataListFull){
                    if (item.peopleUserNickname.toLowerCase().contains(filterPattern)){
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
            data.clear();
            data.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public void banUser(int position) {
        mRecentlyDeletedItem = data.get(position);
        int mRecentlyDeletedItemPosition = position;
        //listener2.deleteItemDatabase(mRecentlyDeletedItem.notificationId);

        final String url2 = "https://yoururl.com/api/update_notification.php?status=delete&user_id=" + user_id +"&id="+mRecentlyDeletedItem.peopleUserId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_json = response.getString("status_json");
                            if (status_json.equals("1")) {
                                data.remove(position);
                                notifyItemRemoved(position);
                                int size = data.size();
                                if (size == 0)
                                {
                                    MyInformationsTab2Fragment.zeroNotifications();
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
        Volley.newRequestQueue(context).add(jsonObjectRequest);


    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        ImageView ivUser;
        RelativeLayout rl_user;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.textUserName);
            ivUser= (ImageView) itemView.findViewById(R.id.ivUser);
            rl_user=(RelativeLayout) itemView.findViewById(R.id.rl_user);
        }

    }

}
