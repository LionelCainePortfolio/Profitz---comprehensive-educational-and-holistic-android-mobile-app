package com.profitz.app.promos.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.profitz.app.R;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.SingleMessageAdapter;
import com.profitz.app.promos.enums.GotoType;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.fragments.BottomSheetDialogBlockUser;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.interfaces.SocketListener;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.service.SetUserOnlineService;
import com.profitz.app.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;


public class PrivateChatActivity extends AppCompatActivity implements BottomSheetDialogBlockUser.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener {
    private ImageView userImage, userImageAnim, back;
    private View onlineNotifier;
    private TextView userName;
    private TextView userLastSeenen;
    private RecyclerView chatRv;
    private Handler handler_update_messages = handler_update_messages = new Handler(Looper.getMainLooper());
    private CustomLoadingDialog customLoadingDialog;
    private HttpManager httpManager;
    private User user;
    private User userpreference;
    private ArrayList<MessageModel> messageModels;
    private MyPreferenceManager myPreferenceManager;
    private String userId;
    private SingleMessageAdapter singleMessageAdapter;
    private Context context;
    // Send message layout
    public EditText message;
    private ImageView send;
    private ImageView takePhoto, selectedImage, removeMessage, iconMore, iconCall, iconVideoCall;
    private MessageModel downloadingMessageModel;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 104;
    private final int PICK_IMAGE_REQUEST = 71;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private String attachment, attachmentExtension;
    private RelativeLayout mainLayout;
    private String friendId = "";
    private String friendName = "";
    private boolean isBlock = false;
    int serverResponseCode = 0;
    Uri uri = null;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utility.PERMISSION_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //    initRecording();
            }
        }

        boolean isAllowed = true;
        for (int grantResult : grantResults) {
            if (grantResult < 0) {
                isAllowed = false;
                break;
            }
        }
        if (isAllowed) {
            if (requestCode == REQUEST_EXTERNAL_STORAGE) {
                if (downloadingMessageModel != null) {
                    Utility.downloadImage(getApplicationContext(), downloadingMessageModel, downloadAttachmentListener);
                }
            }
        } else {
//            Toast.makeText(getApplicationContext(), "Permission denied to download", Toast.LENGTH_LONG).show();
        }
    }

    private final DownloadAttachmentListener downloadAttachmentListener = new DownloadAttachmentListener() {
        @Override
        public void onDownloadClick(MessageModel messageModel) {
            downloadingMessageModel = messageModel;
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        PrivateChatActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_EXTERNAL_STORAGE
                );
            } else {
                Utility.downloadImage(getApplicationContext(), downloadingMessageModel, downloadAttachmentListener);
            }
        }

        @Override
        public void onDownloaded() {
         //   Toast.makeText(getApplicationContext(), "File has been saved at Phone\\Pictures\\" + getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
            Toasty.success(getApplicationContext(), "Plik został zapisany w telefonie w folderze\\Pictures\\" + getResources().getString(R.string.app_name)).show();

        }
    };

    private final View.OnClickListener getMessageDetail = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //na razie nic nie rob po kliknieciu w wiadomosc
            /*
            int index = chatRv.getChildAdapterPosition(view);
            MessageModel messageModel = messageModels.get(index);
            final Dialog dialog = new Dialog(PrivateChatActivity.this);
            dialog.setContentView(R.layout.dialog_member_confirmation);
            TextView userData = dialog.findViewById(R.id.userData);
            ImageView userImage = dialog.findViewById(R.id.userImage);
            ImageView userImageAnim = dialog.findViewById(R.id.userImageAnim);

            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            Button btnOk = dialog.findViewById(R.id.btnOk);
            btnOk.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            userData.setText("Od: " + messageModel.getSend_from().getName()
                    + "\n\t\t\t" + messageModel.getSend_from().getEmail()
                    + "\n\t\t\t" + messageModel.getSend_from().getPhone()
                    + "\n\n " + messageModel.getCreated());
            Utility.showImageAnimated(getApplicationContext(), messageModel.getSend_from().getPicture(), userImage, userImageAnim);
            dialog.show();
            */
        }
    };

    private Menu iconMoreMenu;
    private ImageView recordAudio;
    private MediaRecorder myAudioRecorder;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarDetail);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setStatusBarColor(Color.gold_gradient_shape);
        //Drawable background = MyInformationsActivity.getResources().getDrawable(R.drawable.gold_gradient_shape);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setBackgroundDrawableResource(R.drawable.white);
        //enableBackButton();
        //goFullScreen();
        setContentView(R.layout.private_chat_activity);
        context =this;
        isRecording = false;
        iconCall = findViewById(R.id.iconCall);
        mainLayout = findViewById(R.id.mainLayout);
        iconVideoCall = findViewById(R.id.iconVideoCall);
        recordAudio = findViewById(R.id.recordAudio);

        userImage = findViewById(R.id.userImage);
        userImageAnim = findViewById(R.id.userImageAnim);
        /////Shake to report bug
        report_dialog = new BottomSheetDialogReportBug();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new com.profitz.app.promos.ShakeDetector();
        mShakeDetector.setOnShakeListener(new com.profitz.app.promos.ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                //tvShake.setText("Shake Action is just detected!!");

                if (report_dialog != null
                        && report_dialog.getDialog() != null
                        && report_dialog.getDialog().isShowing()
                        && !report_dialog.isRemoving()) {
                    //dialog is showing so do something
                } else {
                    Bundle args = new Bundle();
                    args.putString("source","PrivateChatActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        onlineNotifier = findViewById(R.id.onlineNotifier);
        userName = findViewById(R.id.userName);
        userLastSeenen = findViewById(R.id.userLastSeen);

        back = findViewById(R.id.back);
        iconMore = findViewById(R.id.iconMore);
        Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MyPopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, iconMore);
        MenuInflater inflater = popup.getMenuInflater();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuIconBlock:
                        if (Utility.isEmpty(friendId)) {
                          //  Toast.makeText(getApplicationContext(), "Zaczekaj na załadowanie danych, lub otwórz czat ponownie.",Toast.LENGTH_LONG).show();
                            Toasty.normal(getApplicationContext(), "Zaczekaj na załadowanie danych, lub otwórz czat ponownie.").show();

                            return true;
                        }
                        Bundle args = new Bundle();

                        BottomSheetDialogBlockUser bottomSheetBlockUser = new BottomSheetDialogBlockUser();
                        args.putString("friendId",friendId);
                        args.putString("friendName", friendName);
                        bottomSheetBlockUser.setArguments(args);
                        bottomSheetBlockUser.show(getSupportFragmentManager(), bottomSheetBlockUser.getTag());

                        return true;

                    case R.id.menuIconUnBlock:
                        if (Utility.isEmpty(friendId)) {
                           // Toast.makeText(getApplicationContext(), "Zaczekaj na załadowanie danych, lub otwórz czat ponownie.",Toast.LENGTH_LONG).show();
                            Toasty.normal(getApplicationContext(), "Zaczekaj na załadowanie danych, lub otwórz czat ponownie.").show();

                            return true;
                        }

                        new AlertDialog.Builder(PrivateChatActivity.this)
                                .setTitle("Un-block User")
                                .setMessage("This user will be able to contact you now")
                                .setPositiveButton("Un-block", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        customLoadingDialog.show();
                                        httpManager.unBlockUser(
                                                friendId,
                                                new HttpResponse() {
                                                    @Override
                                                    public void onSuccess(String response) {
                                                        customLoadingDialog.dismiss();
                                                        dialog.dismiss();

                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String status = jsonObject.getString("status");
                                                          //  Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                                            if (status.equalsIgnoreCase("success")) {
                                                                goBack();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        //
                                                    }
                                                }
                                        );
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        return true;

                    default:
                        return false;
                }
            }
        });

        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.private_chat_activity, menu);
        iconMoreMenu = menu;
        iconMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });

        messageModels = new ArrayList<>();
        chatRv = findViewById(R.id.chatRv);
        chatRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRv.setLayoutManager(layoutManager);
        singleMessageAdapter = new SingleMessageAdapter(messageModels, this, downloadAttachmentListener, getMessageDetail);

        chatRv.setAdapter(singleMessageAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        customLoadingDialog = new CustomLoadingDialog(PrivateChatActivity.this);
        httpManager = new HttpManager(this);
        myPreferenceManager = new MyPreferenceManager(this);
        myPreferenceManager = MyPreferenceManager.getInstance(PrivateChatActivity.this);

        userpreference = myPreferenceManager.getUser();
        SocketManager.getInstance();
        SocketManager.userConnected(String.valueOf(userpreference.getId()));

        loadUI();
        hideUI();


        if (getIntent() != null) {
            Intent serviceIntent = new Intent(context, SetUserOnlineService.class);
            userId = getIntent().getStringExtra("userId");
            assert userId != null;
            customLoadingDialog.show();
            httpManager.getPrivateChat(userId, new HttpResponse() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        user = new Gson().fromJson(jsonObject.getString("user_data"), User.class);
                        if (user != null) {
                            friendName = user.getUsername();

                        friendId = jsonObject.getString("friend_id");
                        isBlock = jsonObject.getBoolean("is_block");
                        Utility.toggleUserStatus(getApplicationContext(), onlineNotifier, user.isIs_online());

                        String datetime = covertTimeToText(user.getLast_scene());
                        userLastSeenen.setText(datetime);

                        userName.setText(user.getUsername());
                        Utility.showImageAnimated(getApplicationContext(), user.getPicture(), userImage, userImageAnim);

                        Type listType = new TypeToken<ArrayList<MessageModel>>() {
                        }.getType();
                        messageModels = new Gson().fromJson(jsonObject.getString("messages"), listType);
                        singleMessageAdapter.setFilter(messageModels);
                        chatRv.scrollToPosition(messageModels.size() - 1);

                        customLoadingDialog.dismiss();
                        if (getWindow().getDecorView().getRootView().isShown()){
                            httpManager.markPrivateChatAsRead(userId, new HttpResponse() {
                            @Override
                            public void onSuccess(String response) {
                                //
                            }

                            @Override
                            public void onError(String error) {
                                //
                            }
                        });
                        }


                        if (isBlock) {
                            String blockBy = jsonObject.getString("block_by");
                            // if I have blocked, hide = block, show = unblock
                            // if other has blocked, show = block, hide = unblock

                            if (blockBy.equalsIgnoreCase("me")) {
                                iconMoreMenu.getItem(0).setVisible(false);
                                iconMoreMenu.getItem(1).setVisible(true);
                            } else if (blockBy.equalsIgnoreCase("other")) {
                                iconMoreMenu.getItem(0).setVisible(true);
                                iconMoreMenu.getItem(1).setVisible(false);
                            }
                            hideUI();
                          //  Snackbar snackbar = Snackbar.make(mainLayout, "Nie możesz wysłać wiadomości w tej konwersacji.", Snackbar.LENGTH_INDEFINITE);
                            //View sbView = snackbar.getView();
                          //  sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                          //  snackbar.show();
                            Toasty.error(context, "Nie możesz wysłać wiadomości w tej konwersacji.").show();

                        } else {
                            // if not blocked, then hide unblock button
                            // and show block button
                            iconMoreMenu.getItem(0).setVisible(true);
                            iconMoreMenu.getItem(1).setVisible(false);

                            showUI();
                          //  initRecording();
                            initializeListeners();
                        }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error_log", String.valueOf(e));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.i("my_log", error);
                }
            });
        }
    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy hh:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix_now = "Aktywny/a teraz";
        String prefix = "Aktywny/a ";
        String suffix = "temu";

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date pasTime = dateFormat.parse(dataDate);


            Date nowTime = new Date();


            long dateDiff = nowTime.getTime() - pasTime.getTime();



            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = prefix_now;

            } else if ((minute < 60) & (minute >= 1)) {
                if (minute <=4){
                    convTime = prefix + minute + " minuty "+suffix;

                }
                else if ((minute >= 22 ) && (minute <=24)){
                    convTime =  prefix + minute + " minuty "+suffix;

                }
                else if ((minute >= 32 ) && (minute <=34)){
                    convTime =  prefix + minute + " minuty "+suffix;

                }
                else if ((minute >= 42 ) && (minute <=44)){
                    convTime =  prefix + minute + " minuty "+suffix;

                }
                else if ((minute >= 52 ) && (minute <=54)){
                    convTime =  prefix + minute + " minuty "+suffix;

                }
                else{
                    convTime =  prefix + minute + " minut "+suffix;

                }
            } else if (hour < 24) {
                if (hour == 1){
                    convTime =  prefix + " godzinę "+suffix;

                }
                else if ((hour >= 2 ) && (hour <=4)){
                    convTime =  prefix + hour + " godziny "+suffix;

                }
                else if ((hour >= 22 ) && (hour <= 23)){
                    convTime =  prefix + hour + " godziny "+suffix;

                }
                else if (hour !=0 && hour != 22 && hour != 23 && hour != 2 && hour != 3 && hour != 4){
                    convTime =  prefix + hour + " godzin "+suffix;

                }

            } else if ((day >= 7)&& (day <=30)) {
                    convTime =  prefix + day+" dni "+suffix;

            } else if (day < 7) {
                if (day == 1)
                {
                    convTime =  prefix + "dzień "+suffix;

                }
                else{
                    convTime =  prefix + day+" dni "+suffix;

                }
            }
            else if ((day >30) && (day <= 60)){
                convTime =  prefix + "miesiąc "+suffix;
            }
            else if ((day > 60) && (day <=90)){
                convTime =  prefix + "2 miesiące "+suffix;
            }
            else if ((day > 90) && (day <=120)){
                convTime =  prefix + "3 miesiące "+suffix;
            }
            else if ((day > 120) && (day <=150)){
                convTime =  prefix + "4 miesiące "+suffix;
            }
            else if ((day > 150) && (day <=180)){
                convTime =  prefix + "5 miesięcy "+suffix;
            }
            else if ((day > 180) && (day <=210)){
                convTime =  prefix + "6 miesięcy "+suffix;
            }
            else if ((day > 210) && (day <=240)){
                convTime =  prefix + "7 miesięcy "+suffix;
            }
            else if ((day > 240) && (day <=270)){
                convTime =  prefix + "8 miesięcy "+suffix;
            }
            else if ((day > 270) && (day <=300)){
                convTime =  prefix + "9 miesięcy "+suffix;
            }
            else if ((day > 300) && (day <=330)){
                convTime =  prefix + "10 miesięcy "+suffix;
            }
            else if ((day > 330) && (day <=360)){
                convTime =  prefix + "11 miesięcy "+suffix;
            }
            else if ((day > 360) && (day <=720)){
                convTime =  prefix + "rok "+suffix;
            }
            else if ((day > 720) && (day <=1440)){
                convTime =  prefix + "dwa lata temu "+suffix;
            }
            else if ((day > 1440) && (day <=2880)){
                convTime =  prefix + "trzy lata temu "+suffix;
            }
            else if (day > 2880){
                convTime =  prefix + "bardzo dawno "+suffix;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }
    private void initRecording() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PrivateChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, Utility.PERMISSION_RECORD_AUDIO);
        } else {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
                            myAudioRecorder = new MediaRecorder();
                            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                           // myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                            myAudioRecorder.setOutputFile(outputFile);
                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                            //
                        }
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            //
                        }
                    }).check();
        }
    }

    private void loadUI() {
        // Send message layout
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        takePhoto = findViewById(R.id.takePhoto);
        selectedImage = findViewById(R.id.selectedImage);
        removeMessage = findViewById(R.id.removeMessage);
        selectedImage.setVisibility(View.GONE);
        removeMessage.setVisibility(View.GONE);

        attachment = "";
        attachmentExtension = "";
    }

    private void hideUI() {
        message.setVisibility(View.GONE);
        send.setVisibility(View.GONE);
        takePhoto.setVisibility(View.GONE);
    }

    private void showUI() {
        message.setVisibility(View.VISIBLE);
        send.setVisibility(View.VISIBLE);
        takePhoto.setVisibility(View.VISIBLE);
    }
    @SuppressLint("NewApi")
    public String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
    private void initializeListeners() {
        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    Toast.makeText(getApplicationContext(), "Nagrywanie zatrzymane.", Toast.LENGTH_LONG).show();
                    isRecording = false;
                } else {
                    try {
                        if (myAudioRecorder != null) {
                            myAudioRecorder.prepare();
                            myAudioRecorder.start();
                            Toast.makeText(getApplicationContext(), "Nagrywanie rozpoczęte.", Toast.LENGTH_LONG).show();
                            isRecording = true;
                        }
                    } catch (IllegalStateException ise) {
                        Toast.makeText(getApplicationContext(), ise.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException ioe) {
                        Toast.makeText(getApplicationContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageText = message.getText().toString();
                if (!messageText.trim().isEmpty() || !attachment.equals("")) {
                    message.setText("");
                    customLoadingDialog.show();
                    selectedImage.setVisibility(View.GONE);
                    removeMessage.setVisibility(View.GONE);

                    final MessageModel messageModel = new MessageModel();
                    messageModel.setAttachment(attachment);
                    messageModel.setAttachmentExtension(attachmentExtension);
                    messageModel.setContent(messageText);
                    messageModel.setMessage_type(MessageType.PRIVATE_CHAT.getType());
                    messageModel.setSend_from(userpreference);
                    messageModel.setSend_to(user);
                    httpManager.sendMessage(messageModel, new HttpResponse() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                messageModel.setId(responseJson.getString("message_id"));
                                String file_path = responseJson.getString("file_path");
                                String many_messages_in_one_time = responseJson.getString("many_messages_in_one_time");
                                String first_message = responseJson.getString("first_message");
                                String last_message = responseJson.getString("last_message");

                                messageModel.setAttachment(file_path);
                                messageModel.setAttachmentExtension(attachmentExtension);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
                                messageModel.setCreated(simpleDateFormat.format(new Date()));
                                messageModel.setSystemMessage("false");
                              messageModel.setManyMessagesInOneMin(many_messages_in_one_time);
                              messageModel.setManyMessagesInOneMinFirstMessage(first_message);
                               messageModel.setManyMessagesInOneMinLastMessage(last_message);

                                messageModel.setMessage_type("New message");
                                    new Thread(new Runnable() {
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    // messageText.setText("uploading started.....");
                                                }
                                            });

                                            String fileName = getRealPathFromURI_API19(context,uri);
                                            String path = getRealPathFromURI_API19(context,uri);
                                            HttpURLConnection conn = null;
                                            DataOutputStream dos = null;
                                            String lineEnd = "\r\n";
                                            String twoHyphens = "--";
                                            String boundary = "*****";
                                            int bytesRead, bytesAvailable, bufferSize;
                                            byte[] buffer;
                                            int maxBufferSize = 1024 * 1024;
                                            File sourceFile = new File(path);

                                            if (!sourceFile.isFile()) {
                                                Log.e("uploadFile", "Source File not exist :"+path);
                                            }
                                            else
                                            {
                                                try {

                                                    // open a URL connection to the Servlet
                                                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                                                    URL url = new URL("https://yoururl.com/api/scp/UploadToServer.php");

                                                    // Open a HTTP  connection to  the URL
                                                    conn = (HttpURLConnection) url.openConnection();
                                                    conn.setDoInput(true); // Allow Inputs
                                                    conn.setDoOutput(true); // Allow Outputs
                                                    conn.setUseCaches(false); // Don't use a Cached Copy
                                                    conn.setRequestMethod("POST");
                                                    conn.setRequestProperty("Connection", "Keep-Alive");
                                                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                                                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                                    conn.setRequestProperty("uploaded_file", fileName);
                                                    dos = new DataOutputStream(conn.getOutputStream());

                                                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                                                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

                                                    dos.writeBytes(lineEnd);
                                                    Log.d("file2", fileName);
                                                    Log.d("file3", lineEnd);

                                                    // create a buffer of  maximum size
                                                    bytesAvailable = fileInputStream.available();

                                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                                    buffer = new byte[bufferSize];

                                                    // read file and write it into form...
                                                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                                    while (bytesRead > 0) {

                                                        dos.write(buffer, 0, bufferSize);
                                                        bytesAvailable = fileInputStream.available();
                                                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                                    }

                                                    // send multipart form data necesssary after file data...
                                                    dos.writeBytes(lineEnd);
                                                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                                                    // Responses from the server (code and message)
                                                    serverResponseCode = conn.getResponseCode();
                                                    String serverResponseMessage = conn.getResponseMessage();

                                                    Log.i("uploadFile", "HTTP Response is : "
                                                            + serverResponseMessage + ": " + serverResponseCode);
                                                    //   Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Avatar został zaktualizowany. Zmiany w niektórych miejsach mogą pojawić się do kilku godzin.", Snackbar.LENGTH_LONG);
                                                    //  snackbar.show();

                                                    //close the streams //
                                                    fileInputStream.close();
                                                    dos.flush();
                                                    dos.close();

                                                } catch (MalformedURLException ex) {


                                                    ex.printStackTrace();
                                                    //   Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                                                    //     snackbar.show();
                                                    Toasty.error(context, "Wystąpił błąd podczas wysyłania. Spróbuj ponownie.").show();

                                                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                                                } catch (Exception e) {
                                                    Log.e("Upload file to server", "error: " + e);
                                                    //    Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                                                    //      snackbar.show();
                                                    Toasty.error(context, "Wystąpił błąd podczas wysyłania. Spróbuj ponownie.").show();

                                                    e.printStackTrace();


                                                }

                                            } // End else block
                                        }});


                                if (!Utility.isEmpty(user.getFcm_token())) {
                                   // new SendFcmNotification(userModel.getFcm_token(), messageModel, null).execute();
                                }

                                messageModel.setMessage_type(MessageType.PRIVATE_CHAT.getType());

                                messageModels.add(messageModel);
                                singleMessageAdapter.addItem(messageModel);
                                chatRv.scrollToPosition(messageModels.size() - 1);


                                SocketManager.sendOutgoingPrivateMessage(messageModel, userId);
                                attachment = "";
                                attachmentExtension = "";


                                customLoadingDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
  //              intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Wybierz z galerii"), PICK_IMAGE_REQUEST);
            }
        });

        removeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment = "";
                attachmentExtension = "";

                selectedImage.setVisibility(View.GONE);
                removeMessage.setVisibility(View.GONE);
            }
        });

        iconCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivateChatActivity.this, PrivateCallActivity.class);
                intent.putExtra("userId", userId);
                Utility.gotoActivity(PrivateChatActivity.this, intent);
            }
        });

        iconVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivateChatActivity.this, PrivateVideoCallActivity.class);
                intent.putExtra("userId", userId);
                Utility.gotoActivity(PrivateChatActivity.this, intent);
            }
        });

                SocketManager.attachIncomingPrivateMessageListener(new SocketListener() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d("test11", response);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("test1", "test2");
                                final MessageModel messageModel = new Gson().fromJson(response, MessageModel.class);
                                if (messageModel.getMessage_type().equalsIgnoreCase(MessageType.PRIVATE_CHAT.getType())
                                        && messageModel.getSend_to().getId().equalsIgnoreCase(userpreference.getId())
                                        && messageModel.getSend_from().getId().equalsIgnoreCase(userId)) {

                                    httpManager.getMessageContent(messageModel.getId(), new HttpResponse() {
                                        @Override                                        public void onSuccess(String response) {

                                            try {
                                                messageModel.setContent(new JSONObject(response).getString("content"));
                                                messageModels.add(messageModel);
                                                singleMessageAdapter.addItem(messageModel);
                                                chatRv.scrollToPosition(messageModels.size() - 1);
                                                customLoadingDialog.dismiss();

                                                httpManager.markPrivateChatAsRead(userId, new HttpResponse() {
                                                    @Override
                                                    public void onSuccess(String response) {
                                                        //
                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        //
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Log.d("my_log", error);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

            }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
        intent.putExtra(Utility.GOTO_TYPE_KEY, GotoType.PRIVATE_TAB.getType());
        Utility.gotoActivity(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {

                  uri      = data.getData();
                String uriPath = uri.toString();

                String mime = getContentResolver().getType(uri);
                if (mime != null && (mime.equalsIgnoreCase("image/jpeg") || mime.equalsIgnoreCase("image/png"))) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    attachment = Utility.getStringImage(bitmap);

                    byte[] bytesImage = Base64.decode(attachment, Base64.DEFAULT);
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(bytesImage)
                            .placeholder(R.drawable.placeholder_logo)
                            .into(selectedImage);
                 /*   Picasso.with(getApplicationContext()).load(Arrays.toString(bytesImage))
                            .placeholder(R.drawable.file_placeholder)
                            .error(R.drawable.file_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(selectedImage);
                    */


                } else {
                    try {
                        InputStream in = getContentResolver().openInputStream(uri);
                        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];

                        int len = 0;
                        while ((len = in.read(buffer)) != -1) {
                            byteBuffer.write(buffer, 0, len);
                        }
                        byte[] bytes = byteBuffer.toByteArray();
//                        Log.d("data", "onActivityResult: bytes size="+bytes.length);
//                        Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
//                        String ansValue = Base64.encodeToString(bytes,Base64.DEFAULT);
                        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);

//                        Log.i("my_log", encode);
                        attachment = encode;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.i("my_log", e.getMessage());
                    }

                    selectedImage.setImageDrawable(getDrawable(R.drawable.placeholder_logo));
                }

                attachmentExtension = uriPath.substring(uriPath.lastIndexOf(".") + 1);
//                Log.i("my_log", attachmentExtension);

                selectedImage.setVisibility(View.VISIBLE);
                removeMessage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onButtonClicked(String text) {

    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
