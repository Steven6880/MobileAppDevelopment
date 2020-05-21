package com.jiuwfung.comp6239.welcome.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jiuwfung.comp6239.MainActivity;
import com.jiuwfung.comp6239.R;
import com.jiuwfung.comp6239.WelcomeActivity;
import com.jiuwfung.comp6239.helper.WelcomeTrigger;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    public FirebaseAnalytics mFirebaseAnalytics;
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public WelcomeTrigger mWelcomeTrigger;
    @BindView(R.id.edit_email)
    EditText mEmail;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.forget_password)
    TextView mForgetPassword;
    @BindView(R.id.txt_go_register)
    TextView mGoRegister;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this , view);

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){
            MainActivity.show(getActivity());
            getActivity().finish();
        }

        EditText account = view.findViewById(R.id.edit_email);
        if(user!=null){
            String useremail = user.getEmail();
            account.setText(useremail);
        }
        return view;
    }

    @OnClick(R.id.forget_password)
    public void ForgetPassword(){
        String account = mEmail.getText().toString();
        if(account.isEmpty()){
            Toast.makeText(getActivity() , "Please Enter Your Email Firstly!" , Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.sendPasswordResetEmail(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity() , "Successfully! Check Your Email Inbox!" , Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity() , "Failure! Please Check Your Email!" , Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void Login(){
        String account = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(account.isEmpty()){
            Toast.makeText(getActivity() , "Please Enter Email!" , Toast.LENGTH_LONG).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(getActivity() , "Please Enter Password!" , Toast.LENGTH_LONG).show();
            return;
        }

        //login after check
        mAuth.signInWithEmailAndPassword(account , password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity() , "Log In Failed."+e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = mAuth.getCurrentUser();
                //get user name
                String username = user.getDisplayName();

                MainActivity.show(getActivity());

                getActivity().finish();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 拿到Activity的引用
        mWelcomeTrigger = (WelcomeTrigger) context;
    }

    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick() {
        // 让AccountActivity进行界面切换
        mWelcomeTrigger.triggerView();
    }

}
