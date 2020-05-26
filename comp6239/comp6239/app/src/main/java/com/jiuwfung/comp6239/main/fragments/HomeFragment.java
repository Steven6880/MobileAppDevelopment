package com.jiuwfung.comp6239.main.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jiuwfung.comp6239.EventActivity;
import com.jiuwfung.comp6239.R;
import com.jiuwfung.comp6239.StudentEventsActivity;
import com.jiuwfung.comp6239.helper.StudentAdapterItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public DatabaseReference mDatayears ;
    public DatabaseReference mDataStudents;

    @BindView(R.id.home_list_view)
    ListView mHomeListView;

    public SharedPreferences sharedIdentity;
    public SharedPreferences.Editor editorIdentity;
    public SharedPreferences sharedEvents ;
    public SharedPreferences.Editor editorEvents ;
    public SharedPreferences sharedStudent;
    public SharedPreferences.Editor editorStudent;
    public SharedPreferences sharedParent;
    public SharedPreferences.Editor editorParent;
    public SharedPreferences sharedEventList;
    public SharedPreferences.Editor editorEventList;

    public static String USER_IDENTITY;
    public Set<String> SetStudents;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this , view);

        mDatayears = FirebaseDatabase.getInstance().getReference().child("Years");
        mDataStudents = FirebaseDatabase.getInstance().getReference().child("Students");

        USER_IDENTITY=sharedIdentity.getString("Identity","Manager");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateList();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedIdentity = this.getActivity().getSharedPreferences("Identity" , Context.MODE_PRIVATE);
        editorIdentity = sharedIdentity.edit();

        sharedStudent = this.getActivity().getSharedPreferences("Student",Context.MODE_PRIVATE);
        editorStudent = sharedStudent.edit();

        sharedParent = this.getActivity().getSharedPreferences("Parent" , Context.MODE_PRIVATE);
        editorParent = sharedParent.edit();

        sharedEvents = this.getActivity().getSharedPreferences("EventAccess" , Context.MODE_PRIVATE);
        editorEvents = sharedEvents.edit();

        sharedEventList =this.getActivity().getSharedPreferences("EventList" , Context.MODE_PRIVATE);
        editorEventList = sharedEventList.edit();
    }

    public void updateList(){
        ArrayList<StudentAdapterItem> listStudentAdapter = new ArrayList<StudentAdapterItem>();

        if(USER_IDENTITY.equals("Student")){
            Set<String> SetEvents = new HashSet<String>();
            String Year = sharedStudent.getString("Year" , "Year");
            String Group = sharedStudent.getString("Group" , "Group");
            String ID = sharedStudent.getString("StudentID" , "StudentID");
            String FirstName = sharedStudent.getString("FirstName" , "FirstName");
            String LastName = sharedStudent.getString("LastName" , "LastName");

            mDatayears.child(Year).child(Group).child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Events")!=null){
                            for(DataSnapshot dataSnapshot2:dataSnapshot.child("Events").getChildren()){
                                SetEvents.add(dataSnapshot2.getKey());
                            }
                            StudentAdapterItem item= new StudentAdapterItem(ID , FirstName , LastName , Year , Group , SetEvents);
                            listStudentAdapter.add(item);
                            Log.d("Get Student Evnets!" , "Sucess!");
                        }
                    mHomeListView.setAdapter(new mStudentAdapter(listStudentAdapter));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Get Student Events!" , "Failure"+databaseError.getDetails());
                }
            });
        }

        if(USER_IDENTITY.equals("Parent")){
            SetStudents = sharedParent.getStringSet("Children" , null);String Year = sharedStudent.getString("Year" , "Year");


            for(String string:SetStudents){
                mDataStudents.child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Set<String> SetEvents = new HashSet<String>();
                        String tem_student_id = dataSnapshot.getKey();
                        String tem_year = dataSnapshot.child("Year").getValue().toString();
                        String tem_group = dataSnapshot.child("Group").getValue().toString();
                        mDatayears.child(tem_year).child(tem_group).child(tem_student_id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String tem_name = dataSnapshot.child("FirstName").getValue().toString();
                                String tem_last = dataSnapshot.child("LastName").getValue().toString();
                                if(dataSnapshot.child("Events")!=null){
                                    for(DataSnapshot dataSnapshot1:dataSnapshot.child("Events").getChildren()){
                                        if(dataSnapshot1.getKey()!=null){
                                            SetEvents.add(dataSnapshot1.getKey());
                                        }
                                    }
                                    Log.d("Get Student Events!" , "Success!");
                                    StudentAdapterItem item = new StudentAdapterItem(tem_student_id ,tem_name ,
                                            tem_last , tem_year , tem_group , SetEvents);
                                    listStudentAdapter.add(item);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Get Student Events!" , "Failure"+databaseError.getDetails());
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Get Student Events!" , "Failure"+databaseError.getDetails());
                    }
                });
            }
            mHomeListView.setAdapter(new mStudentAdapter(listStudentAdapter));

        }

        if(USER_IDENTITY.equals("Manager")){
            mDatayears.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String tem_year = dataSnapshot1.getKey();
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            String tem_group = dataSnapshot2.getKey();
                            for(DataSnapshot dataSnapshot3:dataSnapshot2.getChildren()){
                                Set<String> SetEvents = new HashSet<String>();
                                String tem_student_id = dataSnapshot3.getKey();
                                String tem_name = dataSnapshot3.child("FirstName").getValue().toString();
                                String tem_last = dataSnapshot3.child("LastName").getValue().toString();
                                if(dataSnapshot3.child("Events")!=null){
                                    for(DataSnapshot dataSnapshot4:dataSnapshot3.child("Events").getChildren()){
                                        if(!dataSnapshot4.getKey().equals(null)){
                                            SetEvents.add(dataSnapshot4.getKey());
                                        }
                                    }
                                    StudentAdapterItem item = new StudentAdapterItem(tem_student_id ,tem_name ,
                                            tem_last , tem_year , tem_group , SetEvents);
                                    listStudentAdapter.add(item);
                                }
                            }
                        }
                    }
                    mHomeListView.setAdapter(new mStudentAdapter(listStudentAdapter));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




    }

    public class mStudentAdapter extends BaseAdapter{

        public ArrayList<StudentAdapterItem> listStudentItem;

        public mStudentAdapter(ArrayList<StudentAdapterItem> listStudentItem){
            this.listStudentItem = listStudentItem;
        }

        @Override
        public int getCount() {
            return listStudentItem.size();
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
            Set<String> SetEvents = new HashSet<String>();
            LayoutInflater layoutInflater = getLayoutInflater();
            View homeview = layoutInflater.inflate(R.layout.card_view_home , null);

            StudentAdapterItem item = listStudentItem.get(position);

            TextView mStudentGroup = homeview.findViewById(R.id.card_home_group);
            TextView mStudentID = homeview.findViewById(R.id.card_id);
            TextView mStudentName = homeview.findViewById(R.id.card_name);
            TextView mStudentEvents = homeview.findViewById(R.id.card_home_events);

            mStudentGroup.setText(item.Group);
            mStudentID.setText(item.StudentID);
            mStudentName.setText(item.FirstName);

            SetEvents = item.Events;
            Log.e("Adapter Event!" , item.StudentID+ ":" + SetEvents.toString());
            Set<String> finalSetEvents = SetEvents;
            mStudentEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editorEventList.putStringSet("EventList" , finalSetEvents).commit();
                    editorEventList.putBoolean("EventCheck" , true).commit();
                    editorEventList.putString("Name" , item.FirstName).commit();

                    Intent intent=new Intent();
                    intent.setClass(getActivity(), StudentEventsActivity.class);
                    startActivity(intent);
                }
            });

            return homeview;
        }
    }
}
