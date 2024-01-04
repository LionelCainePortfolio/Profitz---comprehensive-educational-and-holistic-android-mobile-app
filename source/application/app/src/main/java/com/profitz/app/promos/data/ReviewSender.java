package com.profitz.app.promos.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.profitz.app.promos.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * 1.SEND DATA FROM EDITTEXT OVER THE NETWORK
 * 2.DO IT IN BACKGROUND THREAD
 * 3.READ RESPONSE FROM A SERVER
 */
public class ReviewSender extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAddress;

    String user_id;
    String promo_id;
    String promo_name;
    String promo_earn_points;
    String promo_stars;
    String description;
    ProgressDialog pd;

    /*
            1.OUR CONSTRUCTOR
    2.RECEIVE CONTEXT,URL ADDRESS AND EDITTEXTS FROM OUR MAINACTIVITY
    */
    public ReviewSender(Context c, String urlAddress, String...strings) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.user_id=strings[0];
        this.promo_id=strings[1];
        this.promo_name=strings[2];
        this.promo_earn_points=strings[3];
        this.promo_stars=strings[4];
        this.description=strings[5];
        //GET TEXTS FROM EDITEXTS
        //id=nameTxt.getText().toString();
    }
    /*
   1.SHOW PROGRESS DIALOG WHILE DOWNLOADING DATA
    */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

       // pd=new ProgressDialog(c);
       // pd.setTitle("Send");
       // pd.setMessage("Sending..Please wait");
        //pd.show();
    }

    /*
    1.WHERE WE SEND DATA TO NETWORK
    2.RETURNS FOR US A STRING
     */
    @Override
    protected String doInBackground(Void... params) {
        return this.send();
    }

    /*
  1. CALLED WHEN JOB IS OVER
  2. WE DISMISS OUR PD
  3.RECEIVE A STRING FROM DOINBACKGROUND
   */
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        //pd.dismiss();

        if(response != null)
        {
            //SUCCESS
           // Toast.makeText(c,response,Toast.LENGTH_LONG).show();

        }else
        {
            //NO SUCCESS
          //  Toast.makeText(c,"Unsuccessful "+response,Toast.LENGTH_LONG).show();
        }
    }

    /*
    SEND DATA OVER THE NETWORK
    RECEIVE AND RETURN A RESPONSE
     */
    private String send()
    {
        //CONNECT
        HttpURLConnection con= Connector.connect(urlAddress);

        if(con==null)
        {
            return null;
        }

        try
        {
            OutputStream os=con.getOutputStream();

            //WRITE
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            bw.write(new ReviewDataPackager(user_id, promo_id, promo_name, promo_earn_points, promo_stars,description).packData());
            bw.flush();

            //RELEASE RES
            bw.close();
            os.close();

            //HAS IT BEEN SUCCESSFUL?
            int responseCode=con.getResponseCode();

            if(responseCode== HttpURLConnection.HTTP_OK)
            {
                //GET EXACT RESPONSE
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response=new StringBuffer();

                String line;

                //READ LINE BY LINE
                while ((line=br.readLine()) != null)
                {
                    response.append(line);
                }

                //RELEASE RES
                br.close();

                return response.toString();

            }else
            {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}