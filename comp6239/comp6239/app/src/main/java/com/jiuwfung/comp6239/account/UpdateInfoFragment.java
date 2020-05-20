package com.jiuwfung.comp6239.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuwfung.comp6239.R;
import com.jiuwfung.comp6239.widget.PortraitView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * TODO: UPDATE USER INFORMATION
 */
public class UpdateInfoFragment extends Fragment {

    @BindView(R.id.im_account_portrait)
    PortraitView mPortraitView;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_info, container, false);
    }

    @OnClick(R.id.im_account_portrait)
    void onPortraitClick(){
        // 图片选择的Fragment
    }
}
