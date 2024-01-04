package com.profitz.app.promos;


import android.os.Parcel;
import android.os.Parcelable;

public class User {
    private String name;
    private String lastname;
    private final String username;
    private String email;
    private int id;
    private int favourite_count;
    private String phone;
    private String fcm_token;
    private String image_url;
    private boolean is_online;
    private boolean is_ban;
    private String last_scene;
    private int unread_messages;
    private String last_msg;
    private String country;
    private String last_msg_date;
    private String last_msg_sent_by_me;
    private int done_count;
    private double points;
    private double earned;
    private int power_level;
    private int premium;
   // public User(int id, String username, String email, int favourite_count, int done_count) {
         public User(int id, String name, String lastname, String username, String email, int favourite_count, int done_count, double points, double earned, int power_level, int premium, boolean is_ban, boolean is_online, int unread_messages, String phone, String country, String fcm_token, String last_scene, String image_url, String last_msg, String last_msg_date, String last_msg_sent_by_me) {
        this.id = id;
        this.name = name;
             this.lastname = lastname;
             this.username = username;
        this.email = email;
        this.favourite_count = favourite_count;
        this.done_count = done_count;
        this.points = points;
        this.earned = earned;
        this.power_level = power_level;
        this.premium = premium;
        this.is_ban = is_ban;
        this.is_online = is_online;
        this.unread_messages = unread_messages;
        this.phone = phone;
        this.country = country;
        this.fcm_token = fcm_token;
        this.last_scene = last_scene;
        this.image_url = image_url;
        this.last_msg = last_msg;
        this.last_msg_date = last_msg_date;
        this.last_msg_sent_by_me=last_msg_sent_by_me;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getName(){return name;}
    public String getLastname(){return lastname;}

    public String getId() {
        return String.valueOf(id);
    }
    public int getFavourite_count() {return favourite_count;}
    public int getDone_count(){return done_count;}
    public double getPoints(){return points;}
    public double getEarned(){return earned;}
    public int getPower_level(){return power_level;}
    public int getPremium(){return premium;}
    public int getUnread_messages() {
        return unread_messages;
    }
    public String getLastMsgSentByMe() {
        return last_msg_sent_by_me;
    }

    public void setUnread_messages(int unread_messages) {
        this.unread_messages = unread_messages;
    }


    public void setLastMsg(String last_msg) {
        this.last_msg = last_msg;
    }
    public void setLastMsgDate(String last_msg_date) {
        this.last_msg_date = last_msg_date;
    }

    public String getLastMsg() {
        return last_msg;
    }

    public String getLastMsgDate() {
        return last_msg_date;
    }



    public boolean isIs_ban() {
        return is_ban;
    }

    public void setIs_ban(boolean is_ban) {
        this.is_ban = is_ban;
    }

    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        username = in.readString();
    }
    public String getPhone() {
        return phone;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }
    public String getFcm_token() {
        return fcm_token;
    }

    public String getIdd(){ return String.valueOf(id);}
    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getPicture() {
        return image_url;
    }

    public void setPicture(String image_url) {
        this.image_url = image_url;
    }

    public boolean isIs_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }
    public void  setLastMsgSentByMe(String last_msg_sent_by_me) {
        this.last_msg_sent_by_me = last_msg_sent_by_me;
    }
    public String getLast_scene() {
        return last_scene;
    }

    public void setLast_scene(String last_scene) {
        this.last_scene = last_scene;
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", last_scene='" + last_scene + '\'' +
                '}';
    }



}
