package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jiuwfung.comp6239.events.recycler.EventChooseAdapter;
import com.jiuwfung.comp6239.events.recycler.EventChooseAdapterItem;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventChooseActivity extends AppCompatActivity {

    public SharedPreferences sharedIdentity;

    public HashMap hashStudent;

    @BindView(R.id.list_y1g1)
    ListView mEventChooseList;

    public static String userIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_choose);

        sharedIdentity = getSharedPreferences("Identity" , Context.MODE_PRIVATE);
        userIdentity=sharedIdentity.getString("Identity","");

        hashStudent = new HashMap();

        ButterKnife.bind(this);

        mEventChooseList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        mEventChooseList.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener());

        onUpdateList();
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, EventChooseActivity.class));
    }

    public void onUpdateList(){
        ArrayList<EventChooseAdapterItem> listEventChooseAdapter = new ArrayList<EventChooseAdapterItem>();
        FirebaseDatabase.getInstance().getReference().child("Years")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot year_datasnapshot: dataSnapshot.getChildren()){
                            for(DataSnapshot group_datasnapshot: year_datasnapshot.getChildren()){
                                for(DataSnapshot student_datasnapshot: group_datasnapshot.getChildren()){
                                    String StudentID = student_datasnapshot.getKey();
                                    String StudentName = student_datasnapshot.child("FirstName").getValue().toString();
                                    EventChooseAdapterItem item = new EventChooseAdapterItem(StudentID , StudentName);
                                    listEventChooseAdapter.add(item);
                                    Log.d("StudentaID" , StudentID);
                                    Log.d("StudentName" , StudentName);
                                }
                            }
                            mEventChooseList.setAdapter(new mEventChooseAdapter(listEventChooseAdapter));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private class mEventChooseAdapter extends BaseAdapter{

        private ArrayList<EventChooseAdapterItem> listEventChooseAdapter;

        public mEventChooseAdapter(ArrayList<EventChooseAdapterItem> listEventChooseAdapter){
            this.listEventChooseAdapter = listEventChooseAdapter;

        }

        @Override
        public int getCount() {
            return listEventChooseAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.card_view_choosestudents , null);
            boolean flag = false;

            EventChooseAdapterItem item = listEventChooseAdapter.get(position);


            TextView mStudentID = (TextView)v.findViewById(R.id.card_student_id);
            TextView mStudentName = (TextView)v.findViewById(R.id.card_student_name);
            CheckBox mChecckImage = (CheckBox) v.findViewById(R.id.card_student_checkbox);

            mStudentID.setText(item.ID);
            mStudentName.setText(item.Name);
            mChecckImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        hashStudent.put(item.ID , item.Name);
                        Toast.makeText(EventChooseActivity.this , "Add " + item.ID + " To Event!" , Toast.LENGTH_LONG).show();
                    }else {
                        hashStudent.remove(item.ID , item.Name);
                        Toast.makeText(EventChooseActivity.this , "Remove " + item.ID + " To Event!" , Toast.LENGTH_LONG).show();
                    }
                }
            });


            return v;
        }
    }
}
