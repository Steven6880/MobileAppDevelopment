package com.jiuwfung.comp6239;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;

public class EventChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_choose);

        ButterKnife.bind(this);
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, EventChooseActivity.class));
    }
}
