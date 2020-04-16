package com.example.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Group Huifeng, Kejia, Yunheng
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          if(initArgs(getIntent().getExtras())) { //trans data from an activity to another

            // get page's id and set it into activity
            int layId = getContentLayoutId();
            setContentView(layId);

            initWidget();
            initData();
        }else {
            finish();
        }

    }

    // init paramaters
    public boolean initArgs(Bundle bundle){
        return true;
    }

    // init Windows
    public void initWindows(){
    }

    // @Return Id of source
    public abstract int getContentLayoutId();

    // init activity
    public void initWidget(){
        //绑定到当前
        ButterKnife.bind(this);
    }

    //init data
    public void initData(){
    }

    // click back button of the page, finish it.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    // click back of the phone, finish the page.
    @Override
    public void onBackPressed() {
        // get all fragments of this activity

        @SuppressLint("RestrictedApi")

        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        // check the fragment is empty or not
        if(fragments!=null && fragments.size()>0){
            for (Fragment fragment : fragments) {
                // check the type of the fragment
                if(fragment instanceof com.example.common.app.Fragment){ //if the fragment is this app's fragment
                    if(((com.example.common.app.Fragment) fragment).onBackPressed()){ // if block happens 如果有拦截
                        return;
                    }
                }
                
            }
        }

        // if block does not happen, 如果没有拦截
        super.onBackPressed();

        finish();
    }
}
