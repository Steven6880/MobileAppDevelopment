package com.jiuwfung.comp6239.main.fragments;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jiuwfung.comp6239.App;
import com.jiuwfung.comp6239.EventActivity;
import com.jiuwfung.comp6239.EventChooseActivity;
import com.jiuwfung.comp6239.R;
import com.jiuwfung.comp6239.helper.EventAdapterItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    public SharedPreferences sharedEvents ;
    public SharedPreferences.Editor editorEvents ;

    public SharedPreferences sharedEventList;
    public SharedPreferences.Editor editorEventList;

    public StorageReference mStoragepicture = FirebaseStorage.getInstance().getReference();

    public static int MaxPicSize = 1024 * 1024 * 20;
    public static Boolean EVENT_CHECK;
    public Set<String> Event_List = new HashSet<String>();
    public Iterator<String> iterator = Event_List.iterator();

    @BindView(R.id.event_list_view)
    ListView mEventListView;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this , view);

        EVENT_CHECK = sharedEventList.getBoolean("EventCheck" , false);
        Log.e("Get Student Event List" , Boolean.toString(EVENT_CHECK));
        Event_List = sharedEventList.getStringSet("EventList" , null);

        if(Event_List!=null){
            Log.e("Event List Not Null!" , String.valueOf(Event_List.size()));
            while (iterator.hasNext()){
                Log.e("Event List!" , iterator.next());
            }
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedEvents = this.getActivity().getSharedPreferences("EventAccess" , Context.MODE_PRIVATE);
        editorEvents = sharedEvents.edit();

        sharedEventList =this.getActivity().getSharedPreferences("EventList" , Context.MODE_PRIVATE);
        editorEventList = sharedEventList.edit();
    }

    @Override
    public void onStart() {
        super.onStart();

        updateList();
    }

    private void updateList(){
        ArrayList<EventAdapterItem> mEventAdapter = new ArrayList<EventAdapterItem>();


        if(EVENT_CHECK){
            Event_List = sharedEventList.getStringSet("EventList" , null);
            editorEventList.remove("EventList");
            editorEventList.putBoolean("EventCheck" , false);
            editorEventList.commit();

            for(String string:Event_List){
                FirebaseDatabase.getInstance().getReference().child("Events")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    if(string.equals(dataSnapshot1.getKey())){
                                        String tem_Title = dataSnapshot1.getKey();
                                        String tem_Date = dataSnapshot1.child("Date").getValue().toString();
                                        String tem_Detail = dataSnapshot1.child("Detail").getValue().toString();
                                        String tem_Location = dataSnapshot1.child("Location").getValue().toString();
                                        String tem_ShortDescription = dataSnapshot1.child("ShortDescription").getValue().toString();
                                        String tem_Time = dataSnapshot1.child("Time").getValue().toString();
                                        List<String> listStudents = new ArrayList<>();;
                                        if(dataSnapshot1.child("Parts")!=null){
                                            for(DataSnapshot dataSnapshot2:dataSnapshot1.child("Parts").getChildren()){
                                                String tem_StudentID = dataSnapshot2.getKey();
                                                if(!tem_StudentID.isEmpty()){
                                                    listStudents.add(tem_StudentID);
                                                }
                                            }
                                        }
                                        EventAdapterItem item = new EventAdapterItem(tem_Title , tem_Date , tem_Time , tem_Location , tem_ShortDescription,
                                                tem_Detail , listStudents);
                                        mEventAdapter.add(item);
                                    }
                                    mEventListView.setAdapter(new mEventListAdapter(mEventAdapter));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }else {
            FirebaseDatabase.getInstance().getReference().child("Events")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Log.d("Getting Events!" , dataSnapshot.getKey());
                                String tem_Title = dataSnapshot1.getKey();
                                String tem_Date = dataSnapshot1.child("Date").getValue().toString();
                                String tem_Detail = dataSnapshot1.child("Detail").getValue().toString();
                                String tem_Location = dataSnapshot1.child("Location").getValue().toString();
                                String tem_ShortDescription = dataSnapshot1.child("ShortDescription").getValue().toString();
                                String tem_Time = dataSnapshot1.child("Time").getValue().toString();
                                List<String> listStudents = new ArrayList<>();;
                                if(dataSnapshot1.child("Parts")!=null){
                                    for(DataSnapshot dataSnapshot2:dataSnapshot1.child("Parts").getChildren()){
                                        String tem_StudentID = dataSnapshot2.getKey();
                                        if(!tem_StudentID.isEmpty()){
                                            listStudents.add(tem_StudentID);
                                        }
                                    }
                                }
                                EventAdapterItem item = new EventAdapterItem(tem_Title , tem_Date , tem_Time , tem_Location , tem_ShortDescription,
                                        tem_Detail , listStudents);
                                mEventAdapter.add(item);
                            }
                            mEventListView.setAdapter(new mEventListAdapter(mEventAdapter));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Getting Events!" , databaseError.getDetails());
                        }
                    });
        }
    }

    private class mEventListAdapter extends BaseAdapter{

        public ArrayList<EventAdapterItem> listEventItem;

        public mEventListAdapter(ArrayList<EventAdapterItem> listEventItem){
            this.listEventItem = listEventItem;
        }

        @Override
        public int getCount() {
            return listEventItem.size();
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
            View v = layoutInflater.inflate(R.layout.view_card_event , null);

            EventAdapterItem item = listEventItem.get(position);

            ImageView mEventPicture = (ImageView)v.findViewById(R.id.card_image);
            TextView mEventTitle = (TextView)v.findViewById(R.id.card_title);
            TextView mEventTime = (TextView)v.findViewById(R.id.card_time);
            TextView mEventDate = (TextView)v.findViewById(R.id.card_date);
            TextView mEventLocation = (TextView)v.findViewById(R.id.card_location);
            TextView mEventLearnMore =(TextView)v.findViewById(R.id.card_learn_more);


//            mStoragepicture.child("Event/"+item.Title+".jpg").getBytes(MaxPicSize)
//                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0 , bytes.length);
//                mEventPicture.setImageBitmap(bitmap);
//                Log.d("Load Event Picture.","Success!");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getActivity() , "LoadView Event Picture!" , Toast.LENGTH_LONG).show();
//                    Log.e("Load Event Picture." , "Failure!");
//                }
//            });

            Glide.with(v)
                    .asBitmap()
                    .load(R.drawable.bg_src_tianjin)
                    .into(mEventPicture);

            mEventTitle.setText(item.Title);
            mEventTime.setText(item.Time);
            mEventDate.setText(item.Date);
            mEventLocation.setText(item.Location);


            String meventtitle = item.Title;
            String meventdate = item.Date;
            String meventtime = item.Time;
            String meventlocation = item.Location;
            String meventshortdescription = item.ShortDescription;
            String meventdetail = item.Detail;
            mEventLearnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editorEvents.putBoolean("Editable" , false);
                    editorEvents.putString("Title" , meventtitle);
                    editorEvents.putString("Date" , meventdate);
                    editorEvents.putString("Time" , meventtime);
                    editorEvents.putString("Location" , meventlocation);
                    editorEvents.putString("ShortDescription" , meventshortdescription);
                    editorEvents.putString("Detail" , meventdetail);
                    editorEvents.commit();

                    Intent intent=new Intent();
                    intent.setClass(getActivity(), EventActivity.class);
                    startActivity(intent);

                }
            });

            return v;
        }
    }

}
