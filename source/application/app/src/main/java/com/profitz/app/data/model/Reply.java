package com.profitz.app.data.model;
public class Reply {

    private boolean isFavorite;
    public String replyId;
    public String replyImage;
    public String replyUserName;
    public String replyContent;
    public String replyDate;
    public String hasreply;
    public String replyUserNameFirstReply;
    public String replyContentFirstReply;
    public String likes;

    public String reply_to_id;
    public String reply_to_reply_id;
    public String reply_to_username;

    public Reply(String content, String uid, String uimg, String uname, String replyDate, String hasreplys, String replyUserNameFirstReplyString, String replyContentFirstReplyString, String likes,String reply_to_id, String reply_to_reply_id, String reply_to_username) {
        this.replyContent = content;
        this.replyId = uid;
        this.replyImage = uimg;
        this.replyUserName = uname;
        this.replyDate = replyDate;
        this.hasreply = hasreplys;
        this.replyUserNameFirstReply = replyUserNameFirstReplyString;
        this.replyContentFirstReply = replyContentFirstReplyString;
        this.likes = likes;
        this.reply_to_id = reply_to_id;
        this.reply_to_reply_id = reply_to_reply_id;
        this.reply_to_username = reply_to_username;

    }



    public String getContent() {
        return replyContent;
    }

    public void setContent(String content) {
        this.replyContent = content;
    }

    public String getUid() {
        return replyId;
    }

    public void setUid(String uid) {
        this.replyId = uid;
    }

    public String getUimg() {
        return replyImage;
    }

    public void setUimg(String uimg) {
        this.replyImage = uimg;
    }

    public  String getUname() {
        return replyUserName;
    }

    public String hasReply(){
        return hasreply;
    }

    public String getUnameFirstReply(){
        return replyUserNameFirstReply;
    }
    public String getFirstContentReply (){
        return replyContentFirstReply;
    }


    public void setUname(String uname) {
        this.replyUserName = uname;
    }


    public String getReplyDate() {
        return replyDate;
    }
    public String getLikes (){return likes;}
    public String getReplyToId(){return reply_to_id;}

    public String getReplyToReplyId (){
        return reply_to_reply_id;
    }

    public String getReplyToUsername(){
        return reply_to_username;
    }




}