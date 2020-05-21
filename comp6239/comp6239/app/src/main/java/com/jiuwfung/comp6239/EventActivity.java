package com.jiuwfung.comp6239;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class EventActivity extends AppCompatActivity {

    public static void show(Context context){
        context.startActivity(new Intent(context, EventActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_event);
    }
}
