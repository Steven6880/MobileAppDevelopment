<<<<<<< HEAD
package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Group Huifeng, Kejia, Yunheng
 */

public abstract class Fragment extends androidx.fragment.app.Fragment {

    protected View mRoot; // to protect view

    protected Unbinder mRootUnbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        initArgs(getArguments()); //get parameters of fragments
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot==null) { //if the fragment is empty, create root fragment.
            int layId = getContentLayoutId(); // get Id
            View root = inflater.inflate(layId, container, false); //root fragment
            initWidget(root);
            mRoot = root;
        }else {
            if(mRoot.getParent()!=null){
                //remove this fragment from its parent.
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init Data after View was created
        initData();
    }

    // init paramaters
    public void initArgs(Bundle bundle){

    }

    // @Return Id of source
    public abstract int getContentLayoutId();

    // init Widget
    public void initWidget( View root ){
        //绑定到当前
        mRootUnbinder = ButterKnife.bind(this , root );
    }

    // init Data
    public void initData(){

    }

    /**
     * when back button is pressed
     * @return false means there has not handle the request. Activity takes its own logic.
     * @return true means the request has been handled. Activity does not need to finish.
     */
    public boolean onBackPressed(){
        return false;
    }
}
=======
package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Group Huifeng, Kejia, Yunheng
 */

public abstract class Fragment extends androidx.fragment.app.Fragment {

    protected View mRoot; // to protect view

    protected Unbinder mRootUnbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        initArgs(getArguments()); //get parameters of fragments
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot==null) { //if the fragment is empty, create root fragment.
            int layId = getContentLayoutId(); // get Id
            View root = inflater.inflate(layId, container, false); //root fragment
            initWidget(root);
            mRoot = root;
        }else {
            if(mRoot.getParent()!=null){
                //remove this fragment from its parent.
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init Data after View was created
        initData();
    }

    // init paramaters
    public void initArgs(Bundle bundle){

    }

    // @Return Id of source
    public abstract int getContentLayoutId();

    // init Widget
    public void initWidget( View root ){
        //绑定到当前
        mRootUnbinder = ButterKnife.bind(this , root );
    }

    // init Data
    public void initData(){

    }

    /**
     * when back button is pressed
     * @return false means there has not handle the request. Activity takes its own logic.
     * @return true means the request has been handled. Activity does not need to finish.
     */
    public boolean onBackPressed(){
        return false;
    }
}
>>>>>>> 87ea13dcc63eaf6c2826a1f70c5aea3654665388
