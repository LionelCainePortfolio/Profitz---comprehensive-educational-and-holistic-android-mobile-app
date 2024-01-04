package com.profitz.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Promo implements Parcelable{

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("difficult")
    @Expose
    private String difficult;

    @SerializedName("short_desc")
    @Expose
    private String short_desc;

    @SerializedName( "price" )
    @Expose
    private String price;

    @SerializedName( "PriceType" )
    @Expose
    private String PriceType;

    @SerializedName( "how_long" )
    @Expose
    private String how_long;

    @SerializedName( "tutorial" )
    @Expose
    private String tutorial;

    @SerializedName( "step1" )
    @Expose
    private String step1;

    @SerializedName( "step1_1" )
    @Expose
    private String step1_1;

    @SerializedName( "step1_2" )
    @Expose

    private String step1_2;

    @SerializedName( "step1_3" )
    @Expose
    private String step1_3;

    @SerializedName( "step1_4" )
    @Expose
    private String step1_4;

    @SerializedName( "step1_5" )
    @Expose
    private String step1_5;
    @SerializedName( "step1_6" )
    @Expose
    private String step1_6;

    @SerializedName( "step1_7" )
    @Expose
    private String step1_7;

    @SerializedName( "step2" )
    @Expose
    private String step2;

    @SerializedName( "step2_1" )
    @Expose
    private String step2_1;

    @SerializedName( "step2_2" )
    @Expose

    private String step2_2;

    @SerializedName( "step2_3" )
    @Expose
    private String step2_3;

    @SerializedName( "step2_4" )
    @Expose
    private String step2_4;

    @SerializedName( "step2_5" )
    @Expose
    private String step2_5;
    @SerializedName( "step2_6" )
    @Expose
    private String step2_6;

    @SerializedName( "step2_7" )
    @Expose
    private String step2_7;

    @SerializedName( "step3" )
    @Expose
    private String step3;

    @SerializedName( "step3_1" )
    @Expose
    private String step3_1;

    @SerializedName( "step3_2" )
    @Expose

    private String step3_2;

    @SerializedName( "step3_3" )
    @Expose
    private String step3_3;

    @SerializedName( "step3_4" )
    @Expose
    private String step3_4;

    @SerializedName( "step3_5" )
    @Expose
    private String step3_5;
    @SerializedName( "step3_6" )
    @Expose
    private String step3_6;

    @SerializedName( "step3_7" )
    @Expose
    private String step3_7;

    @SerializedName( "step4" )
    @Expose
    private String step4;
    @SerializedName( "step4_1" )
    @Expose
    private String step4_1;

    @SerializedName( "step4_2" )
    @Expose

    private String step4_2;

    @SerializedName( "step4_3" )
    @Expose
    private String step4_3;

    @SerializedName( "step4_4" )
    @Expose
    private String step4_4;

    @SerializedName( "step4_5" )
    @Expose
    private String step4_5;
    @SerializedName( "step4_6" )
    @Expose
    private String step4_6;

    @SerializedName( "step4_7" )
    @Expose
    private String step4_7;
    @SerializedName( "step5" )
    @Expose
    private String step5;
    @SerializedName( "step5_1" )
    @Expose
    private String step5_1;

    @SerializedName( "step5_2" )
    @Expose

    private String step5_2;

    @SerializedName( "step5_3" )
    @Expose
    private String step5_3;

    @SerializedName( "step5_4" )
    @Expose
    private String step5_4;

    @SerializedName( "step5_5" )
    @Expose
    private String step5_5;
    @SerializedName( "step5_6" )
    @Expose
    private String step5_6;

    @SerializedName( "step5_7" )
    @Expose
    private String step5_7;
    @SerializedName( "step6" )
    @Expose
    private String step6;
    @SerializedName( "step6_1" )
    @Expose
    private String step6_1;

    @SerializedName( "step6_2" )
    @Expose

    private String step6_2;

    @SerializedName( "step6_3" )
    @Expose
    private String step6_3;

    @SerializedName( "step6_4" )
    @Expose
    private String step6_4;

    @SerializedName( "step6_5" )
    @Expose
    private String step6_5;
    @SerializedName( "step6_6" )
    @Expose
    private String step6_6;

    @SerializedName( "step6_7" )
    @Expose
    private String step6_7;
    @SerializedName( "step7" )
    @Expose
    private String step7;
    @SerializedName( "step7_1" )
    @Expose
    private String step7_1;

    @SerializedName( "step7_2" )
    @Expose

    private String step7_2;

    @SerializedName( "step7_3" )
    @Expose
    private String step7_3;

    @SerializedName( "step7_4" )
    @Expose
    private String step7_4;

    @SerializedName( "step7_5" )
    @Expose
    private String step7_5;
    @SerializedName( "step7_6" )
    @Expose
    private String step7_6;

    @SerializedName( "step7_7" )
    @Expose
    private String step7_7;
    @SerializedName( "stepX" )
    @Expose
    private String stepX;

    @SerializedName( "steps" )
    @Expose
    private int steps;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("video")
    @Expose
    private boolean video;

    @SerializedName("vote_average")
    @Expose
    private float voteAverage;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("popularity")
    @Expose
    private int popularity;

    @SerializedName("xp")
    @Expose
    private int xp;

    @SerializedName("dones")
    @Expose
    private int dones;


    @SerializedName("promo_type")
    @Expose
    private String promo_type;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("likes")
    @Expose
    private int likes;

    @SerializedName("comments")
    @Expose
    private int comments;

    @SerializedName("reviews")
    @Expose
    private int reviews;

    @SerializedName("adult")
    @Expose
    private boolean adult;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("instruction_type")
    @Expose
    private String instruction_type;

    @SerializedName("promo_rules_url")
    @Expose
    private String promo_rules_url;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    private boolean favorite;

    public Promo() {
    }

    public Promo(int voteCount,
                 String difficult,
                 String short_desc,
                 String price,
                 String PriceType,
                 String how_long,
                 String tutorial,
                 String step1,
                 String step1_1,
                 String step1_2,
                 String step1_3,
                 String step1_4,
                 String step1_5,
                 String step1_6,
                 String step1_7,
                 String step2,
                 String step2_1,
                 String step2_2,
                 String step2_3,
                 String step2_4,
                 String step2_5,
                 String step2_6,
                 String step2_7,
                 String step3,
                 String step3_1,
                 String step3_2,
                 String step3_3,
                 String step3_4,
                 String step3_5,
                 String step3_6,
                 String step3_7,
                 String step4,
                 String step4_1,
                 String step4_2,
                 String step4_3,
                 String step4_4,
                 String step4_5,
                 String step4_6,
                 String step4_7,
                 String step5,
                 String step5_1,
                 String step5_2,
                 String step5_3,
                 String step5_4,
                 String step5_5,
                 String step5_6,
                 String step5_7,
                 String step6,
                 String step6_1,
                 String step6_2,
                 String step6_3,
                 String step6_4,
                 String step6_5,
                 String step6_6,
                 String step6_7,
                 String step7,
                 String step7_1,
                 String step7_2,
                 String step7_3,
                 String step7_4,
                 String step7_5,
                 String step7_6,
                 String step7_7,
                 String stepX,
                 int steps,
                 int id,
                 boolean video,
                 float voteAverage,
                 String title,
                 int popularity,
                 int xp,
                 int dones,
                 String promo_type,
                 String posterPath,
                 String originalLanguage,
                 String originalTitle,
                 String backdropPath,
                 int likes,
                 int comments,
                 int reviews,
                 boolean adult,
                 String overview,
                 String instruction_type,
                 String promo_rules_url,
                 String releaseDate,
                 boolean favorite) {
        this.voteCount = voteCount;
        this.difficult = difficult;
        this.short_desc = short_desc;
        this.price = price;
        this.PriceType = PriceType;
        this.how_long = how_long;
        this.tutorial = tutorial;
        this.step1 = step1;
        this.step1_1 = step1_1;
        this.step1_2 = step1_2;
        this.step1_3 = step1_3;
        this.step1_4 = step1_4;
        this.step1_5 = step1_5;
        this.step1_6 = step1_6;
        this.step1_7 = step1_7;
        this.step2 = step2;
        this.step2_1 = step2_1;
        this.step2_2 = step2_2;
        this.step2_3 = step2_3;
        this.step2_4 = step2_4;
        this.step2_5 = step2_5;
        this.step2_6 = step2_6;
        this.step2_7 = step2_7;
        this.step3 = step3;
        this.step3_1 = step3_1;
        this.step3_2 = step3_2;
        this.step3_3 = step3_3;
        this.step3_4 = step3_4;
        this.step3_5 = step3_5;
        this.step3_6 = step3_6;
        this.step3_7 = step3_7;
        this.step4 = step4;
        this.step4_1 = step4_1;
        this.step4_2 = step4_2;
        this.step4_3 = step4_3;
        this.step4_4 = step4_4;
        this.step4_5 = step4_5;
        this.step4_6 = step4_6;
        this.step4_7 = step4_7;
        this.step5 = step5;
        this.step5_1 = step5_1;
        this.step5_2 = step5_2;
        this.step5_3 = step5_3;
        this.step5_4 = step5_4;
        this.step5_5 = step5_5;
        this.step5_6 = step5_6;
        this.step5_7 = step5_7;
        this.step6 = step6;
        this.step6_1 = step6_1;
        this.step6_2 = step6_2;
        this.step6_3 = step6_3;
        this.step6_4 = step6_4;
        this.step6_5 = step6_5;
        this.step6_6 = step6_6;
        this.step6_7 = step6_7;
        this.step7 = step7;
        this.step7_1 = step7_1;
        this.step7_2 = step7_2;
        this.step7_3 = step7_3;
        this.step7_4 = step7_4;
        this.step7_5 = step7_5;
        this.step7_6 = step7_6;
        this.step7_7 = step7_7;
        this.stepX = stepX;
        this.steps = steps;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.xp = xp;
        this.dones = dones;
        this.promo_type = promo_type;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.likes = likes;
        this.comments = comments;
        this.reviews = reviews;
        this.adult = adult;
        this.overview = overview;
        this.instruction_type = instruction_type;
        this.promo_rules_url = promo_rules_url;
        this.releaseDate = releaseDate;
        this.favorite = favorite;
    }

    public Promo(boolean favorite) {
        this.favorite = favorite;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getDifficult() { return difficult; }

    public String getShortDesc(){ return short_desc;}

    public String getPrice(){return price;}

    public String getPriceType(){ return PriceType; }

    public String getHow_long(){ return how_long; }

    public String getTutorial(){ return tutorial; }

    public String getStep1(){ return step1; }

    public String getStep1_1(){ return step1_1; }

    public String getStep1_2(){ return step1_2; }

    public String getStep1_3(){ return step1_3; }

    public String getStep1_4(){ return step1_4; }

    public String getStep1_5(){ return step1_5; }

    public String getStep1_6(){ return step1_6; }

    public String getStep1_7(){ return step1_7; }

    public String getStep2(){ return step2; }

    public String getStep2_1(){ return step2_1; }

    public String getStep2_2(){ return step2_2; }

    public String getStep2_3(){ return step2_3; }

    public String getStep2_4(){ return step2_4; }

    public String getStep2_5(){ return step2_5; }

    public String getStep2_6(){ return step2_6; }

    public String getStep2_7(){ return step2_7; }

    public String getStep3(){ return step3; }

    public String getStep3_1(){ return step3_1; }

    public String getStep3_2(){ return step3_2; }

    public String getStep3_3(){ return step3_3; }

    public String getStep3_4(){ return step3_4; }

    public String getStep3_5(){ return step3_5; }

    public String getStep3_6(){ return step3_6; }

    public String getStep3_7(){ return step3_7; }

    public String getStep4(){ return step4; }

    public String getStep4_1(){ return step4_1; }

    public String getStep4_2(){ return step4_2; }

    public String getStep4_3(){ return step4_3; }

    public String getStep4_4(){ return step4_4; }

    public String getStep4_5(){ return step4_5; }

    public String getStep4_6(){ return step4_6; }

    public String getStep4_7(){ return step4_7; }

    public String getStep5(){ return step5; }

    public String getStep5_1(){ return step5_1; }

    public String getStep5_2(){ return step5_2; }

    public String getStep5_3(){ return step5_3; }

    public String getStep5_4(){ return step5_4; }

    public String getStep5_5(){ return step5_5; }

    public String getStep5_6(){ return step5_6; }

    public String getStep5_7(){ return step5_7; }

    public String getStep6(){ return step6; }

    public String getStep6_1(){ return step6_1; }

    public String getStep6_2(){ return step6_2; }

    public String getStep6_3(){ return step6_3; }

    public String getStep6_4(){ return step6_4; }

    public String getStep6_5(){ return step6_5; }

    public String getStep6_6(){ return step6_6; }

    public String getStep6_7(){ return step6_7; }

    public String getStep7(){ return step7; }

    public String getStep7_1(){ return step7_1; }

    public String getStep7_2(){ return step7_2; }

    public String getStep7_3(){ return step7_3; }

    public String getStep7_4(){ return step7_4; }

    public String getStep7_5(){ return step7_5; }

    public String getStep7_6(){ return step7_6; }

    public String getStep7_7(){ return step7_7; }

    public String getStepX(){ return stepX; }

    public int getSteps(){ return steps; }

    public int getId() {
        return id;
    }

    public boolean hasVideo() {
        return video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getTotalXp(){return xp;}

    public int getTotalDone(){return dones;}

    public String getPromoType() {return promo_type;}

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public int getReviews() {
        return reviews;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getPromoRulesUrl() {return promo_rules_url;}

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getInstructionType() {return instruction_type;}

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.voteCount);
        dest.writeString(this.difficult);
        dest.writeString(this.short_desc);
        dest.writeString(this.price);
        dest.writeString(this.PriceType);
        dest.writeString(this.how_long);
        dest.writeString(this.tutorial);
        dest.writeString(this.step1);
        dest.writeString(this.step1_1);
        dest.writeString(this.step1_2);
        dest.writeString(this.step1_3);
        dest.writeString(this.step1_4);
        dest.writeString(this.step1_5);
        dest.writeString(this.step1_6);
        dest.writeString(this.step1_7);
        dest.writeString(this.step2);
        dest.writeString(this.step2_1);
        dest.writeString(this.step2_2);
        dest.writeString(this.step2_3);
        dest.writeString(this.step2_4);
        dest.writeString(this.step2_5);
        dest.writeString(this.step2_6);
        dest.writeString(this.step2_7);
        dest.writeString(this.step3);
        dest.writeString(this.step3_1);
        dest.writeString(this.step3_2);
        dest.writeString(this.step3_3);
        dest.writeString(this.step3_4);
        dest.writeString(this.step3_5);
        dest.writeString(this.step3_6);
        dest.writeString(this.step3_7);
        dest.writeString(this.step4);
        dest.writeString(this.step4_1);
        dest.writeString(this.step4_2);
        dest.writeString(this.step4_3);
        dest.writeString(this.step4_4);
        dest.writeString(this.step4_5);
        dest.writeString(this.step4_6);
        dest.writeString(this.step4_7);
        dest.writeString(this.step5);
        dest.writeString(this.step5_1);
        dest.writeString(this.step5_2);
        dest.writeString(this.step5_3);
        dest.writeString(this.step5_4);
        dest.writeString(this.step5_5);
        dest.writeString(this.step5_6);
        dest.writeString(this.step5_7);
        dest.writeString(this.step6);
        dest.writeString(this.step6_1);
        dest.writeString(this.step6_2);
        dest.writeString(this.step6_3);
        dest.writeString(this.step6_4);
        dest.writeString(this.step6_5);
        dest.writeString(this.step6_6);
        dest.writeString(this.step6_7);
        dest.writeString(this.step7);
        dest.writeString(this.step7_1);
        dest.writeString(this.step7_2);
        dest.writeString(this.step7_3);
        dest.writeString(this.step7_4);
        dest.writeString(this.step7_5);
        dest.writeString(this.step7_6);
        dest.writeString(this.step7_7);
        dest.writeString(this.stepX);
        dest.writeInt(this.steps);
        dest.writeInt(this.id);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.voteAverage);
        dest.writeString(this.title);
        dest.writeInt(this.popularity);
        dest.writeInt(this.xp);
        dest.writeInt(this.dones);
        dest.writeString(this.promo_type);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeList(this.genreIds);
        dest.writeString(this.backdropPath);
        dest.writeInt(this.likes);
        dest.writeInt(this.comments);
        dest.writeInt(this.reviews);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.instruction_type);
        dest.writeString(this.promo_rules_url);
        dest.writeString(this.releaseDate);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
    }

    protected Promo(Parcel in) {
        this.voteCount = in.readInt();
        this.difficult = in.readString();
        this.short_desc = in.readString();
        this.price = in.readString();
        this.PriceType = in.readString();
        this.how_long = in.readString();
        this.tutorial = in.readString();
        this.step1 = in.readString();
        this.step1_1 = in.readString();
        this.step1_2 = in.readString();
        this.step1_3 = in.readString();
        this.step1_4 = in.readString();
        this.step1_5 = in.readString();
        this.step1_6 = in.readString();
        this.step1_7 = in.readString();
        this.step2 = in.readString();
        this.step2_1 = in.readString();
        this.step2_2 = in.readString();
        this.step2_3 = in.readString();
        this.step2_4 = in.readString();
        this.step2_5 = in.readString();
        this.step2_6 = in.readString();
        this.step2_7 = in.readString();
        this.step3 = in.readString();
        this.step3_1 = in.readString();
        this.step3_2 = in.readString();
        this.step3_3 = in.readString();
        this.step3_4 = in.readString();
        this.step3_5 = in.readString();
        this.step3_6 = in.readString();
        this.step3_7 = in.readString();
        this.step4 = in.readString();
        this.step4_1 = in.readString();
        this.step4_2 = in.readString();
        this.step4_3 = in.readString();
        this.step4_4 = in.readString();
        this.step4_5 = in.readString();
        this.step4_6 = in.readString();
        this.step4_7 = in.readString();
        this.step5 = in.readString();
        this.step5_1 = in.readString();
        this.step5_2 = in.readString();
        this.step5_3 = in.readString();
        this.step5_4 = in.readString();
        this.step5_5 = in.readString();
        this.step5_6 = in.readString();
        this.step5_7 = in.readString();
        this.step6 = in.readString();
        this.step6_1 = in.readString();
        this.step6_2 = in.readString();
        this.step6_3 = in.readString();
        this.step6_4 = in.readString();
        this.step6_5 = in.readString();
        this.step6_6 = in.readString();
        this.step6_7 = in.readString();
        this.step7 = in.readString();
        this.step7_1 = in.readString();
        this.step7_2 = in.readString();
        this.step7_3 = in.readString();
        this.step7_4 = in.readString();
        this.step7_5 = in.readString();
        this.step7_6 = in.readString();
        this.step7_7 = in.readString();
        this.stepX = in.readString();
        this.steps = in.readInt();
        this.id = in.readInt();
        this.video = in.readByte() != 0;
        this.voteAverage = in.readFloat();
        this.title = in.readString();
        this.popularity = in.readInt();
        this.xp = in.readInt();
        this.dones = in.readInt();
        this.promo_type = in.readString();
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.backdropPath = in.readString();
        this.likes = in.readInt();
        this.comments = in.readInt();
        this.reviews = in.readInt();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.instruction_type = in.readString();
        this.promo_rules_url = in.readString();
        this.releaseDate = in.readString();
        this.favorite = in.readByte() != 0;
    }

    public static final Creator<Promo> CREATOR = new Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel source) {
            return new Promo(source);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };



}
