package com.jiuwfung.comp6239;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.jiuwfung.comp6239.main.fragments.EventsFragment;

public class StudentEventsActivity extends AppCompatActivity {

    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_events);

        mFragment = new EventsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.student_container , mFragment)
                .commit();

    }
}
