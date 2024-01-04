package com.profitz.app.promos.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.profitz.app.R;
import com.profitz.app.data.model.GroupDetailModel;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.SingleMessageAdapter;
import com.profitz.app.promos.async.SendFcmNotification;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.interfaces.SocketListener;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class GroupDetailActivity extends AppCompatActivity implements BottomSheetDialogReportBug.BottomSheetListener {
    private ImageView back, groupImage;
    private TextView groupName, numberOfMembers, tvNotMember;
    private CustomLoadingDialog customLoadingDialog;

    private HttpManager httpManager;
    private String groupId;
    private GroupDetailModel groupDetailModel;

    private final int PICK_IMAGE_REQUEST = 71;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private RecyclerView chatRv;
    private SingleMessageAdapter singleMessageAdapter;
    private ArrayList<MessageModel> messageModels;
    private RelativeLayout groupDetailLayout, layoutNotMember, main;
    private MyPreferenceManager myPreferenceManager;
    private boolean isIAmMember, isIAmBanned, isIAmAdmin;
    private Button btnRequestJoin;
    private User user;
    private Context context;
    // Send message layout
    public EditText message;
    private ImageView send;
    private ImageView takePhoto, selectedImage, removeMessage;
    private MessageModel downloadingMessageModel;

    private static final int REQUEST_EXTERNAL_STORAGE = 104;

    private String attachment, attachmentExtension;
    private ImageView iconLeave;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            //Toast.makeText(getApplicationContext(), "Brak uprawnień by pobrać", Toast.LENGTH_LONG).show();
            Toasty.normal(getApplicationContext(), "Brak uprawnień by pobrać.").show();

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
                        GroupDetailActivity.this,
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
     //       Toast.makeText(getApplicationContext(), "File has been saved at Phone\\Pictures\\" + getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
            Toasty.success(getApplicationContext(), "Plik został zapisany w telefonie w folderze\\Pictures\\" + getResources().getString(R.string.app_name)).show();

        }
    };

    private final View.OnClickListener getMessageDetail = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = chatRv.getChildAdapterPosition(view);
            MessageModel messageModel = messageModels.get(index);
            final Dialog dialog = new Dialog(GroupDetailActivity.this);
            dialog.setContentView(R.layout.dialog_member_confirmation);
            TextView userData = dialog.findViewById(R.id.userData);
            ImageView userImage = dialog.findViewById(R.id.userImage);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            Button btnOk = dialog.findViewById(R.id.btnOk);
            btnOk.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            userData.setText("Od: " + messageModel.getSend_from().getName()
                    + "\n\t\t" + messageModel.getSend_from().getEmail()
                    + "\n\t\t" + messageModel.getSend_from().getPhone()
                    + "\n\n" + messageModel.getCreated());
            Utility.showImage(getApplicationContext(), Utility.BASE_URL + "/" + messageModel.getSend_from().getPicture(), userImage);
            dialog.show();
        }
    };

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
        getWindow().setBackgroundDrawableResource(R.drawable.white);       // goFullScreen();
        setContentView(R.layout.activity_group_detail);

        SocketManager.getInstance();
        context = this;
        httpManager = new HttpManager(this);
        myPreferenceManager = new MyPreferenceManager(this);
        messageModels = new ArrayList<>();
        groupName = findViewById(R.id.groupName);
        tvNotMember = findViewById(R.id.tvNotMember);
        layoutNotMember = findViewById(R.id.layoutNotMember);
        main = findViewById(R.id.main);
        btnRequestJoin = findViewById(R.id.btnRequestJoin);
        layoutNotMember.setVisibility(View.GONE);
        groupDetailLayout = findViewById(R.id.groupDetailLayout);
        customLoadingDialog = new CustomLoadingDialog(GroupDetailActivity.this);
        numberOfMembers = findViewById(R.id.numberOfMembers);
        groupImage = findViewById(R.id.groupImage);
        user = myPreferenceManager.getUser();
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
                    args.putString("source","GroupDetailActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        iconLeave = findViewById(R.id.iconLeave);
        iconLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isIAmAdmin) {
        //            Toast.makeText(getApplicationContext(), "Jesteś administratorem grupy", Toast.LENGTH_LONG).show();
                    Toasty.normal(getApplicationContext(), "Jesteś administratorem grupy.").show();

                }
                else{
                      new AlertDialog.Builder(GroupDetailActivity.this)
                        .setTitle("Opuść grupę")
                        .setMessage("Nie będziesz mógł(a) udzielać się ani wyświetlać wiadomości w tej grupie.")
                        .setPositiveButton("Opuść", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                customLoadingDialog.show();
                                httpManager.leaveGroup(
                                        groupId,
                                        new HttpResponse() {
                                            @Override
                                            public void onSuccess(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                                    Toasty.normal(getApplicationContext(), jsonObject.getString("message")).show();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                customLoadingDialog.dismiss();
                                                dialog.dismiss();
                                                goBack();
                                            }

                                            @Override
                                            public void onError(String error) {
                                                //
                                            }
                                        }
                                );
                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .show();
                }
             /* POPUP OLD
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, view);
                MenuInflater inflater = popup.getMenuInflater();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuIconLeave:
                                if (isIAmAdmin) {
                                    Toast.makeText(getApplicationContext(), "Jesteś administratorem grupy", Toast.LENGTH_LONG).show();
                                    return true;
                                }
                                new AlertDialog.Builder(GroupDetailActivity.this)
                                        .setTitle("Opuść grupę")
                                        .setMessage("Nie będziesz mógł(a) udzielać się ani wyświetlać wiadomości w tej grupie.")
                                        .setPositiveButton("Opuść", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                customLoadingDialog.show();
                                                httpManager.leaveGroup(
                                                        groupId,
                                                        new HttpResponse() {
                                                            @Override
                                                            public void onSuccess(String response) {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                customLoadingDialog.dismiss();
                                                                dialog.dismiss();
                                                                goBack();
                                                            }

                                                            @Override
                                                            public void onError(String error) {
                                                                //
                                                            }
                                                        }
                                                );
                                            }
                                        })
                                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
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
                inflater.inflate(R.menu.group_detail_activity, menu);
                popup.show();
                */
            }
        });

        btnRequestJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customLoadingDialog.show();
                httpManager.requestGroupJoin(groupId, new HttpResponse() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            final String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("success")) {
                                MessageModel messageModel = new MessageModel();
                                messageModel.setMessage_type(MessageType.GROUP_REQUEST.getType());
                                messageModel.setContent(user.getName() + " poprosił o dołączenie do jednej z twoich grup");
                                messageModel.setSend_from(user);
                                User sendToMode = MyPreferenceManager.getInstance(context).getUser();
                                sendToMode.setId(groupId);
                                messageModel.setSend_to(sendToMode);

                                new SendFcmNotification(groupDetailModel.getAdmin().getFcm_token(), messageModel, new Runnable() {
                                    @Override
                                    public void run() {
                                        customLoadingDialog.dismiss();
                                     //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        Toasty.normal(getApplicationContext(), message).show();

                                        SocketManager.sendRequest(String.valueOf(groupDetailModel.getAdmin().getId()), MessageType.GROUP_REQUEST.getType());
                                    }
                                }).execute();
                            } else {
                                customLoadingDialog.dismiss();
                                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                Toasty.normal(getApplicationContext(), message).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("my_log", error);
                    }
                });
            }
        });

        groupDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((isIAmMember || isIAmAdmin) && !isIAmBanned) {
                    Intent intent = new Intent(getApplicationContext(), GroupMetaDetailActivity.class);
                    intent.putExtra("groupId", groupId);
                    Utility.gotoActivity(GroupDetailActivity.this, intent);
                }
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        chatRv = findViewById(R.id.chatRv);
        chatRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRv.setLayoutManager(layoutManager);
        singleMessageAdapter = new SingleMessageAdapter(messageModels, this, downloadAttachmentListener, getMessageDetail);
        chatRv.setAdapter(singleMessageAdapter);

        loadUI();
        if (getIntent() != null) {
            groupId = getIntent().getStringExtra("groupId");
            getGroupDetail();
        }
    }

    private void getGroupDetail() {
        customLoadingDialog.show();
        httpManager.getGroupDetail(groupId, new HttpResponse() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("error")) {
                        String message = jsonObject.getString("message");
                      //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Toasty.normal(getApplicationContext(), message).show();

                        Utility.gotoActivity(GroupDetailActivity.this, ChatsActivity.class);
                    } else {
                        groupDetailModel = new Gson().fromJson(response, GroupDetailModel.class);
                        groupName.setText(groupDetailModel.getDetails().getGroup_name());

                        isIAmAdmin = groupDetailModel.getAdmin().getIdd().equalsIgnoreCase(String.valueOf(user.getId()));
                        for (int a = 0; a < groupDetailModel.getMembers().size(); a++) {
                            if (groupDetailModel.getMembers().get(a).getIdd().equalsIgnoreCase(String.valueOf(user.getId()))) {
                                isIAmMember = true;
                                isIAmBanned = groupDetailModel.getMembers().get(a).isIs_ban();
                                break;
                            }
                        }
                        if (isIAmMember || isIAmAdmin) {
                            if (isIAmBanned) {
                              //  Snackbar snackbar = Snackbar.make(main, "Zostałeś zbanowany przez administratora grupy", Snackbar.LENGTH_INDEFINITE);
                                //View sbView = snackbar.getView();
                              //  sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                              //  snackbar.show();
                                Toasty.error(context, "Zostałeś zbanowany przez administratora grupy!").show();

                                hideUI();
                            } else {
                                messageModels.clear();
                                messageModels.addAll(groupDetailModel.getMessages());
                                singleMessageAdapter.notifyDataSetChanged();
                                groupName.setText(groupDetailModel.getDetails().getGroup_name());
                                // +1 is for admin
                                numberOfMembers.setText((groupDetailModel.getMembers().size()) + " członków");
                                chatRv.scrollToPosition(messageModels.size() - 1);

                                Utility.showImage(getApplicationContext(), Utility.BASE_URL + "/" + groupDetailModel.getDetails().getPicture(), groupImage);
                                SocketManager.userJoinGroup(groupId);

                                httpManager.markGroupChatAsRead(groupId, new HttpResponse() {
                                    @Override
                                    public void onSuccess(String response) {
                                        //
                                    }

                                    @Override
                                    public void onError(String error) {
                                        //
                                    }
                                });

                                initializeListeners();
                            }
                        } else {
                            hideUI();
                            layoutNotMember.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                customLoadingDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                //
            }
        });
    }

    private void initializeListeners() {
        final User sendTo = MyPreferenceManager.getInstance(this).getUser();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageText = message.getText().toString();
                if (messageText.trim().isEmpty() && attachment.isEmpty()) {
                    return;
                }

                message.setText("");
                customLoadingDialog.show();
                selectedImage.setVisibility(View.GONE);
                removeMessage.setVisibility(View.GONE);

                final MessageModel messageModel = new MessageModel();
                messageModel.setAttachment(attachment);
                messageModel.setAttachmentExtension(attachmentExtension);
                messageModel.setContent(messageText);
                messageModel.setMessage_type(MessageType.GROUP.getType());
                messageModel.setSend_from(myPreferenceManager.getUser());
                sendTo.setId(groupId);
                messageModel.setSend_to(sendTo);

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
                            messageModel.setMessage_type(MessageType.GROUP.getType());
                            messageModel.setSystemMessage("false");
                            messageModel.setManyMessagesInOneMin(many_messages_in_one_time);
                            messageModel.setManyMessagesInOneMinFirstMessage(first_message);
                            messageModel.setManyMessagesInOneMinLastMessage(last_message);
                            // Sending FCM notification to all members
                            // for loop was not working here
                            int a = 0;
                            while (a < groupDetailModel.getMembers().size()) {
                                if (!groupDetailModel.getMembers().get(a).getIdd().equalsIgnoreCase(String.valueOf(myPreferenceManager.getUser().getId()))) {
                                    if (!Utility.isEmpty(groupDetailModel.getMembers().get(a).getFcm_token())) {
                                        if (!groupDetailModel.getMembers().get(a).isIs_ban()) {
                                            new SendFcmNotification(groupDetailModel.getMembers().get(a).getFcm_token(), messageModel, null).execute();
                                        }
                                    }
                                }
                                a++;
                            }

                            // Send notification to admin if I am not admin
                            if (!groupDetailModel.getAdmin().getIdd().equalsIgnoreCase(String.valueOf(myPreferenceManager.getUser().getId()))) {
                                if (!Utility.isEmpty(groupDetailModel.getAdmin().getFcm_token())) {
                                    new SendFcmNotification(groupDetailModel.getAdmin().getFcm_token(), messageModel, null).execute();
                                }
                            }

                            // Emitting socket signal
                            messageModel.setMessage_type(MessageType.GROUP.getType());
                            SocketManager.sendOutgoingGroupMessage(messageModel, groupId);
                            attachment = "";
                            attachmentExtension = "";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {
                      //  Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        Toasty.error(getApplicationContext(), error).show();

                    }
                });
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] mimeTypes =
                        {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf",
                                "application/zip"};

                Intent intent = new Intent();
