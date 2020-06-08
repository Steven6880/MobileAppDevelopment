package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jiuwfung.comp6239.helper.Application;
import com.jiuwfung.comp6239.helper.MyGlideEngine;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class EventActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    FirebaseAuth mAuth;
    StorageReference mStorage ;
    StorageReference mStoragepicture;
    DatabaseReference mDataevents;

    public SharedPreferences sharedEvents;
    public SharedPreferences.Editor editorEvents;

    @BindView(R.id.im_event_picture)
    ImageView mEventPicture;
    @BindView(R.id.choose_event_picture)
    TextView mEventTextView;
    @BindView(R.id.edit_event_title)
    EditText mEventTitle;
    @BindView(R.id.edit_event_description)
    EditText mEventDescription;
    @BindView(R.id.edit_event_detail)
    EditText mEventDetail;
    @BindView(R.id.edit_event_location)
    EditText mEventLocation;
    @BindView(R.id.edit_event_time)
    EditText mEventTime;
    @BindView(R.id.edit_date)
    EditText mEventDate;
    @BindView(R.id.event_button)
    LinearLayout mEventButton;

    List<Uri> mSelected;
    String mPicture;
    HashMap hashEvent;

    private static int INT_PICTURE=0;
    private static final int REQUEST_CODE_STORAGE = 1;
    private static final int REQUEST_CODE_PICTURE = 23;
    private static Uri resultUri;
    private static String STRING_IDENTITY;
    private boolean Editible;
    public static int MaxPicSize = 1024 * 1024 * 20;

    SharedPreferences sharedEventCreate;
    SharedPreferences.Editor editoreventcreate;
    SharedPreferences sharedIdentity;


    public static void show(Context context){
        context.startActivity(new Intent(context, EventActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDataevents = FirebaseDatabase.getInstance().getReference().child("Events");

        ButterKnife.bind(this);

        sharedEventCreate= getSharedPreferences("EventCreate",Context.MODE_PRIVATE);
        editoreventcreate = sharedEventCreate.edit();

        sharedEvents = getSharedPreferences("EventAccess" , Context.MODE_PRIVATE);
        editorEvents = sharedEvents.edit();
        Editible = sharedEvents.getBoolean("Editable" , false);
        Log.e("Editable" , Boolean.toString(Editible));

        hashEvent = new HashMap();


        sharedIdentity = getSharedPreferences("Identity" , Context.MODE_PRIVATE);
        STRING_IDENTITY = sharedIdentity.getString("Identity" , "");


        if(!Editible){
            mEventPicture.setImageResource(R.drawable.bg_src_tianjin);

            mEventPicture.setFocusable(false);
            mEventPicture.setFocusableInTouchMode(false);
            mEventButton.setVisibility(View.GONE);
            mEventTextView.setText("");
            mEventTitle.setFocusable(false);
            mEventTitle.setFocusableInTouchMode(false);
            mEventDescription.setFocusable(false);
            mEventDescription.setFocusableInTouchMode(false);
            mEventDetail.setFocusable(false);
            mEventDetail.setFocusableInTouchMode(false);
            mEventLocation.setFocusable(false);
            mEventLocation.setFocusableInTouchMode(false);
            mEventTime.setFocusable(false);
            mEventTime.setFocusableInTouchMode(false);
            mEventDate.setFocusable(false);
            mEventDate.setFocusableInTouchMode(false);

            mEventTitle.setText(sharedEvents.getString("Title" , "Title"));
            mEventDescription.setText(sharedEvents.getString("ShortDescription" , "ShortDescription"));
            mEventDetail.setText(sharedEvents.getString("Detail" , "Detail"));
            mEventDate.setText(sharedEvents.getString("Date" , "Date"));
            mEventLocation.setText(sharedEvents.getString("Location" , "Location"));
            mEventTime.setText(sharedEvents.getString("Time" , "Time"));

            mStorage.child("Event/"+sharedEvents.getString("Title" , "Title")+".jpg").getBytes(MaxPicSize)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
                            mEventPicture.setImageBitmap(bitmap);
                            Log.d("Load Event Picture.","Success!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EventActivity.this , "LoadView Event Picture!" , Toast.LENGTH_LONG).show();
                    Log.e("Load Event Picture." , "Failure!");
                }
            });

            editorEvents.remove("Title");
            editorEvents.remove("ShortDescription");
            editorEvents.remove("detail");
            editorEvents.remove("Date");
            editorEvents.remove("Location");
            editorEvents.remove("Time");
            editorEvents.commit();

        }


    }

    @OnClick(R.id.im_event_picture)
    public void onSelectEventPicture(){
        if(STRING_IDENTITY.equals("Manager")) {
            onRequestStorge();
        }else {
            return;
        }
    }

    @OnClick(R.id.event_button)
    public void onCreateEventChoose(){

        String eventtitle = mEventTitle.getText().toString();
        String eventdescription = mEventDescription.getText().toString();
        String eventdetail = mEventDetail.getText().toString();
        String eventlocation = mEventLocation.getText().toString();
        String eventtime = mEventTime.getText().toString();
        String eventdate = mEventDate.getText().toString();
        if(INT_PICTURE == 0){
            Toast.makeText(this , "Please Choose The Picture" , Toast.LENGTH_LONG).show();
            return;
        }
        if(eventtitle.isEmpty()){
            Toast.makeText(this , "Please Type Title" , Toast.LENGTH_LONG).show();
            return;
        }
        if(eventdescription.isEmpty()){
            Toast.makeText(this , "Please Type Description" , Toast.LENGTH_LONG).show();
            return;
        }
        if(eventdetail.isEmpty()){
            Toast.makeText(this , "Please Type Detail" , Toast.LENGTH_LONG).show();
            return;
        }
        if(eventlocation.isEmpty()){
            Toast.makeText(this , "Please Type Location" , Toast.LENGTH_LONG).show();
            return;
        }
        if(eventtime.isEmpty()){
            Toast.makeText(this , "Please Type Time" , Toast.LENGTH_LONG).show();
            return;
        }
        if(eventdate.isEmpty()){
            Toast.makeText(this , "Please Type Date" , Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PLease Check Event's Title");
        builder.setMessage("The title is :"+eventtitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                hashEvent.put("ShortDescription" , eventdescription);
                hashEvent.put("Detail", eventdetail);
                hashEvent.put("Location" , eventlocation);
                hashEvent.put("Time" , eventtime);
                hashEvent.put("Date" , eventdate);

                mDataevents.child(eventtitle).setValue(hashEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EventActivity.this, "upload event success!" , Toast.LENGTH_LONG).show();
                        Log.d("Event Database" , "add event success!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventActivity.this, "upload event picture failure!" + e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                        Log.e("Event Picture" , "add event picture failure!"+e.getLocalizedMessage());
                    }
                });

                mStoragepicture = mStorage.child(eventtitle+".jpg");
                mStoragepicture.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EventActivity.this , "upload picture success!" , Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventActivity.this , "upload picture failure!"+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                    }
                });

                Intent intent=new Intent();
                intent.setClass(EventActivity.this, EventChooseActivity.class);
                startActivity(intent);
                editoreventcreate.putString("Title" , eventtitle);
                editoreventcreate.putString("Description" , eventdescription);
                editoreventcreate.commit();

                onFinish();

            }
        }).setNegativeButton("Rewrite it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();
    }


    @AfterPermissionGranted(REQUEST_CODE_STORAGE )
    public void onRequestStorge(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.ACCESS_NETWORK_STATE};

        if(EasyPermissions.hasPermissions(this , perms)){
            Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG , MimeType.PNG))
                    .countable(false)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
                    .thumbnailScale(0.85f)
                    .imageEngine(new MyGlideEngine())
                    .forResult(REQUEST_CODE_PICTURE);
        }else {
            EasyPermissions.requestPermissions(this , "Please Modify The Correct Permissions" ,
                    REQUEST_CODE_STORAGE, perms );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this , "Please Go Setting To Modify The Permission." , Toast.LENGTH_LONG).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICTURE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            mPicture = Matisse.obtainPathResult(data).get(0);
            Toast.makeText(this,"get picture" , Toast.LENGTH_LONG).show();
            UCrop.Options options = new UCrop.Options();
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            options.setCompressionFormat( Bitmap.CompressFormat.PNG);
            options.setCompressionQuality(96);

            // get the Cache Path of ImageView in this app
            File temPath = Application.getPortraitTmpFile();

            UCrop.of(mSelected.get(0) , Uri.fromFile(temPath))
                    //length and width
                    .withAspectRatio(16 ,9)
                    //size of return
                    .withMaxResultSize(1920 , 1080)
                    .start(this);
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultUri = UCrop.getOutput(data);
            //Toast.makeText(getActivity() , resultUri.toString().substring(0,resultUri.toString().indexOf(".jpg")) , Toast.LENGTH_LONG).show();
            if(resultUri != null){
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void loadPortrait(Uri resultUri){
        Glide.with(this)
                .asBitmap()
                .load(resultUri)
                .into(mEventPicture);

        INT_PICTURE = 1;
    }

    private void onFinish(){
        this.finish();
    }
}
