package com.profitz.app.promos.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * 1.BASICALLY PACKS DATA WE WANNA SEND
 */
public class ReviewDataPackager {

    String user_id;
    String promo_id;
    String promo_name;
    String promo_earn_points;
    String promo_stars;
    String description;

    /*
    SECTION 1.RECEIVE ALL DATA WE WANNA SEND
     */
    public ReviewDataPackager(String user_id,String promo_id, String promo_name, String promo_earn_points, String promo_stars, String description) {
       this.user_id = user_id;
        this.promo_id = promo_id;
        this.promo_name = promo_name;
        this.promo_earn_points = promo_earn_points;
        this.promo_stars = promo_stars;
        this.description = description;
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
            jo.put("promo_id",promo_id);
            jo.put("promo_name",promo_name);
            jo.put("promo_earn_points", promo_earn_points);
            jo.put("promo_stars", promo_stars);
            jo.put("description", description);

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
