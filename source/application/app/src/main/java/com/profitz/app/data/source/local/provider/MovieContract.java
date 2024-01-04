package com.profitz.app.data.source.local.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.profitz.app.";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_DIFFICULT = "difficult";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_PRICE_TYPE = "price_type";
        public static final String COLUMN_HOW_LONG = "how_long";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_PROMO_TYPE = "promo_type";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TUTORIAL = "tutorial";
        public static final String COLUMN_STEP1 = "step1";
        public static final String COLUMN_STEP1_1 = "step1_1";
        public static final String COLUMN_STEP1_2 = "step1_2";
        public static final String COLUMN_STEP1_3 = "step1_3";
        public static final String COLUMN_STEP1_4 = "step1_4";
        public static final String COLUMN_STEP1_5 = "step1_5";
        public static final String COLUMN_STEP1_6 = "step1_6";
        public static final String COLUMN_STEP1_7 = "step1_7";
        public static final String COLUMN_STEP2 = "step2";
        public static final String COLUMN_STEP2_1 = "step1_1";
        public static final String COLUMN_STEP2_2 = "step1_2";
        public static final String COLUMN_STEP2_3 = "step1_3";
        public static final String COLUMN_STEP2_4 = "step1_4";
        public static final String COLUMN_STEP2_5 = "step1_5";
        public static final String COLUMN_STEP2_6 = "step1_6";
        public static final String COLUMN_STEP2_7 = "step1_7";
        public static final String COLUMN_STEP3 = "step3";
        public static final String COLUMN_STEP3_1 = "step1_1";
        public static final String COLUMN_STEP3_2 = "step1_2";
        public static final String COLUMN_STEP3_3 = "step1_3";
        public static final String COLUMN_STEP3_4 = "step1_4";
        public static final String COLUMN_STEP3_5 = "step1_5";
        public static final String COLUMN_STEP3_6 = "step1_6";
        public static final String COLUMN_STEP3_7 = "step1_7";
        public static final String COLUMN_STEP4 = "step4";
        public static final String COLUMN_STEP4_1 = "step1_1";
        public static final String COLUMN_STEP4_2 = "step1_2";
        public static final String COLUMN_STEP4_3 = "step1_3";
        public static final String COLUMN_STEP4_4 = "step1_4";
        public static final String COLUMN_STEP4_5 = "step1_5";
        public static final String COLUMN_STEP4_6 = "step1_6";
        public static final String COLUMN_STEP4_7 = "step1_7";
        public static final String COLUMN_STEP5 = "step5";
        public static final String COLUMN_STEP5_1 = "step1_1";
        public static final String COLUMN_STEP5_2 = "step1_2";
        public static final String COLUMN_STEP5_3 = "step1_3";
        public static final String COLUMN_STEP5_4 = "step1_4";
        public static final String COLUMN_STEP5_5 = "step1_5";
        public static final String COLUMN_STEP5_6 = "step1_6";
        public static final String COLUMN_STEP5_7 = "step1_7";
        public static final String COLUMN_STEP6 = "step6";
        public static final String COLUMN_STEP6_1 = "step1_1";
        public static final String COLUMN_STEP6_2 = "step1_2";
        public static final String COLUMN_STEP6_3 = "step1_3";
        public static final String COLUMN_STEP6_4 = "step1_4";
        public static final String COLUMN_STEP6_5 = "step1_5";
        public static final String COLUMN_STEP6_6 = "step1_6";
        public static final String COLUMN_STEP6_7 = "step1_7";
        public static final String COLUMN_STEP7 = "step7";
        public static final String COLUMN_STEP7_1 = "step1_1";
        public static final String COLUMN_STEP7_2 = "step1_2";
        public static final String COLUMN_STEP7_3 = "step1_3";
        public static final String COLUMN_STEP7_4 = "step1_4";
        public static final String COLUMN_STEP7_5 = "step1_5";
        public static final String COLUMN_STEP7_6 = "step1_6";
        public static final String COLUMN_STEP7_7 = "step1_7";
        public static final String COLUMN_STEPX = "stepX";
        public static final String COLUMN_STEPS = "steps";
        public static final String COLUMN_XP = "xp";
        public static final String COLUMN_DONES = "dones";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_COMMENTS = "comments";
        public static final String COLUMN_REVIEWS = "reviews";
        public static final String COLUMN_PROMO_RULES_URL = "promo_rules_url";
        public static final String COLUMN_INSTRUCTION_TYPE = "instruction_type";
    }
}
