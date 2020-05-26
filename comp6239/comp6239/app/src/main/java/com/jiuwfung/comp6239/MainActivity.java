package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jiuwfung.comp6239.helper.NavHelper;
import com.jiuwfung.comp6239.main.fragments.EventsFragment;
import com.jiuwfung.comp6239.main.fragments.HomeFragment;
import com.jiuwfung.comp6239.main.fragments.VerifyFragment;
import com.jiuwfung.comp6239.widget.PortraitView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.io.File;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer>{

    public SharedPreferences sharedIdentity;
    public SharedPreferences.Editor editorIdentity;
    public SharedPreferences sharedStudent;
    public SharedPreferences.Editor editorStudent;
    public SharedPreferences sharedParent;
    public SharedPreferences.Editor editorParent;
    public SharedPreferences sharedEvents;
    public SharedPreferences.Editor editorEvents;


    public FirebaseAuth mAuth ;
    public FirebaseUser mUser;
    public StorageReference mStorage ;
    public StorageReference mStoragepicture ;
    public DatabaseReference mDatayears ;
    public DatabaseReference mDatastudents ;
    public DatabaseReference mDataaccounts;

    public static int INT_IDENTITY = 0;
    public static String STRING_IDENTITY = "Manager";
    public static String STRING_UID;
    public static File fileDownloadportrait;
    public static int MaxPicSize = 1024 * 1024 * 20;
    public Set<String> SetEvents;
    public Set<String> SetChildren;

    public NavHelper mNavHelper;

    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.text_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.btn_floataction)
    FloatActionButton mFloat;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        sharedIdentity = getSharedPreferences("Identity" , Context.MODE_PRIVATE);
        editorIdentity = sharedIdentity.edit();

        sharedStudent = getSharedPreferences("Student",Context.MODE_PRIVATE);
        editorStudent = sharedStudent.edit();

        sharedParent = getSharedPreferences("Parent" , Context.MODE_PRIVATE);
        editorParent = sharedParent.edit();

        sharedEvents = getSharedPreferences("EventAccess" , Context.MODE_PRIVATE);
        editorEvents = sharedEvents.edit();

        initChannel();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.e("MainActivity", "Key: " + key + " Value: " + value);
            }
        }

        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("FCMDemo", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.e("FCMDemo", "token: " + token);
                });
        if(INT_IDENTITY == 1){
            subscribestudent();
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        //checkpoint
        STRING_UID = mUser.getUid();
        //checkpoint
        mStoragepicture = mStorage.child("Accounts/"+STRING_UID+".jpg");
        mDatayears = FirebaseDatabase.getInstance().getReference().child("Years");
        mDatastudents = FirebaseDatabase.getInstance().getReference().child("Students");
        mDataaccounts = FirebaseDatabase.getInstance().getReference().child("Account");

        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);

        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(HomeFragment.class, R.string.title_home))
                .add(R.id.action_events, new NavHelper.Tab<>(EventsFragment.class, R.string.title_events));

        mNavigation.setOnNavigationItemSelectedListener(this);

        //Load Portrait
        fileDownloadportrait = new File(getApplicationContext().getFilesDir() , STRING_UID+".jpg");

        mStoragepicture.getBytes(MaxPicSize).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
                mPortraitView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this , "LoadView Failue!" , Toast.LENGTH_LONG).show();
            }
        });

        mStoragepicture.getFile(fileDownloadportrait)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("download sucess" , taskSnapshot.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failure" , e.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);

        if(STRING_IDENTITY==null) {
            getIdentity();
        }


        if(fileDownloadportrait!=null){
            loadPortrait(Uri.fromFile(fileDownloadportrait));
        }

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new CustomViewTarget<View, Drawable>(mAppbar) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        this.view.setBackground(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void subscribestudent(){
        FirebaseMessaging.getInstance().subscribeToTopic(sharedStudent.getString("Group" , ""))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    String TAG = "subscribe group";
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to group";
                        if (!task.isSuccessful()) {
                            msg = "Failed to subscribe to group topic";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic(sharedStudent.getString("Year" , ""))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    String TAG = "subscribe year";
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to year";
                        if (!task.isSuccessful()) {
                            msg = "Failed to subscribe to year topic";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("ChannelId",
                    "ChannelName",
                    NotificationManager.IMPORTANCE_HIGH);

            //渠道描述
            mChannel.setDescription("Channel desc");
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.cancelAll();
        }
    }

    @OnClick(R.id.btn_floataction)
    public void onCreateEvent(){
        EventActivity.show(this);
        editorEvents.putBoolean("Editable" , true);
        editorEvents.commit();
    }


    /**
     * 转接底部菜单工具流到NavHelper中
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performClickMenu(item.getItemId());
    }


    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {

        mTitle.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_events)) {
                if(INT_IDENTITY==0){
                    mFloat.setImageResource(R.drawable.ic_group_add);
                    rotation = -360;
                }else {
                    transY = Ui.dipToPx(getResources(), 76);
                }
            }
        }

        mFloat.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();
    }

    public void loadPortrait(Uri resultUri){
        Glide.with(this)
                .asBitmap()
                .load(resultUri)
                .centerCrop()
                .into(mPortraitView);
    }

    public void getIdentity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PLease Wait!");
        builder.setMessage("Getting Your Identity!");
        builder.setCancelable(false);
        Toast.makeText(this , "Starting Get Identity" , Toast.LENGTH_LONG).show();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

        mDataaccounts.child(STRING_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                STRING_IDENTITY = dataSnapshot.child("Identity").getValue().toString();
                Toast.makeText(MainActivity.this , STRING_IDENTITY , Toast.LENGTH_LONG).show();
                if(STRING_IDENTITY.equals("Manager")){
                    INT_IDENTITY=0;
                    editorIdentity.putString("Identity" , "Manager");
                    Toast.makeText(MainActivity.this , STRING_IDENTITY , Toast.LENGTH_LONG).show();
                    editorIdentity.commit();
                }
                if(STRING_IDENTITY.equals("Student")){
                    INT_IDENTITY=1;
                    editorIdentity.putString("Identity" , "Student");

                    Toast.makeText(MainActivity.this , STRING_IDENTITY , Toast.LENGTH_LONG).show();

                    editorStudent.putString("Year" , dataSnapshot.child("Year").getValue().toString());
                    editorStudent.putString("Email" , dataSnapshot.child("Email").getValue().toString());
                    editorStudent.putString("Group" , dataSnapshot.child("Group").getValue().toString());
                    editorStudent.putString("StudentID" , dataSnapshot.child("StudentID").getValue().toString());

                    mDatayears.child(dataSnapshot.child(dataSnapshot.child("Year").getValue().toString()).getValue().toString())
                            .child(dataSnapshot.child(dataSnapshot.child("Group").getValue().toString()).getValue().toString())
                            .child(dataSnapshot.child(dataSnapshot.child("StudentID").getValue().toString()).getValue().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    editorStudent.putString("FirstName" , dataSnapshot.child("FirstName").getValue().toString());
                                    editorStudent.putString("LastName" , dataSnapshot.child("LastName").getValue().toString());
                                    editorParent.putString("FatherName" , dataSnapshot.child("Father").child("FirstName").getValue().toString());
                                    editorParent.putString("FatherNumber" , dataSnapshot.child("Father").child("Number").getValue().toString());
                                    editorParent.putString("MotherName" , dataSnapshot.child("Mother").child("FirstName").getValue().toString());
                                    editorParent.putString("MotherNumber" , dataSnapshot.child("Mother").child("Number").getValue().toString());
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                    editorIdentity.commit();
                    editorStudent.commit();
                    editorParent.commit();

                }
                if(STRING_IDENTITY.equals("Parent")){
                    INT_IDENTITY=2;
                    Toast.makeText(MainActivity.this , STRING_IDENTITY , Toast.LENGTH_LONG).show();
                    editorIdentity.putString("Identity" , "Parent");

                    editorParent.putString("Email" , dataSnapshot.child("Email").getValue().toString());
                    editorParent.putString("FirstName" , dataSnapshot.child("FirstName").getValue().toString());
                    editorParent.putString("LastName" , dataSnapshot.child("LastName").getValue().toString());
                    editorParent.putString("Number" , dataSnapshot.child("Number").getValue().toString());
                    editorParent.putString("Relation" , dataSnapshot.child("Relation").getValue().toString());

                    for (DataSnapshot tem_datasnapshot : dataSnapshot.child("Children").getChildren()){
                        SetChildren.add(tem_datasnapshot.getKey().toString());
                    }

                    editorParent.putStringSet("Children" , SetChildren);

                    editorIdentity.commit();
                    editorParent.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this , "Identity failure" , Toast.LENGTH_LONG).show();
            }
        });
    }

//    @OnClick(R.id.im_portrait)
//    void onPortraitClick(){
//        AccountActivity.show(this);
//    }
}
