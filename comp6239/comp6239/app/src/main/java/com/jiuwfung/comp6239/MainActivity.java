package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.StorageReference;
import com.jiuwfung.comp6239.helper.NavHelper;
import com.jiuwfung.comp6239.main.fragments.EventsFragment;
import com.jiuwfung.comp6239.main.fragments.HomeFragment;
import com.jiuwfung.comp6239.main.fragments.VerifyFragment;
import com.jiuwfung.comp6239.widget.PortraitView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer>{

    public NavHelper mNavHelper;
    public StorageReference mStoragereference;

    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    AppBarLayout mAppbar;

    TextView mTitle;

    FrameLayout mContainer;

    FloatActionButton mFloat;

    BottomNavigationView mNavigation;

    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化底部工具类
        // 初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(HomeFragment.class, R.string.title_home))
                .add(R.id.action_events, new NavHelper.Tab<>(EventsFragment.class, R.string.title_events))
                .add(R.id.action_verify, new NavHelper.Tab<>(VerifyFragment.class, R.string.title_verify));

        mAppbar = (AppBarLayout) findViewById(R.id.appbar);
        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mTitle = (TextView)findViewById(R.id.text_title);
        mContainer = (FrameLayout) findViewById((R.id.lay_container));
        mFloat = (FloatActionButton) findViewById(R.id.btn_floataction);

        // 传到线面的onNaviagation...处理
        mNavigation.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //从底部接管menu，进行手动触发第一次点击
        Menu menu = mNavigation.getMenu();
        //首次触发Home
        menu.performIdentifierAction(R.id.action_home, 0);


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

        // 从额外字段中取出我们的Title资源Id
        mTitle.setText(newTab.extra);


        // 对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            // 主界面时隐藏
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            // transY 默认为0 则显示
            if (Objects.equals(newTab.extra, R.string.title_events)) {
                // 群
                mFloat.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                // 联系人
                mFloat.setImageResource(R.drawable.ic_contact_add);
                transY = Ui.dipToPx(getResources(), 76);
            }
        }

        // 开始动画
        // 旋转，Y轴位移，弹性差值器，时间
        mFloat.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        AccountActivity.show(this);
    }
}