//                intent.setType("image/*");

                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture or Document"), PICK_IMAGE_REQUEST);
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

        SocketManager.attachIncomingGroupMessageListener(new SocketListener() {
            @Override
            public void onResponse(final String response) {
                Log.d("xxaa2", response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final MessageModel messageModel = new Gson().fromJson(response, MessageModel.class);
                        Log.d("xxaa", response);
                        if (messageModel.getMessage_type().equalsIgnoreCase(MessageType.GROUP.getType())
                                && messageModel.getSend_to().getIdd().equalsIgnoreCase(groupId)) {
                            httpManager.getMessageContent(messageModel.getId(), new HttpResponse() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        messageModel.setContent(new JSONObject(response).getString("content"));
                                        messageModel.getSend_from().setIs_online(true);
                                        messageModels.add(messageModel);
                                        singleMessageAdapter.notifyDataSetChanged();
                                        chatRv.scrollToPosition(messageModels.size() - 1);
                                        customLoadingDialog.dismiss();

                                        httpManager.markGroupChatAsRead(groupId, new HttpResponse() {
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
                                    Log.i("my_log", error);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void hideUI() {
        message.setVisibility(View.GONE);
        send.setVisibility(View.GONE);
        takePhoto.setVisibility(View.GONE);
        iconLeave.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Utility.gotoActivity(this, ChatsActivity.class);
    }

    @Override
    public void onDestroy() {
        SocketManager.userLeaveGroup(groupId);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {

                Uri uri = data.getData();
                String uriPath = uri.toString();

                String mime = getContentResolver().getType(uri);
                if (mime != null && (mime.equalsIgnoreCase("image/jpeg") || mime.equalsIgnoreCase("image/png"))) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    attachment = Utility.getStringImage(bitmap);

                    byte[] bytesImage = Base64.decode(attachment, Base64.DEFAULT);
                  /*
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(bytesImage)
                            .placeholder(R.drawable.file_placeholder)
                            .into(selectedImage);
                    */
                    Picasso.with(getApplicationContext()).load(String.valueOf(bytesImage))
                            .placeholder(R.drawable.file_placeholder)
                            .error(R.drawable.file_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(selectedImage);

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

                    selectedImage.setImageDrawable(getDrawable(R.drawable.file_placeholder));
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
    @Override
    public void onButtonClicked(String text) {

    }
}
