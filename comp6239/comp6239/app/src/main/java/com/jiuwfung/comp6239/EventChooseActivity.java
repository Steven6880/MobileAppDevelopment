package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jiuwfung.comp6239.events.recycler.EventChooseAdapter;
import com.jiuwfung.comp6239.events.recycler.EventChooseAdapterItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventChooseActivity extends AppCompatActivity {

    DatabaseReference mDatayears;
    DatabaseReference mDataevents ;
    DatabaseReference mDataevent ;
    DatabaseReference mDatastudents;

    public SharedPreferences sharedEventCreate;

    public HashMap hashStudent;
    public Set setStudentID;
    public Iterator iterorStudentID;

    @BindView(R.id.list_y1g1)
    ListView mEventChooseList;
    @BindView(R.id.create_event_choose_button)
    ImageButton mImageButton;

    public static String userIdentity;
    public static String eventTitle;
    public static String eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_choose);

        mDataevents = FirebaseDatabase.getInstance().getReference().child("Events");
        mDatayears = FirebaseDatabase.getInstance().getReference().child("Years");
        mDatastudents = FirebaseDatabase.getInstance().getReference().child("Students");

        sharedEventCreate = getSharedPreferences("EventCreate",Context.MODE_PRIVATE);
        eventTitle = sharedEventCreate.getString("Title" , "");
        eventDescription = sharedEventCreate.getString("Description" , "");

        Toast.makeText(this , eventTitle , Toast.LENGTH_LONG).show();

        mDataevent = mDataevents.child(eventTitle);


        hashStudent = new HashMap();

        ButterKnife.bind(this);

        mEventChooseList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        mEventChooseList.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener());

        onUpdateList();
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, EventChooseActivity.class));
    }

    @OnClick(R.id.create_event_choose_button)
    public void onAddStudent(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ready to add");
        builder.setMessage("Are you sure add them to "+ eventTitle + " ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataevent.child("Parts").setValue(hashStudent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EventChooseActivity.this , "Add Student Success!" , Toast.LENGTH_LONG).show();
                        Log.d("Add student" , "Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventChooseActivity.this , "Add Student Failure! "+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                        Log.e("Add student" , "Failure! "+e.getLocalizedMessage());
                    }
                });

                setStudentID = hashStudent.keySet();
                iterorStudentID = setStudentID.iterator();

                while (iterorStudentID.hasNext()){
                    String tem_studentID = (String) iterorStudentID.next();
                    Log.e("StudentID" , tem_studentID);
                    mDatastudents.child(tem_studentID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String tem_studentyear = dataSnapshot.child("Year").getValue().toString();
                            String tem_studentgroup = dataSnapshot.child("Group").getValue().toString();
                            mDatayears.child(tem_studentyear).child(tem_studentgroup).child(tem_studentID)
                                    .child("Events").child(eventTitle).setValue(eventDescription);
                            Toast.makeText(EventChooseActivity.this , "Add "+ eventTitle + " To "+ tem_studentID + " Sucess!",
                                    Toast.LENGTH_LONG).show();
                            Log.d("Add evnent to student" , "Sucess!");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(EventChooseActivity.this , "Add "+ eventTitle + " To "+ tem_studentID + " Failure!",
                                    Toast.LENGTH_LONG).show();
                            Log.d("Add evnent to student" , "Failure!");
                        }
                    });
                }

                Log.d("Add Student to Event." , "Finished!");
                onFinish();

            }
        }).setNegativeButton("Check it again!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("Create return!" , "User anction!");
                return;
            }
        });
        builder.create().show();
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

    public void onFinish(){
        this.finish();
    }
}
