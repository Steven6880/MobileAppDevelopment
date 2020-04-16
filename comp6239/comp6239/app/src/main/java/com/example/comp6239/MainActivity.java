package com.example.comp6239;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.common.app.BaseActivity;
import com.example.common.widget.a.portraitview;
import com.example.comp6239.fragments.main.ActiveFragment;
import com.example.comp6239.fragments.main.EventsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    portraitview mPortrait;

    @BindView(R.id.text_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_floataction)
    FloatActionButton mAction;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mNavigation.setOnNavigationItemSelectedListener(this);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new CustomViewTarget<View, Drawable>(mLayAppbar) {
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

    public void initDat(){ super.initData();}

    @OnClick(R.id.im_search)
    void onSearchMenuClick(){

    }

    @OnClick(R.id.btn_floataction)
    void onFloatActionClick(){

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_home){ // take action according button's id
            mTitle.setText(R.string.title_home); // set the title of the fragment

            ActiveFragment activeFragment = new ActiveFragment(); //new a ActiveFragment as the main page

            getSupportFragmentManager().beginTransaction().add(R.id.lay_container , activeFragment).commit(); //hard method, to add the fragment.
        }else if( item.getItemId() == R.id.action_events){
            mTitle.setText(R.string.title_events);

            EventsFragment eventsFragment = new EventsFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.lay_container , eventsFragment).commit();
        }
        return true;
    }
}
