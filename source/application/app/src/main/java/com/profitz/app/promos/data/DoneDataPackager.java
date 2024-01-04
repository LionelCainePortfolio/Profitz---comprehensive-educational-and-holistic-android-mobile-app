package com.profitz.app.promos.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * 1.BASICALLY PACKS DATA WE WANNA SEND
 */
public class DoneDataPackager {

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

    /*
    SECTION 1.RECEIVE ALL DATA WE WANNA SEND
     */
    public DoneDataPackager(String user_id, String user_nickname, String user_email, String promo_id, String promo_name, String promo_email, String promo_type2, String promo_earn, String done_date, String promo_number_phone, String optional) {
       this.user_id = user_id;
       this.user_nickname = user_nickname;
       this.user_email=user_email;
        this.promo_id = promo_id;
        this.promo_name = promo_name;
        this.promo_email = promo_email;
        this.promo_type2 = promo_type2;
        this.promo_earn = promo_earn;
        this.done_date = done_date;
        this.promo_number_phone = promo_number_phone;
        this.optional = optional;
    }
    public String getPromoDoneName() {
        return promo_name ;
    }
    /*
   SECTION 2
   1.PACK THEM INTO A JSON OBJECT
   1. READ ALL THIS DATA AND ENCODE IT INTO A FROMAT THAT CAN BE SENT VIA NETWORK
    */
    public String packData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer packedData=new StringBuffer();

        try
        {
            jo.put("user_id",user_id);
            jo.put("user_nickname",user_nickname);
            jo.put("user_email",user_email);
            jo.put("promo_id",promo_id);
            jo.put("promo_name",promo_name);
            jo.put("promo_email", promo_email);
            jo.put("promo_type2", promo_type2);
            jo.put("promo_earn", promo_earn);
            jo.put("done_date", done_date);
            jo.put("promo_number_phone", promo_number_phone);
            jo.put("optional", optional);

            Boolean firstValue=true;

            Iterator it=jo.keys();

            do {
                String key=it.next().toString();
                String value=jo.get(key).toString();

                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packedData.append("&");
                }

                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));

            }while (it.hasNext());

            return packedData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
