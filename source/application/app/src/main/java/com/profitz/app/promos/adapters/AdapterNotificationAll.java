package com.profitz.app.promos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.data.DataFavorite;
import com.profitz.app.promos.data.DataNotification;
import com.profitz.app.promos.IMethodCaller;
import com.profitz.app.promos.fragments.MyInformationsTab2Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.profitz.app.promos.PromosActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterNotificationAll extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    List<DataNotification> data= Collections.emptyList();
    DataFavorite current;
    private List<Promo> mPromos;
    int currentPos=0;
    View view;
    DataNotification mRecentlyDeletedItem;
    int user_id;
    private final IMethodCaller listener2;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterNotificationAll(Context context, List<DataNotification> data, IMethodCaller listener2, int user_id){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.listener2 = listener2;
        this.user_id=user_id;


    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.container_notification_all, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        DataNotification current=data.get(position);
        myHolder.textTitle.setText(current.notificationTitle);
        myHolder.textDescription.setText(current.notificationDescription);
        myHolder.textEarnN.setText(current.notificationEarn);
        myHolder.textEarnType.setText(current.notificationEarnType);
        String my_date = current.notificationDateAdd;
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date datee = format.parse(my_date);
            myHolder.textDateN.setText(PromosActivity.getCountdownText(context, datee));

        } catch (ParseException e) {
            e.printStackTrace();
        }




        String imgUrl = current.notificationImage;
        Picasso.with( context )
                .load( imgUrl )
                .error( context.getDrawable( R.mipmap.empty_view ) )
                .into( myHolder.ivNotification );
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public List<Promo> getList() {
        return mPromos;
    }
    private void showUndoSnackbar(int id, int user_id, int idPos) {
        Snackbar snackbar = Snackbar.make(view, "Powiadomienie zostało usunięte", Snackbar.LENGTH_LONG);
      //  Toast.makeText(context, id+"xxx"+idPos, Toast.LENGTH_SHORT).show();
        snackbar.setAction("Cofnij", v -> undoDelete(id, user_id, idPos));
        snackbar.show();
        //Toasty.success(context, "Powiadomienie zostało usunięte.").show();

    }

    private void undoDelete(int id, int user_id, int idPos) {

        final String url2 = "https://yoururl.com/api/update_notification.php?status=undo&user_id=" + user_id +"&id="+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_json = response.getString("status_json");
                            if (status_json.equals("0")) {
                                data.add(idPos,mRecentlyDeletedItem);
                                notifyItemInserted(idPos);
                                int size = data.size();
                                if (size > 0)
                                {
                                    MyInformationsTab2Fragment.oneOrMoreNotifications();
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
    public void deleteAllItems(int user_id_new){
        String urlAddress = "https://yoururl.com/api/update_notification.php?status=deleteall&user_id="+user_id_new;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, urlAddress, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_json = response.getString("status_json");
                            if (status_json.equals("0")) {
                                int size = data.size();
                                data.clear();
                                notifyItemRangeRemoved(0, size);
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

    public void deleteItem(int position) {
        mRecentlyDeletedItem = data.get(position);
        int mRecentlyDeletedItemPosition = position;
        //listener2.deleteItemDatabase(mRecentlyDeletedItem.notificationId);

        final String url2 = "https://yoururl.com/api/update_notification.php?status=delete&user_id=" + user_id +"&id="+mRecentlyDeletedItem.notificationId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status_json = response.getString("status_json");
                            if (status_json.equals("1")) {
                                data.remove(position);
                                notifyItemRemoved(position);
                                showUndoSnackbar(mRecentlyDeletedItem.notificationId, user_id, mRecentlyDeletedItemPosition);
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

        TextView textDescription;
        TextView textTitle;
        TextView textEarnN;
        TextView textEarnType;
        TextView textDateN;
        ImageView ivNotification;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textTitle= (TextView) itemView.findViewById(R.id.textTitle);
            textDescription= (TextView) itemView.findViewById(R.id.textDescription);
            textEarnN= (TextView) itemView.findViewById(R.id.textEarnN);
            textEarnType= (TextView) itemView.findViewById(R.id.textEarnType);
            textDateN= (TextView) itemView.findViewById(R.id.textDateN);

            ivNotification= (ImageView) itemView.findViewById(R.id.ivNotification);

        }

    }

}
