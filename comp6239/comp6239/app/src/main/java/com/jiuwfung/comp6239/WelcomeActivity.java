package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.jiuwfung.comp6239.helper.MyGlideEngine;
import com.jiuwfung.comp6239.helper.WelcomeStorage;
import com.jiuwfung.comp6239.helper.WelcomeTrigger;
import com.jiuwfung.comp6239.welcome.fragments.LoginFragment;
import com.jiuwfung.comp6239.welcome.fragments.RegisterFragment;
import com.jiuwfung.comp6239.widget.PortraitView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import net.qiujuer.genius.ui.compat.UiCompat;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class WelcomeActivity<Private> extends AppCompatActivity implements WelcomeTrigger, WelcomeStorage ,EasyPermissions.PermissionCallbacks {

    Fragment mCurrentFragment;
    Fragment mLoginFragment;
    Fragment mRegisterFragment;

    public FirebaseAnalytics mFirebaseAnalytics;
    public FirebaseAuth mAuth;

    ImageView mBackGround;
    PortraitView mPortraitview;
    FrameLayout mLayContainer;

    private static final int REQUEST_CODE_PICTURE = 23;
    private static final int REQUEST_CODE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mBackGround = (ImageView) findViewById(R.id.im_bg);

        mCurrentFragment = mLoginFragment = new LoginFragment();
        mRegisterFragment = new RegisterFragment();

        mPortraitview = (PortraitView) findViewById(R.id.im_register_portrait);


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mLoginFragment)
                .add(R.id.lay_container, mRegisterFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .show(mLoginFragment)
                .hide(mRegisterFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.lay_container, mCurrentFragment)
//                .commit();



        onSetBackGround();

    }

    void onSetBackGround(){
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new DrawableImageViewTarget(mBackGround) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        if (resource == null) {
                            super.setResource(resource);
                            return;
                        }

                        Drawable drawable = DrawableCompat.wrap(resource);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.white_alpha_64),
                                PorterDuff.Mode.SCREEN);

                        super.setResource(drawable);
                    }
                });
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurrentFragment == mLoginFragment) {
            fragment = mRegisterFragment;
            mCurrentFragment = fragment;
            getSupportFragmentManager().beginTransaction()
                    .show(mRegisterFragment)
                    .hide(mLoginFragment)
                    .commit();
        } else {
            fragment = mLoginFragment;
            mCurrentFragment = fragment;
            getSupportFragmentManager().beginTransaction()
                    .show(mLoginFragment)
                    .hide(mRegisterFragment)
                    .commit();
        }
        onSetBackGround();
    }

    @Override
    public void welcomestorage() {
        onRequestStorge();
    }


    @AfterPermissionGranted(REQUEST_CODE_STORAGE )
    public void onRequestStorge(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(EasyPermissions.hasPermissions(this , perms)){

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCurrentFragment.onActivityResult(requestCode , resultCode , data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

