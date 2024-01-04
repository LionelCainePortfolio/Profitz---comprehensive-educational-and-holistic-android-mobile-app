package com.profitz.app.promos.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
public class DoneSender extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAddress2;

    String user_id;
    String user_nickname;
    String user_email;
    String promo_id;
    String promo_name;
    String promo_email;
    String promo_type2;
    String promo_earn;
    String done_date;
    String promo_number_phone;
    String optional;

    ProgressDialog pd;

    /*
            1.OUR CONSTRUCTOR
    2.RECEIVE CONTEXT,URL ADDRESS AND EDITTEXTS FROM OUR MAINACTIVITY
    */
    public DoneSender(Context c, String urlAddress2, String...strings) {
        this.c = c;
        this.urlAddress2 = urlAddress2;
        this.user_id=strings[0];
        this.user_nickname=strings[1];
        this.user_email=strings[2];
        this.promo_id=strings[3];
        this.promo_name=strings[4];
        this.promo_email=strings[5];
        this.promo_type2=strings[6];
        this.promo_earn=strings[7];
        this.done_date=strings[8];
        this.promo_number_phone=strings[9];
        this.optional=strings[10];
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
          //  Toast.makeText(c,response,Toast.LENGTH_LONG).show();

        }else
        {
            //NO SUCCESS
           // Toast.makeText(c,"Unsuccessful "+response,Toast.LENGTH_LONG).show();
        }
    }

    /*
    SEND DATA OVER THE NETWORK
    RECEIVE AND RETURN A RESPONSE
     */
    private String send()
    {
        //CONNECT
        HttpURLConnection con= Connector.connect(urlAddress2);

        if(con==null)
        {
            return null;
        }

        try
        {
            OutputStream os=con.getOutputStream();

            //WRITE
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            bw.write(new DoneDataPackager(user_id, user_nickname, user_email, promo_id, promo_name, promo_email, promo_type2, promo_earn, done_date, promo_number_phone, optional).packData());
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