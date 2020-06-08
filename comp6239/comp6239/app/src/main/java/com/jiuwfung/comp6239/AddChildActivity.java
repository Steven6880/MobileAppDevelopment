package com.jiuwfung.comp6239;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddChildActivity extends AppCompatActivity {

    public FirebaseAuth mAuth ;
    public FirebaseUser mUser;
    public DatabaseReference mDatayears ;
    public DatabaseReference mDatastudents ;
    public DatabaseReference mDataaccounts;

    @BindView(R.id.edit_add_child_id)
    EditText mAddID;

    @BindView(R.id.edit_add_name)
    EditText mAddName;

    @BindView(R.id.btn_add_submit)
    Button mAddSubmit;

    public static String STRING_UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        STRING_UID = mUser.getUid();
        mDatayears = FirebaseDatabase.getInstance().getReference().child("Years");
        mDatastudents = FirebaseDatabase.getInstance().getReference().child("Students");
        mDataaccounts = FirebaseDatabase.getInstance().getReference().child("Account");
    }

    @OnClick(R.id.btn_add_submit)
    public void onAddStudent(){
        String childID = mAddID.getText().toString();
        String childName = mAddName.getText().toString();

        mDataaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(STRING_UID).child("Children").child(childID)!=null){
                    Toast.makeText(AddChildActivity.this, "Your Child Already Added! Please Check it!"
                            , Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatastudents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(childID)!=null){
                    String Year = dataSnapshot.child(childID).child("Year").getValue().toString();
                    String Group = dataSnapshot.child(childID).child("Group").getValue().toString();

                    mDatayears.child(Year).child(Group).child(childID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("FirstName").getValue().toString().equals(childName)){
                                mDataaccounts.child(STRING_UID).child("Children").child(childID).setValue(childName)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AddChildActivity.this, "Add Information to Database"
                                                , Toast.LENGTH_LONG).show();
                                                onFinish();
                                            }
                                        });
                            }else {
                                Toast.makeText(AddChildActivity.this, "Information wrong! Please Check it!"
                                        , Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, AddChildActivity.class));
    }

    public void onFinish(){
        this.finish();
    }
}
