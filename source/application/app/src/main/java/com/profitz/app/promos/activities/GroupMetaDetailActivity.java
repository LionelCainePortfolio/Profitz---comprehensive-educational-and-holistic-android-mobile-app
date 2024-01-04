package com.profitz.app.promos.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.profitz.app.R;
import com.profitz.app.data.model.GroupDetailModel;
import com.profitz.app.data.model.GroupRequestsModel;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PictureMessageDialog;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogGroupAddUser;
import com.profitz.app.promos.fragments.BottomSheetDialogGroupEdit;
import com.profitz.app.promos.fragments.BottomSheetDialogGroupMemberMenagment;
import com.profitz.app.promos.fragments.BottomSheetDialogGroupRequestAcceptOrReject;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.MyGroupRequestsFragment;
import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GroupMetaDetailActivity extends AppCompatActivity implements BottomSheetDialogGroupRequestAcceptOrReject.BottomSheetListener, BottomSheetDialogGroupEdit.BottomSheetListener, BottomSheetDialogGroupMemberMenagment.BottomSheetListener, BottomSheetDialogGroupAddUser.BottomSheetListener,  BottomSheetDialogReportBug.BottomSheetListener  {
    private String groupId;
    private TextView groupName, numberOfMembers, numberOfRequests;
    private ImageView back;
    private HttpManager httpManager;
    private CustomLoadingDialog customLoadingDialog;
    private GroupDetailModel groupDetailModel;
    private RecyclerView usersRv;
    private MembersListAdapter membersListAdapter;
    private ArrayList<GroupRequestsModel> groupRequestsModels;
    private Uri filePath;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 71;
    private RelativeLayout hide_if_zero;
    private ImageView iconEdit, groupImage;
    private TextView btnAddMember;
    private Button deleteGroup;
    private MyPreferenceManager myPreferenceManager;
    private boolean amIAdmin;
    private int groupRequests;
    private static final int LOCATION_REQUEST = 222;
    public static final int PICK_IMAGE = 2;
    int serverResponseCode = 0;
    private String Document_img1="";

    private RelativeLayout search_airdrop_edit_text_rl;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ImageView groupImageAnimated;
    private MyGroupRequestsFragment myGroupRequestsFragment;
    private RelativeLayout request_participants_rl;
    private User user2;
    private MessageModel downloadingMessageModel;
    private static final int REQUEST_EXTERNAL_STORAGE = 104;
    private SwipeRefreshLayout swipeRefresh;
    private Context context;
    BottomSheetDialogGroupEdit bottomSheetDialogGroupEdit = new BottomSheetDialogGroupEdit();
    BottomSheetDialogGroupAddUser bottomSheetDialogGroupAddUser = new BottomSheetDialogGroupAddUser();


    private final Runnable removeNotifierRequests = new Runnable() {
        @Override
        public void run() {
            View notifier = findViewById(R.id.notifier);
            notifier.setVisibility(View.GONE);
        }
    };
    private final DownloadAttachmentListener downloadAttachmentListener = new DownloadAttachmentListener() {
        @Override
        public void onDownloadClick(MessageModel messageModel) {
            downloadingMessageModel = messageModel;
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        GroupMetaDetailActivity.this,
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
         //   Toast.makeText(getApplicationContext(), "Plik został zapisany w \\Galerii\\" + getResources().getString(R.string.app_name), Toast.LENGTH_LONG).show();
            Toasty.success(getApplicationContext(), "Plik został zapisany w telefonie w folderze\\Pictures\\" + getResources().getString(R.string.app_name)).show();

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
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.gold_gradient_shape, null);
        getWindow().setBackgroundDrawable(background);

        setContentView(R.layout.activity_group_meta_detail);

        SocketManager.getInstance();

        groupRequests = 0;

        if (getIntent() != null) {
            groupRequests = getIntent().getIntExtra("groupRequests", 0);
        }


       myGroupRequestsFragment = new MyGroupRequestsFragment();
        //myGroupRequestsFragment.setRemoveNotifier(removeNotifierRequests);
      FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame_layout_for_groups_fragment, myGroupRequestsFragment, "1").commit();

        user2 = MyPreferenceManager.getInstance(this).getUser();
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
                    args.putString("source","GroupMetaDetailActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////

        myPreferenceManager = new MyPreferenceManager(this);
        groupDetailModel = new GroupDetailModel();
        groupDetailModel.setMembers(new ArrayList<User>());
        customLoadingDialog = new CustomLoadingDialog(GroupMetaDetailActivity.this);
        httpManager = new HttpManager(this);
        context = this;
        groupName = findViewById(R.id.groupName);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        groupImage = findViewById(R.id.groupImage);
        groupImageAnimated = findViewById(R.id.groupImageAnim);
        hide_if_zero=findViewById(R.id.hide_if_zero);
        request_participants_rl=findViewById(R.id.request_participants_rl);
        iconEdit = findViewById(R.id.iconEdit);
        deleteGroup = findViewById(R.id.deleteGroup);
        numberOfMembers = findViewById(R.id.numberOfMembers);
        numberOfRequests = findViewById(R.id.numberOfRequests);
        back = findViewById(R.id.back);
        search_airdrop_edit_text_rl = findViewById(R.id.search_airdrop_edit_text_rl);
        btnAddMember = findViewById(R.id.btnAddMember);
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("groupId",groupId);
                args.putString("groupName", groupName.getText().toString());
                bottomSheetDialogGroupAddUser.setArguments(args);
                bottomSheetDialogGroupAddUser.show(getSupportFragmentManager(), bottomSheetDialogGroupAddUser.getTag());


            }
        });

        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isEmpty(Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture())) {
                    MessageModel messageModel = new MessageModel();
                    messageModel.setId(groupId);
                    messageModel.setAttachment(Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture());
                    messageModel.setContent(groupDetailModel.getDetails().getGroup_name());

                    new PictureMessageDialog(GroupMetaDetailActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                            .setDownloadAttachmentListener(downloadAttachmentListener)
                            .setMessageModel(messageModel)
                            .show();
                }
            }
        });

        iconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();
                args.putString("groupId",groupId);
                bottomSheetDialogGroupEdit.setArguments(args);
                bottomSheetDialogGroupEdit.show(getSupportFragmentManager(), bottomSheetDialogGroupEdit.getTag());


            }
        });

        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(GroupMetaDetailActivity.this);
                alert.setTitle("Usuń grupę");
                alert.setMessage("Jesteś pewien/a, że chcesz usunmąć tę grupę? Tej operacji nie będzie można cofnąć.");
                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        customLoadingDialog.show();
                        httpManager.deleteGroup(groupId, new HttpResponse() {
                            @Override
                            public void onSuccess(String response) {
                                customLoadingDialog.dismiss();
                                dialog.dismiss();
                              // Toast.makeText(getApplicationContext(), "Grupa została pomyślnie usunięta.", Toast.LENGTH_LONG).show();
                                Toasty.success(getApplicationContext(), "Grupa została usunięta.").show();

                                SocketManager.groupDeleted(groupId);
                                Utility.gotoActivity(GroupMetaDetailActivity.this, ChatsActivity.class);
                            }

                            @Override
                            public void onError(String error) {
                                customLoadingDialog.dismiss();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });

        usersRv = findViewById(R.id.praticipantsRv);
        usersRv.setHasFixedSize(true);
        usersRv.setLayoutManager(new GridLayoutManager(context,4));
        membersListAdapter = new MembersListAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = usersRv.getChildAdapterPosition(view);
                String userId = String.valueOf(groupDetailModel.getMembers().get(index).getId());
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("groupId", groupId);
              //  Utility.gotoActivity(GroupMetaDetailActivity.this, intent);
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (amIAdmin) {
                    int index = usersRv.getChildAdapterPosition(view);
                    final User user = groupDetailModel.getMembers().get(index);
                    if (!user.getId().equals(user2.getIdd())){

                    Bundle args = new Bundle();
                    BottomSheetDialogGroupMemberMenagment bottomSheetDialogGroupMemberMenagment = new BottomSheetDialogGroupMemberMenagment();
                    args.putInt("index",index);
                    args.putString("user_id", user.getId());
                    args.putString("fcm_token", user.getFcm_token());
                    args.putString("groupId",groupId);
                    args.putString("groupName",groupDetailModel.getDetails().getGroup_name());
                    int myInt = user.isIs_ban() ? 1 : 0;
                    args.putInt("isUserBanned", myInt);
                    bottomSheetDialogGroupMemberMenagment.setArguments(args);
                    bottomSheetDialogGroupMemberMenagment.show(getSupportFragmentManager(), bottomSheetDialogGroupMemberMenagment.getTag());

                    }
                }
                return true;
            }
        });
        usersRv.setAdapter(membersListAdapter);

        if (getIntent() != null) {
            groupId = getIntent().getStringExtra("groupId");

            customLoadingDialog.show();
            getGroupDetail();
            getUserGroupRequests();
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getGroupDetail();
                    getUserGroupRequests();

                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }
    public void respondToGroupRequestActivity(final int index, final String status){
        myGroupRequestsFragment.respondToGroupRequestFragment(index,status);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(LOCATION_REQUEST)
    private void checkLocationRequest() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(context, perms)) {
            // Already have permission, do the thing

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"Udziel uprawnień, by móc zmienić avatar.",
                    LOCATION_REQUEST, perms);
        }
    }
    public void selectImage() {
        // final CharSequence[] options = { "Zrób zdjęcie", "Wybierz z galerii","Anuluj" };
        final CharSequence[] options = {"Wybierz z galerii","Anuluj" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        // AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Dodaj avatar");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
               /* if (options[item].equals("Zrób zdjęcie"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } */
                if (options[item].equals("Wybierz z galerii"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Wybierz zdjęcie grupy"), PICK_IMAGE_REQUEST);
                }
                else if (options[item].equals("Anuluj")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("requestCode", String.valueOf(requestCode));
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    groupDetailModel.getDetails().setPicture(Utility.getStringImage(bitmap));
                    byte[] bytesImage = Base64.decode(Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture(), Base64.DEFAULT);

                    bottomSheetDialogGroupEdit.changePicture(bytesImage);
                    BitMapToString(bitmap);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, System.currentTimeMillis() + ".png");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 2) {
                Uri selectedImageUri = data.getData();

                try {
                    bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri));
                    groupDetailModel.getDetails().setPicture(Utility.getStringImage(bitmap));
                    byte[] bytesImage = Base64.decode(Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture(), Base64.DEFAULT);

                    bottomSheetDialogGroupEdit.changePicture(bytesImage);
                    // uploadFile(selectedImageUri);
                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // messageText.setText("uploading started.....");
                                }
                            });


                            String fileName = getRealPathFromURI_API19(context,selectedImageUri);
                            String path = getRealPathFromURI_API19(context,selectedImageUri);
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
                                    URL url = new URL("https://yoururl.com/api/UploadToServer.php?group_id="+ groupId);

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
                                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                            + fileName + "\"" + lineEnd);

                                    dos.writeBytes(lineEnd);

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
                                    URL url22 = new URL("https://yoururl.com/api/scp/groups/"+ groupId +".png");
                                    bitmap = BitmapFactory.decodeStream(url22.openConnection().getInputStream());
                                    groupDetailModel.getDetails().setPicture(Utility.getStringImage(bitmap));
                                    byte[] bytesImage = Base64.decode(Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture(), Base64.DEFAULT);

                                    bottomSheetDialogGroupEdit.changePicture(bytesImage);

                                    Log.i("uploadFile", "HTTP Response is : "
                                            + serverResponseMessage + ": " + serverResponseCode);
                                  //  Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Zdjęcie grupy zostało zaaktualizowane. Zmiany w niektórych miejsach mogą pojawić się do kilku godzin.", Snackbar.LENGTH_LONG);
                                   // snackbar.show();
                                    Toasty.success(context, "Zdjęcie grupy zostało zaaktualizowane. Zmiany w niektórych miejsach mogą pojawić się do kilku godzin.").show();

                                    //close the streams //
                                    fileInputStream.close();
                                    dos.flush();
                                    dos.close();

                                } catch (MalformedURLException ex) {


                                    ex.printStackTrace();
                                   /// Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                                 //   snackbar.show();
                                    Toasty.error(context, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.").show();

                                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                                } catch (Exception e) {
                                    Log.e("Upload file to server", "error: " + e);
                                 //   Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                                 //   snackbar.show();
                                    Toasty.error(context, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.").show();

                                    e.printStackTrace();


                                }


                            } // End else block

                        }
                    }).start();
                } catch (FileNotFoundException e) {
                  //  Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                 //   snackbar.show();
                    Toasty.error(context, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.").show();

                    e.printStackTrace();
                }


            }
        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void getUserGroupRequests() {
        httpManager.viewUserGroupRequests(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                Type type = new TypeToken<ArrayList<GroupRequestsModel>>() {}.getType();
                groupRequestsModels = new Gson().fromJson(response, type);
                swipeRefresh.setRefreshing(false);
                customLoadingDialog.dismiss();
                if (groupRequestsModels.size() > 0) {
                    request_participants_rl.setVisibility(View.VISIBLE);
                    numberOfRequests.setText(String.valueOf(groupRequestsModels.size()));
                } else {
                    request_participants_rl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                Log.i("my_log", error);
            }
        });
    }
    public void getGroupDetail() {
        httpManager.getGroupMetaDetail(groupId, new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("error")) {
                        String message = jsonObject.getString("message");
                     //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Toasty.normal(getApplicationContext(), message).show();

                        Utility.gotoActivity(GroupMetaDetailActivity.this, ChatsActivity.class);
                    } else {
                        groupDetailModel = new Gson().fromJson(response, GroupDetailModel.class);
                        groupName.setText(groupDetailModel.getDetails().getGroup_name());
                        // +1 is for admin
                        numberOfMembers.setText(String.valueOf(groupDetailModel.getMembers().size()));
                        if ((groupDetailModel.getMembers().size()+1)>0) {
                            hide_if_zero.setVisibility(View.GONE);
                        }
                        else{
                            hide_if_zero.setVisibility(View.VISIBLE);
                        }
                        membersListAdapter.notifyDataSetChanged();

                        Utility.showImageAnimated(getApplicationContext(), Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture(), groupImage, groupImageAnimated);

                        usersRv.scrollToPosition(groupDetailModel.getMembers().size() - 1);
                        User user2 = MyPreferenceManager.getInstance(context).getUser();
                        String userid = user2.getId();
                        amIAdmin = groupDetailModel.getAdmin().getIdd().equalsIgnoreCase(userid);
                        // allow delete group only for admin
                        if (!amIAdmin) {
                            deleteGroup.setVisibility(View.GONE);
                            iconEdit.setVisibility(View.GONE);
                            btnAddMember.setVisibility(View.GONE);



                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            search_airdrop_edit_text_rl.setLayoutParams(lp);
                        }
                    }

                    swipeRefresh.setRefreshing(false);
                    customLoadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                //
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
        intent.putExtra("groupId", groupId);
        Utility.gotoActivity(GroupMetaDetailActivity.this, intent);
    }

    @Override
    public void onButtonClicked(String text) {

    }

    private class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.ViewHolder> {
        private final Context context;
        private final View.OnClickListener onUserSelected;
        private final View.OnLongClickListener onLongClickListener;

        MembersListAdapter(Context context, View.OnClickListener onUserSelected, View.OnLongClickListener onLongClickListener) {
            this.context = context;
            this.onUserSelected = onUserSelected;
            this.onLongClickListener = onLongClickListener;
        }

        @NonNull
        @Override
        public MembersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_member_list_single, parent, false);
            return new MembersListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MembersListAdapter.ViewHolder holder, int position) {
            User user = groupDetailModel.getMembers().get(position);
            holder.username.setText(user.getUsername());
            Utility.showImageAnimated(context, user.getPicture(), holder.memberImage, holder.imageAnim);

            if (user.getId().equals(groupDetailModel.getAdmin().getId()))
            {
                holder.group_admin.setVisibility(View.VISIBLE);
            }
            else{
                holder.group_admin.setVisibility(View.GONE);
            }


            if (user.isIs_ban()) {
             //   holder.iconBlock.setVisibility(View.VISIBLE);
            } else {
              //  holder.iconBlock.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return groupDetailModel.getMembers().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView username;
            CircleImageView memberImage;
            ImageView imageAnim, iconBlock;
            View onlineNotifier;
            TextView group_admin;
            ViewHolder(View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.username);
                memberImage = itemView.findViewById(R.id.memberImage);
                imageAnim = itemView.findViewById(R.id.imageAnimated);
                group_admin = itemView.findViewById(R.id.group_admin);
                //     iconBlock = itemView.findViewById(R.id.iconBlock);
                onlineNotifier = itemView.findViewById(R.id.onlineNotifier);
                if (onUserSelected != null) {
                    itemView.setOnClickListener(onUserSelected);
                }
                if (onLongClickListener != null) {
                    itemView.setOnLongClickListener(onLongClickListener);
                }
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
}
