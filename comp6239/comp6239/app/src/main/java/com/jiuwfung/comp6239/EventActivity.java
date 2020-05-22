package com.jiuwfung.comp6239;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.event_button)
    Button mEventButton;

    public static void show(Context context){
        context.startActivity(new Intent(context, EventActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_event);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.event_button)
    public void onCreateEventChoose(){
        EventChooseActivity.show(this);
    }
}
