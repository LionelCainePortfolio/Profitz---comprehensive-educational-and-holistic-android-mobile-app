package com.profitz.app.data.model;


public class Comment {

    public String commentId;
    public String commentImage;
    public String commentUserName;
    public String commentContent;
    public String commentDate;
    public String hasreply;
    public String commentUserNameFirstReply;
    public String commentContentFirstReply;
    public String likes;
    public String reply_to_id;
    public String reply_to_reply_id;
    public String reply_to_username;


    public Comment(String content, String uid, String uimg, String uname, String commentDate, String hasreplys, String commentUserNameFirstReplyString, String commentContentFirstReplyString, String likes, String reply_to_id, String reply_to_reply_id, String reply_to_username) {
        this.commentContent = content;
        this.commentId = uid;
        this.commentImage = uimg;
        this.commentUserName = uname;
        this.commentDate = commentDate;
        this.hasreply = hasreplys;
        this.commentUserNameFirstReply = commentUserNameFirstReplyString;
        this.commentContentFirstReply = commentContentFirstReplyString;
        this.likes = likes;
        this.reply_to_id = reply_to_id;
        this.reply_to_reply_id = reply_to_reply_id;
        this.reply_to_username = reply_to_username;
    }

    public String getContent() {
        return commentContent;
    }


    public String getUid() {
        return commentId;
    }


    public String getUimg() {
        return commentImage;
    }


    public String getUname() {
        return commentUserName;
    }

    public String hasReply(){
        return hasreply;
    }

    public String getUnameFirstReply(){
        return commentUserNameFirstReply;
    }
    public String getFirstContentReply (){
        return commentContentFirstReply;
    }



    public String getCommentDate() {
        return commentDate;
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
