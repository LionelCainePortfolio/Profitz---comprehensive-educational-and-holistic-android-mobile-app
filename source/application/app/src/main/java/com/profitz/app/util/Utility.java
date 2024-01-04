package com.profitz.app.util;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.profitz.app.R;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.data.model.SingleMessageModel;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.async.DownloadImageTask;
import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Utility {
    public static final String KEY_EVENT_MESSAGE_TYPING_DISCUSSION = "KEY_EVENT_MESSAGE_TYPING_DISCUSSION";
    public static final String KEY_EVENT_MESSAGE_TYPING_PRIVATE = "KEY_EVENT_MESSAGE_TYPING_PRIVATE";

    public static final String KEY_EVENT_MESSAGE_DELETED = "KEY_EVENT_MESSAGE_DELETED";

    // TODO: Enable this for production
    public static final String NODE_ROOT_URL = "http://node.yourserver.com:1777";

    public static final String API_ROOT_URL = "https://yoururl.com/api/scp/Http.php";
    public static final String BASE_URL = "https://yoururl.com/api/scp";


    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static final String FCM_SERVER_KEY = "AAA:BBB-CCC";

    public static final int NOTIFICATION_ID = 565;
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int PERMISSION_EXTERNAL_STORAGE_TO_DOWNLOAD = 565;
    public static final int PERMISSION_RECORD_AUDIO = 104;
    public static final String GOTO_TYPE_KEY = "GOTO_TYPE_KEY";
    public static final String TERMS_CONDITION_URL = "";

    public static void doLogout(Context context) {
        MyPreferenceManager preferenceManager = new MyPreferenceManager(context);
        preferenceManager.removeLoginUser();
        preferenceManager.setLogin(false);
    }

    public static void gotoActivity(AppCompatActivity activity, Class activityName) {
        activity.startActivity(new Intent(activity, activityName));
        activity.finish();
    }

    public static void gotoActivity(AppCompatActivity activity, Intent intent) {
        activity.startActivity(intent);
        activity.finish();
    }

    public static void createNotification(Context context, String title, String message, String type, Intent intent) {
        String channelId = String.valueOf(message.length());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSound(notificationSoundUri)
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(new long[]{250, 500, 250, 500, 250, 500});
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(contentIntent);
            notificationBuilder.setAutoCancel(true);

            @SuppressLint("WrongConstant") NotificationChannel nc = new NotificationChannel(channelId, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            nc.setDescription(context.getString(R.string.app_name));
            nc.enableLights(true);
            nc.setLightColor(Color.GREEN);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(nc);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }
        } else {
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSound(notificationSoundUri)
                    .setLights(Color.BLUE, 500, 500)
                    .setVibrate(new long[]{250, 500, 250, 500, 250, 500});
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(contentIntent);
            notificationBuilder.setAutoCancel(true);

            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }
        }
    }

    public static void removeNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    public static void updateFirebaseToken(final Context context, final Runnable onRefreshed) {
        HttpManager httpManager = new HttpManager(context);
        httpManager.updateFirebaseToken(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                if (onRefreshed != null)
                    onRefreshed.run();
            }

            @Override
            public void onError(String error) {
                //
            }
        });
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        p.setMargins(l, t, r, b);
        v.requestLayout();
    }

    public static void alignParentRight(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        view.setLayoutParams(params);

        /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        setMargins(view, 70, 10, 10, 10);*/
    }

    public static void alignParentLeft(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT;
        view.setLayoutParams(params);

        /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            setMargins(view, 10, 10, 70, 10);
        }*/
    }

    public static boolean isAppRunning() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

    public static void handleSomeoneTyping(final AppCompatActivity activity, final Socket socket, final TextView someoneTyping, EditText message, final String sendTo) {
        final MyPreferenceManager preferenceManager = new MyPreferenceManager(activity);
        User user = MyPreferenceManager.getInstance(activity).getUser();

        final Handler handler = new Handler();
        final Runnable someoneTypingHandler = new Runnable() {
            @Override
            public void run() {
                someoneTyping.setVisibility(View.GONE);
            }
        };

        if (sendTo.isEmpty()) {
            socket.on(Utility.KEY_EVENT_MESSAGE_TYPING_DISCUSSION, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    boolean needsToShow = !args[0].toString().equals(user.getUsername());

                    if (needsToShow) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                someoneTyping.setVisibility(View.VISIBLE);
                                someoneTyping.setText(args[0].toString() + " pisze...");

                                handler.postDelayed(someoneTypingHandler, 3000);
                            }
                        });
                    }
                }
            });
        } else {
            socket.on(Utility.KEY_EVENT_MESSAGE_TYPING_PRIVATE, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    boolean needsToShow = false;
                    try {
                        final SingleMessageModel messageModel = new Gson().fromJson(args[0].toString(), SingleMessageModel.class);
                        if (messageModel.getSend_to().equals(user.getUsername())
                                && messageModel.getSend_from().equals(sendTo)) {
                            needsToShow = true;
                        }

                        if (needsToShow) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    someoneTyping.setVisibility(View.VISIBLE);
                                    someoneTyping.setText("pisze...");

                                    handler.postDelayed(someoneTypingHandler, 3000);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.toString().length() % 5 == 0
                        && preferenceManager.getUserModel().isEnableTyping()
                        && !s.toString().trim().isEmpty()) {
                    if (sendTo.isEmpty()) {
                        if (!preferenceManager.getUserModel().isAnonymousDiscussion()) {
                            socket.emit(Utility.KEY_EVENT_MESSAGE_TYPING_DISCUSSION, preferenceManager.getUserModel().getName());
                        }
                    } else {
                        SingleMessageModel singleMessageModel = new SingleMessageModel();
                        singleMessageModel.setSend_from(preferenceManager.getUserModel().getUsername());
                        singleMessageModel.setSend_to(sendTo);

                        socket.emit(Utility.KEY_EVENT_MESSAGE_TYPING_PRIVATE, new Gson().toJson(singleMessageModel));
                    }
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static void toggleUserStatus(Context context, View view, boolean isOnline) {
        Drawable background = view.getBackground();

        if (background instanceof GradientDrawable) {
            if (isOnline) {
                GradientDrawable bgShape = (GradientDrawable) background;
                bgShape.setColor(context.getResources().getColor(R.color.colorGreen));
            } else {
                GradientDrawable bgShape = (GradientDrawable) background;
                bgShape.setColor(context.getResources().getColor(R.color.colorOffline));
            }
        }

    }

    public static void markUserOffline(Context context) {
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(context);
        HttpManager httpManager = new HttpManager(context);
        httpManager.changeUserState(String.valueOf(myPreferenceManager.getUser().getId()), "offline");

        /*UserModel userModel = myPreferenceManager.getUserModel();
        userModel.setIs_online(false);
        myPreferenceManager.setLoginUsermodel(userModel);
        socket.emit(KEY_EVENT_MARK_USER_OFFLINE, userModel.getUsername());*/
    }

    public static void markUserOnline(Context context) {
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(context);
        HttpManager httpManager = new HttpManager(context);
        httpManager.changeUserState(String.valueOf(myPreferenceManager.getUser().getId()), "online");

        /*UserModel userModel = myPreferenceManager.getUserModel();
        userModel.setIs_online(true);
        myPreferenceManager.setLoginUsermodel(userModel);
        socket.emit(Utility.KEY_EVENT_MARK_USER_ONLINE, userModel.getUsername());*/
    }

    public static Date UTCtoLocalDate(long timestamp) {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        return new Date(timestamp + TimeZone.getTimeZone(timeZone).getOffset(timestamp));
    }

    public static String getLastScene(long lastSceneTimestamp) {
        Date date = UTCtoLocalDate(lastSceneTimestamp);
        String lastScene = DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
        if (lastScene.contains("0 minute")) {
            lastScene = "just now";
        }
        return "last scene " + lastScene;
    }

    public static String getStringValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static void showImageAnimated(Context context, String imagePath, ImageView imageView, ImageView animatedimageView) {
        if (!Utility.isEmpty(imagePath)) {
            AnimationDrawable progressAnimation;
            progressAnimation = (AnimationDrawable) animatedimageView.getDrawable();
            progressAnimation.setCallback(animatedimageView);
           progressAnimation.setVisible(true, true);
            animatedimageView.post(new Runnable() {
                @Override
                public void run() {
                   progressAnimation.start();
                }
            });
            Picasso.with( context )
                    .load(imagePath)
                    .fit()
                    .config(Bitmap.Config.ARGB_4444)
                    .centerCrop()
                    .noFade()
                    .error( context.getDrawable( R.drawable.progresscircle ) )
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into( imageView ,new Callback() {
                        @Override
                        public void onSuccess() {
                            animatedimageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            animatedimageView.setVisibility(View.GONE);
                            //Try again online if cache failed
                            Picasso.with(context)
                                    .load(imagePath)
                                    .error( context.getDrawable( R.drawable.progresscircle ))
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            animatedimageView.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
      /*
            Glide.with(context)
                    .asBitmap()
                    .load(imagePath)
                    .placeholder(R.drawable.progresscircle)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
            */
        }
    }
    public static void showImage(Context context, String imagePath, ImageView imageView) {
        if (!Utility.isEmpty(imagePath)) {

            Picasso.with(context).load(imagePath)
                    .placeholder(R.drawable.progresscircle)
                    .error(R.drawable.progresscircle)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);

        }
    }

    public static void showNoInternetMessage(Context context, View main, View.OnClickListener mOnClickListener) {
        Snackbar snackbar = Snackbar.make(main, "Brak połączenia z internetem ", Snackbar.LENGTH_INDEFINITE);
        if (mOnClickListener != null) {
            snackbar.setAction("Ponów", mOnClickListener);
            snackbar.setActionTextColor(Color.WHITE);
        }
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        snackbar.show();
    }

    public static void downloadImage(Context context, MessageModel messageModel, DownloadAttachmentListener downloadAttachmentListener) {
        String attachmentExtension = messageModel.getAttachment().substring(messageModel.getAttachment().lastIndexOf(".") + 1);
        String fileName = messageModel.getContent().trim() + "_" + messageModel.getId() + new Date().getTime() + "." + attachmentExtension;
        new DownloadImageTask(context, messageModel.getAttachment(), fileName, downloadAttachmentListener).execute();
    }
}
