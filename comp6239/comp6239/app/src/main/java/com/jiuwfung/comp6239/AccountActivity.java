package com.jiuwfung.comp6239;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jiuwfung.comp6239.account.UpdateInfoFragment;

public class AccountActivity extends AppCompatActivity {

    Fragment mUpdateInforFragment;

    public static void show(Context context){
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mUpdateInforFragment = new UpdateInfoFragment();
    }
}
