package com.example.smarttourapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.smarttourapp.ui.activities.LoginActivity;
import com.example.smarttourapp.R;
import com.example.smarttourapp.ui.activities.MainActivity;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
         TextView textView = view.findViewById(R.id.logout);
        TextView username = view.findViewById(R.id.usernameTextView);
        username.setText(((MainActivity)getActivity()).getName());
        LinearLayout profileLayout1 =view.findViewById(R.id.profileLayout1);
        ScrollView profileLayout2 =view.findViewById(R.id.profileLayout2);
        Button button=view.findViewById(R.id.profileLogin);
        Switch s=view.findViewById(R.id.rec);

        s.setChecked(((MainActivity) getActivity()).getRec());

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(((MainActivity)getActivity()).getRec()){
                    s.setChecked(false);
                    ((MainActivity)getActivity()).recommendationOff();
                }
                else {
                    s.setChecked(true);
                    ((MainActivity)getActivity()).recommendationOn();
                }
            }
        });





        if(((MainActivity)getActivity()).getLoginStatus()) {
            profileLayout1.setVisibility(View.INVISIBLE);
            profileLayout2.setVisibility(View.VISIBLE);
        }else{


            profileLayout1.setVisibility(View.VISIBLE);
            profileLayout2.setVisibility(View.INVISIBLE);
        }



         textView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ((MainActivity)getActivity()).logout();

                 if(((MainActivity)getActivity()).getLoginStatus()) {
                     profileLayout1.setVisibility(View.INVISIBLE);
                     profileLayout2.setVisibility(View.VISIBLE);
                 }else{


                     profileLayout1.setVisibility(View.VISIBLE);
                     profileLayout2.setVisibility(View.INVISIBLE);
                 }

             }
         });

        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });


        return  view;
    }


}