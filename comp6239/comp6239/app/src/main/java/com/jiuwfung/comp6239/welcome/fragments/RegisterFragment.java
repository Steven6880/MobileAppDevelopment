package com.jiuwfung.comp6239.welcome.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jiuwfung.comp6239.Application;
import com.jiuwfung.comp6239.R;
import com.jiuwfung.comp6239.helper.MyGlideEngine;
import com.jiuwfung.comp6239.helper.WelcomeStorage;
import com.jiuwfung.comp6239.helper.WelcomeTrigger;
import com.jiuwfung.comp6239.widget.PortraitView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    public WelcomeTrigger mWelcomeTrigger;
    public WelcomeStorage mWelcomeStorage;

    // Return of Matisse
    List<Uri> mSelected;
    String mPicture;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    StorageReference mStoragepicture;
    DatabaseReference mDatayears = FirebaseDatabase.getInstance().getReference().child("Years");
    DatabaseReference mDatastudents = FirebaseDatabase.getInstance().getReference().child("Students");
    DatabaseReference mDataaccounts = FirebaseDatabase.getInstance().getReference().child("Account");
    DatabaseReference mDatayear ;
    DatabaseReference mDatagroup ;
    DatabaseReference mDatastudent ;
    DatabaseReference mDatafather ;
    DatabaseReference mDatamother ;


    HashMap Student = new HashMap();
    HashMap Father = new HashMap();
    HashMap Mother = new HashMap();
    HashMap Students = new HashMap();
    HashMap Account = new HashMap();

    @BindView(R.id.register_view)
    ScrollView mRegisterView;

    @BindView(R.id.im_register_portrait)
    PortraitView mPortraitView;
    @BindView(R.id.im_register_gender)
    ImageView mGender;
    @BindView(R.id.edit_email)
    EditText mEmail;
    @BindView(R.id.edit_first_name)
    EditText mName;
    @BindView(R.id.edit_last_name)
    EditText mLastName;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.edit_confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.switch_student_parent)
    Switch mSwitchSP;
    @BindView(R.id.edit_student_id)
    EditText mStudentID;
    @BindView(R.id.edit_register_father_name)
    EditText mFathername;
    @BindView(R.id.edit_register_father_number)
    EditText mFathernumber;
    @BindView(R.id.edit_register_mother_name)
    EditText mMothername;
    @BindView(R.id.edit_register_mother_number)
    EditText mMohternumber;
    @BindView(R.id.edit_child_id)
    EditText mChildID;
    @BindView(R.id.edit_register_contact_number)
    EditText mContactnumber;
    @BindView(R.id.edit_register_relation)
    TextView mRelation;
    @BindView(R.id.edit_year)
    TextView mYear;
    @BindView(R.id.edit_group)
    TextView mGroup;
    @BindView(R.id.txt_go_login)
    TextView mGoLogin;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.student_student_id)
    LinearLayout mLayoutStudentID;
    @BindView(R.id.student_year)
    LinearLayout mLayoutStudentyear;
    @BindView(R.id.student_group)
    LinearLayout mLayoutStudentgroup;
    @BindView(R.id.student_father_name)
    LinearLayout mLayoutFathername;
    @BindView(R.id.student_father_number)
    LinearLayout mLayoutFatherNumber;
    @BindView(R.id.student_mother_name)
    LinearLayout mLayoutMothername;
    @BindView(R.id.student_mother_number)
    LinearLayout mLayoutMotherNumber;
    @BindView(R.id.parent_child_id)
    LinearLayout mLayoutChildID;
    @BindView(R.id.parent_contact_number)
    LinearLayout mLayoutContactNumber;
    @BindView(R.id.parent_relation)
    LinearLayout mLayoutParentRelation;

    private int shortAnimationDuration;

    private static final int REQUEST_CODE_PICTURE = 23;
    private static int INT_PICTURE=0;
    private static final int REQUEST_CODE_STORAGE = 1;
    private static final int RESULT_OK = -1;
    // 0 =male; 1=female
    private static int REQUEST_CODE_GENDER = 2;
    private static String STRING_RELATION = "";
    private static String STRING_GENDER = "";
    // 0=student;1=parent
    private static int REQUEST_CODE_SWITCH = 0;
    // 1=Year1 , 2 =...
    private static int REQUEST_CODE_YEAR = 0;
    private static String STRING_YEAR = "";
    private static String INT_YEAR = "0";
    // 1=Group1 , 2=...
    private static int REQUEST_CODE_GROUP = 0;
    private static String STRING_GROUP = "";
    private static String INT_GROUP = "0";
    // Length of Group, for Student ID
    private static String STRING_ACCOUNT;
    private static Uri resultUri;
    private static File RESULT_URI;
    private static String UID;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this , view);

        initSwitch();

        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        onChangeSwitch();

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mWelcomeTrigger = (WelcomeTrigger) context;
        mWelcomeStorage = (WelcomeStorage) context;
    }

    @OnClick(R.id.im_register_portrait)
    void onSelectPortrait(){
        mWelcomeStorage.welcomestorage();
        onRequestStorge();
    }

    @OnClick(R.id.im_register_gender)
    void onSelectGender(){
        final String[] genderlist= {"male" , "female"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.drawable.ic_genderchoose);
        builder.setTitle("Please Choose Your Gender");
        builder.setSingleChoiceItems(genderlist, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                REQUEST_CODE_GENDER = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(REQUEST_CODE_GENDER == 2){
                    Toast.makeText(getActivity() , "Please Choose Gender" , Toast.LENGTH_LONG).show();
                }

                if(REQUEST_CODE_GENDER == 0){
                    Toast.makeText(getActivity() , "Your Choice is Male" , Toast.LENGTH_LONG).show();
                    mGender.setImageResource(R.drawable.ic_sex_man);
                    STRING_GENDER = "Male";
                }

                if(REQUEST_CODE_GENDER == 1){
                    Toast.makeText(getActivity() , "Your Choice is Female" ,Toast.LENGTH_LONG).show();
                    mGender.setImageResource(R.drawable.ic_sex_woman);
                    STRING_GENDER = "Female";
                }
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.edit_year)
    void onSelectYear(){
        final String[] yearlist= {"Year1" , "Year2" , "Year3"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.drawable.ic_group);
        builder.setTitle("Please Choose Your Year");
        builder.setSingleChoiceItems(yearlist, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                REQUEST_CODE_YEAR = which+1;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(REQUEST_CODE_YEAR == 1){
                    mYear.setText("Year1");
                    STRING_YEAR = "Year1";
                    INT_YEAR="01";
                }
                if(REQUEST_CODE_YEAR == 2){
                    mYear.setText("Year2");
                    STRING_YEAR="Year2";
                    INT_YEAR = "02";
                }
                if(REQUEST_CODE_YEAR == 3){
                    mYear.setText("Year3");
                    STRING_YEAR = "Year3";
                    INT_YEAR="03";
                }
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.edit_group)
    void onSelectGroup(){
        final String[] grouplist= {"Group1" , "Group2" , "Group3"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setIcon(R.drawable.ic_group);
        builder.setTitle("Please Choose Your Group");
        builder.setSingleChoiceItems(grouplist, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                REQUEST_CODE_GROUP = which+1;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(REQUEST_CODE_GROUP == 1){
                    mGroup.setText("Group1");
                    STRING_GROUP="Group1";
                    INT_GROUP = "01";
                }
                if(REQUEST_CODE_GROUP == 2){
                    mGroup.setText("Group2");
                    STRING_GROUP="Group2";
                    INT_GROUP="02";
                }
                if(REQUEST_CODE_GROUP == 3){
                    mGroup.setText("Group3");
                    STRING_GROUP="Group3";
                    INT_GROUP="03";
                }
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.edit_register_relation)
    void onRelation() {
        if(REQUEST_CODE_SWITCH==1){
            if(REQUEST_CODE_GENDER == 0){
                mRelation.setText("Father");
                STRING_RELATION = "Father";
            }
            if(REQUEST_CODE_GENDER == 1){
                mRelation.setText("Mother");
                STRING_RELATION = "Mother";
            }
        }else {
            mRelation.setText(R.string.Label_edit_relation);
        }
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick(){
        // 让AccountActivity进行界面切换
        mWelcomeTrigger.triggerView();
    }

    @OnClick(R.id.btn_submit)
    public void createNewAccount(){
        String account = mEmail.getText().toString();
        String name = mName.getText().toString();
        String lastname = mLastName.getText().toString();
        String password = mPassword.getText().toString();
        String confirm_password = mConfirmPassword.getText().toString();
        String studentID = mStudentID.getText().toString();
        String year = STRING_YEAR;
        String group = STRING_GROUP;
        String gender = STRING_GENDER;
        String father_name = mFathername.getText().toString();
        String father_number = (mFathernumber.getText().toString());
        String mother_name = mMothername.getText().toString();
        String mother_number = (mMohternumber.getText().toString());
        String childID = (mChildID.getText().toString());
        String contactnumbner = (mContactnumber.getText().toString());
        String relation = STRING_RELATION;
        STRING_ACCOUNT = account.substring(0,account.indexOf("@"));

        if (INT_PICTURE!=1){
            Toast.makeText( getActivity() , "Choose Your Photo!" , Toast.LENGTH_LONG).show();
            return;
        }
        if(account.isEmpty()){
            Toast.makeText( getActivity() , "Enter Account!" , Toast.LENGTH_LONG).show();
            return;
        }
        if(name.isEmpty()){
            Toast.makeText( getActivity() , "Enter First Name!" , Toast.LENGTH_LONG).show();
            return;
        }
        if(lastname.isEmpty()){
            Toast.makeText( getActivity() , "Enter Last Name!" , Toast.LENGTH_LONG).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText( getActivity() , "Enter Password!" , Toast.LENGTH_LONG).show();
            return;
        }
        if(confirm_password.isEmpty()){
            Toast.makeText( getActivity() , "Enter Confirm Password!" , Toast.LENGTH_LONG).show();
            return;
        }

        if(REQUEST_CODE_SWITCH==0){

            if(studentID.isEmpty()){
                Toast.makeText( getActivity() , "Enter Student ID!" , Toast.LENGTH_LONG).show();
                return;
            }
            if(father_name.isEmpty()){
                Toast.makeText( getActivity() , "Enter Father First Name!" , Toast.LENGTH_LONG).show();
                return;
            }
            if(father_number.isEmpty()){
                Toast.makeText( getActivity() , "Enter Father Phone Number!" , Toast.LENGTH_LONG).show();
                return;
            }
            if(mother_name.isEmpty()){
                Toast.makeText( getActivity() , "Enter Mother Name!" , Toast.LENGTH_LONG).show();
                return;
            }
            if(mother_number.isEmpty()){
                Toast.makeText(getActivity() , "Enter Mother Phone Number!", Toast.LENGTH_LONG).show();
                return;
            }
            if(year.isEmpty()){
                Toast.makeText(getActivity() , "Choose Your Year!", Toast.LENGTH_LONG).show();
                return;
            }
            if(group.isEmpty()){
                Toast.makeText(getActivity() , "Choose Your Group!", Toast.LENGTH_LONG).show();
                return;
            }
            if(studentID.isEmpty()){
                Toast.makeText(getActivity() , "Enter Your ID!", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("PLease Check Your Student ID");
                builder.setMessage("Your ID is :"+studentID);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(password.equals(confirm_password)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("PLease Wait!");
                            builder.setMessage("Please Wait!");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.create().show();
                            //add account and password
                            mAuth.createUserWithEmailAndPassword(account,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                //set username;
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                                UID = user.getUid();

                                                //update data
                                                user.updateProfile(request);

                                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity() , "Please Check Your Email To Verify!" , Toast.LENGTH_LONG).show();
                                                        //go to login fragment
                                                        mWelcomeTrigger.triggerView();

                                                        if(REQUEST_CODE_SWITCH==0){
                                                            Student.put("Email" , account);
                                                            Student.put("FirstName" , name);
                                                            Student.put("LastName" , lastname);
                                                            Student.put("Gender" , gender);
                                                            Father.put("FirstName" , father_name);
                                                            Father.put("Number" , father_number);
                                                            Mother.put("FirstName" , mother_name);
                                                            Mother.put("Number",mother_number);
                                                            Students.put("Group" , STRING_YEAR+"_"+STRING_GROUP);
                                                            Students.put("Year" , STRING_YEAR);
                                                            Account.put("Identity" , "Student");
                                                            Account.put("Email" , account);
                                                            Account.put("StudentID" , studentID);
                                                            Account.put("Year" , STRING_YEAR);
                                                            Account.put("Group" , STRING_YEAR+"_"+STRING_GROUP);
                                                            Account.put("Portrait" , UID+".jpg");
                                                            mDatayear = mDatayears.child(STRING_YEAR);
                                                            mDatagroup = mDatayear.child(STRING_YEAR+"_"+STRING_GROUP);
                                                            //For Student

                                                            mStoragepicture = mStorage.child("Accounts/"+UID+".jpg");
                                                            mStoragepicture.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    Toast.makeText(getActivity() , "upload portriat sucess!" , Toast.LENGTH_LONG).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getActivity() , "upload portriat failure!" , Toast.LENGTH_LONG).show();
                                                                }
                                                            });

                                                            mDatastudent = mDatagroup.child(studentID);
                                                            mDatafather = mDatastudent.child("Father");
                                                            mDatamother = mDatastudent.child("Mother");
                                                            mDatastudent.setValue(Student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getActivity() , "Add Information to Database!",Toast.LENGTH_LONG).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getActivity() , "add information failure" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            mDatafather.setValue(Father).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getActivity() , "Add Information to Database!",Toast.LENGTH_LONG).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getActivity() , "add information failure" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            mDatamother.setValue(Mother)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(getActivity() , "Add Information to Database!",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getActivity() , "add information failure" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            mDatastudents.child(studentID).setValue(Students)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(getActivity() , "Add Information to Database!",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getActivity() , "add information failure" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            mDatastudents.child(studentID).child("Father").setValue(Father);
                                                            mDatastudents.child(studentID).child("Mother").setValue(Mother);
                                                            mDataaccounts.child(UID).setValue(Account).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getActivity() , "add account to database!",Toast.LENGTH_LONG).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getActivity() , "add account failure!",Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity() , "Something Went Wrongly!"+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity() , "Account create Failure!\n"+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText( getActivity() , "Check Passwords!" , Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                }).setNegativeButton("Rewrite It!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.create().show();
            }
        }

        if(REQUEST_CODE_SWITCH==1){
                if(contactnumbner.isEmpty()){
                    Toast.makeText(getActivity() , "Enter Contact Number!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(relation.isEmpty()){
                    Toast.makeText(getActivity() , "Press The Relation!", Toast.LENGTH_LONG).show();
                    return;
                }
            if(childID.isEmpty()){
                Toast.makeText(getActivity() , "Enter Child Id!", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("PLease Check Your Child ID");
                builder.setMessage("Your Child ID is :"+childID);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        if(password.equals(confirm_password)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("PLease Wait!");
                            builder.setMessage("Please Wait!");
                            builder.setCancelable(true);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.create().show();
                            //add account and password
                            mAuth.createUserWithEmailAndPassword(account,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                //set username;
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                                                //update data
                                                user.updateProfile(request);


                                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(getActivity() , "Please Check Your Email To Verify!" , Toast.LENGTH_LONG).show();
                                                        //go to login fragment
                                                        mWelcomeTrigger.triggerView();



                                                        if(REQUEST_CODE_SWITCH==0){

                                                        }else {
                                                            // to get Student Information
                                                            mDatastudents.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    STRING_GROUP = dataSnapshot.child(childID).child("Group").getValue().toString();
                                                                    STRING_YEAR = dataSnapshot.child(childID).child("Year").getValue().toString();
                                                                    String tem_father_number = dataSnapshot.child(childID).child("Father").child("Number").getValue().toString();
                                                                    String tem_father_name = dataSnapshot.child(childID).child("Father").child("FirstName").getValue().toString();
                                                                    String tem_mother_number = dataSnapshot.child(childID).child("Mother").child("Number").getValue().toString();
                                                                    String tem_mother_name = dataSnapshot.child(childID).child("Mother").child("FirstName").getValue().toString();

                                                                    Account.put("Year" , STRING_YEAR);
                                                                    Account.put("Group" , STRING_GROUP);
                                                                    if(STRING_GROUP.isEmpty()){
                                                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Cannot Find Your Child ID! Let Your Child Register Firstly!", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        });
                                                                    }else {
                                                                        if(REQUEST_CODE_GENDER==0){
                                                                            if(tem_father_name.equals(name)){
                                                                                Father.put("FirstName" , name);
                                                                            }else {
                                                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Your Name Maybe Wrong!", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                            if(tem_father_number.equals(contactnumbner)){
                                                                                Father.put("Number" , contactnumbner);
                                                                            }else {
                                                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Your ContactNumber Maybe Wrong!", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                            // For Father
                                                                            STRING_ACCOUNT = account.substring(0 , account.indexOf("@"));
                                                                            Father.put("Email" , account);
                                                                            Father.put("LastName" , lastname);
                                                                            Account.put("Email" , account);
                                                                            Account.put("ChildID" , childID);
                                                                            Account.put("Identity" , "Parent");
                                                                            Account.put("Portrait" , STRING_ACCOUNT+".jpg");
                                                                            mDatayear = mDatayears.child(STRING_YEAR);
                                                                            mDatagroup = mDatayear.child(STRING_GROUP);
                                                                            mDatastudent = mDatagroup.child(childID);
                                                                            mDatafather = mDatastudent.child("Father");

                                                                            mStoragepicture = mStorage.child("Accounts/"+UID+".jpg");
                                                                            mStoragepicture.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                    //checkpoint
                                                                                    Toast.makeText(getActivity() , "upload portriat sucess!" , Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    //checkpoint
                                                                                    Toast.makeText(getActivity() , "upload portriat failure!" , Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });

                                                                            mDatafather.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String tem_father_number = dataSnapshot.child("Number").getValue().toString();
                                                                                    String tem_father_name = dataSnapshot.child("FirstName").getValue().toString();

                                                                                }
                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                }
                                                                            });
                                                                            mDatafather.setValue(Father)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Toast.makeText(getActivity() , "Add Information to Database!",Toast.LENGTH_LONG).show();
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getActivity() , "add information failure" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                            mDataaccounts.child(STRING_ACCOUNT).setValue(Account).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Toast.makeText(getActivity() , "Add information to Database!", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getActivity() , "add account failure!" , Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                        }else {
                                                                            if(tem_mother_name.equals(name)){
                                                                                Mother.put("FirstName" , name);
                                                                            }else {
                                                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Your Name Maybe Wrong!", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                            if(tem_mother_number.equals(contactnumbner)){
                                                                                Mother.put("Number" , contactnumbner);
                                                                            }else {
                                                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Your ContactNumber Maybe Wrong!", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                            // For Mother
                                                                            STRING_ACCOUNT = account.substring(0 , account.indexOf("@"));
                                                                            Mother.put("Email", account);
                                                                            Mother.put("LastName" , lastname);
                                                                            Account.put("Identity" , "Parent");
                                                                            Account.put("ChildID" , childID );
                                                                            Account.put("Email" , account);
                                                                            Account.put("Portrait" , STRING_ACCOUNT+".jpg");
                                                                            mDatayear = mDatayears.child(STRING_YEAR);
                                                                            mDatagroup = mDatayear.child(STRING_GROUP);
                                                                            mDatastudent = mDatagroup.child(childID);
                                                                            mDatamother = mDatastudent.child("Mother");

                                                                            mStoragepicture = mStorage.child("Accounts/"+UID+".jpg");
                                                                            mStoragepicture.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                    //checkpoint
                                                                                    Toast.makeText(getActivity() , "upload portriat sucess!" , Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    //checkpoint
                                                                                    Toast.makeText(getActivity() , "upload portriat failure!" , Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });

                                                                            mDatamother.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String tem_father_number = dataSnapshot.child("Number").getValue().toString();
                                                                                    String tem_father_name = dataSnapshot.child("FirstName").getValue().toString();
                                                                                    if(tem_father_name.equals(name)){
                                                                                        Mother.put("FirstName" , name);
                                                                                    }else {
                                                                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Your Name Maybe Wrong!", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    if(tem_father_number.equals(contactnumbner)){
                                                                                        Mother.put("Number" , contactnumbner);
                                                                                    }else {
                                                                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                Toast.makeText(getActivity() , "Your Account Was Deleted, Cause Your ContactNumber Maybe Wrong!", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                }
                                                                            });
                                                                            mDatamother.setValue(Mother)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Toast.makeText(getActivity() , "Add Information to Database!",Toast.LENGTH_LONG).show();
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(getActivity() , "add information failure" +e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                            mDataaccounts.child(STRING_ACCOUNT).setValue(Account).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Toast.makeText(getActivity() , "Add Information to Database!" , Toast.LENGTH_LONG).show();
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity() , "Something Went Wrongly!"+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity() , "Account create Failure!\n"+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                                }
                            });

                        }else{
                            Toast.makeText( getActivity() , "Check Passwords!" , Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }).setNegativeButton("Rewrite It!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.create().show();
            }
        }
    }


    // 启动Switch按钮
    void initSwitch(){
        REQUEST_CODE_SWITCH=0;

        mSwitchSP.setChecked(false);

        mLayoutChildID.setVisibility(View.GONE);
        mLayoutContactNumber.setVisibility(View.GONE);
        mLayoutParentRelation.setVisibility(View.GONE);

        mLayoutStudentyear.setVisibility(View.VISIBLE);
        mLayoutStudentgroup.setVisibility(View.VISIBLE);
        mLayoutFathername.setVisibility(View.VISIBLE);
        mLayoutFatherNumber.setVisibility(View.VISIBLE);
        mLayoutMothername.setVisibility(View.VISIBLE);
        mLayoutMotherNumber.setVisibility(View.VISIBLE);
        mLayoutStudentID.setVisibility(View.VISIBLE);
    }

    // 更改Switch按钮状态以及动画
    void onChangeSwitch(){
        mSwitchSP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    REQUEST_CODE_SWITCH = 1;

                    mLayoutChildID.setAlpha(0f);
                    mLayoutChildID.setVisibility(View.VISIBLE);
                    mLayoutContactNumber.setAlpha(0f);
                    mLayoutContactNumber.setVisibility(View.VISIBLE);
                    mLayoutParentRelation.setAlpha(0f);
                    mLayoutParentRelation.setVisibility(View.VISIBLE);

                    mLayoutChildID.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutContactNumber.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutParentRelation.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);

                    mLayoutStudentyear.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutStudentyear.setVisibility(View.GONE);
                                }
                            });
                    mLayoutStudentgroup.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutStudentgroup.setVisibility(View.GONE);
                                }
                            });
                    mLayoutFathername.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutFathername.setVisibility(View.GONE);
                                }
                            });
                    mLayoutFatherNumber.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutFatherNumber.setVisibility(View.GONE);
                                }
                            });
                    mLayoutMothername.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutMothername.setVisibility(View.GONE);
                                }
                            });
                    mLayoutMotherNumber.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutMotherNumber.setVisibility(View.GONE);
                                }
                            });
                    mLayoutStudentID.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutStudentID.setVisibility(View.GONE);
                                }
                            });
                }else {
                    REQUEST_CODE_SWITCH = 0;

                    mLayoutStudentID.setAlpha(0f);
                    mLayoutStudentID.setVisibility(View.VISIBLE);
                    mLayoutStudentyear.setAlpha(0f);
                    mLayoutStudentyear.setVisibility(View.VISIBLE);
                    mLayoutStudentgroup.setAlpha(0f);
                    mLayoutStudentgroup.setVisibility(View.VISIBLE);
                    mLayoutFathername.setAlpha(0f);
                    mLayoutFathername.setVisibility(View.VISIBLE);
                    mLayoutFatherNumber.setAlpha(0f);
                    mLayoutFatherNumber.setVisibility(View.VISIBLE);
                    mLayoutMothername.setAlpha(0f);
                    mLayoutMothername.setVisibility(View.VISIBLE);
                    mLayoutMotherNumber.setAlpha(0f);
                    mLayoutMotherNumber.setVisibility(View.VISIBLE);

                    mLayoutStudentID.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutStudentyear.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutStudentgroup.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutFathername.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutFatherNumber.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutMothername.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);
                    mLayoutMotherNumber.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);

                    mLayoutChildID.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutChildID.setVisibility(View.GONE);
                                }
                            });
                    mLayoutContactNumber.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutContactNumber.setVisibility(View.GONE);
                                }
                            });
                    mLayoutParentRelation.animate()
                            .alpha(0f)
                            .setDuration(shortAnimationDuration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mLayoutParentRelation.setVisibility(View.GONE);
                                }
                            });
                }
            }

        });
    }


    @AfterPermissionGranted(REQUEST_CODE_STORAGE )
    public void onRequestStorge(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(EasyPermissions.hasPermissions(getActivity() , perms)){
            Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG , MimeType.PNG))
                    .countable(false)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
                    .thumbnailScale(0.85f)
                    .imageEngine(new MyGlideEngine())
                    .forResult(REQUEST_CODE_PICTURE);
        }else {
            EasyPermissions.requestPermissions(this , "Please Modify The Correct Permissions" ,
                    REQUEST_CODE_STORAGE, perms );
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICTURE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            mPicture = Matisse.obtainPathResult(data).get(0);
            Toast.makeText(getActivity(),"get picture" , Toast.LENGTH_LONG).show();
            UCrop.Options options = new UCrop.Options();
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            options.setCompressionFormat( Bitmap.CompressFormat.PNG);
            options.setCompressionQuality(96);

            // get the Cache Path of ImageView in this app
            File temPath = Application.getPortraitTmpFile();

            UCrop.of(mSelected.get(0) , Uri.fromFile(temPath))
                    //length and width
                    .withAspectRatio(1,1)
                    //size of return
                    .withMaxResultSize(520 , 520)
                    .start(getActivity());
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            resultUri = UCrop.getOutput(data);
            //Toast.makeText(getActivity() , resultUri.toString().substring(0,resultUri.toString().indexOf(".jpg")) , Toast.LENGTH_LONG).show();
            if(resultUri != null){
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void loadPortrait(Uri resultUri){
        Glide.with(this)
                .asBitmap()
                .load(resultUri)
                .centerCrop()
                .into(mPortraitView);

        INT_PICTURE = 1;
    }
}
