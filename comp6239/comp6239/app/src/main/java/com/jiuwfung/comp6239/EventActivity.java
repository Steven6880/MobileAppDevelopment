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
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jiuwfung.comp6239.helper.Application;
import com.jiuwfung.comp6239.helper.MyGlideEngine;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class EventActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.im_event_picture)
    ImageView mEventPicture;
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

    private static int INT_PICTURE=0;
    private static final int REQUEST_CODE_STORAGE = 1;
    private static final int REQUEST_CODE_PICTURE = 23;
    private static Uri resultUri;

    SharedPreferences sharedEventCreate;
    SharedPreferences.Editor editoreventcreate;


    public static void show(Context context){
        context.startActivity(new Intent(context, EventActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);

        sharedEventCreate= getSharedPreferences("EventCreate",Context.MODE_PRIVATE);
        editoreventcreate = sharedEventCreate.edit();

    }

    @OnClick(R.id.im_event_picture)
    public void onSelectEventPicture(){
        onRequestStorge();
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
                Intent intent=new Intent();
                intent.setClass(EventActivity.this, EventChooseActivity.class);
                startActivity(intent);
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
}
