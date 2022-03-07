package com.example.smarttourapp.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smarttourapp.R;
import com.example.smarttourapp.ui.activities.SearchCity;
import com.example.smarttourapp.ui.activities.DisplayFlightPrediction;
import com.example.smarttourapp.ui.activities.DisplayTrainPrediction;
import com.example.smarttourapp.utils.Global;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class PredictionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText noOfDay, noOfPerson;
    TextView from, to;
    Button predict;
    Spinner spinner;
    RadioGroup radioGroup;
    int pos;
    Boolean trainOption = false;
    Boolean flightOption = false;
    RadioButton selectedRadioButton;
    String selectedRbText;
    private static final String[] paths = {"1 Star", "2 Star", "3 Star", "4 Star", "5 Star"};
    private static final String[] star = {"1", "2", "3", "4", "5"};

    LinearLayout fromFrame, toFrame;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prediction, container, false);

        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);

//        if(Global.fromAddress.getCity()!=null){
//            from.setText(Global.fromAddress.getCity());
//            to.setText(Global.toAddress.getCity());
//        }

        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Travel Cost Estimations");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        noOfDay = view.findViewById(R.id.no_of_days);
        noOfPerson = view.findViewById(R.id.no_of_persons);
        predict = view.findViewById(R.id.predict);
        radioGroup = view.findViewById(R.id.radioGroup);
        fromFrame = view.findViewById(R.id.fromFrame);
        toFrame = view.findViewById(R.id.toFrame);
        fromFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), SearchCity.class);
                myIntent.putExtra("operation", "from");
                startActivityForResult(myIntent, 1);
            }
        });
        toFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), SearchCity.class);
                myIntent.putExtra("operation", "to");
                startActivityForResult(myIntent, 2);

            }
        });

//        from.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(getContext(), SearchLocation.class);
//                startActivity(myIntent);
//            }
//        });

        RadioButton train = view.findViewById(R.id.radio_train);
        RadioButton flight = view.findViewById(R.id.radio_flight);

        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainOption = true;
                flightOption = false;
            }
        });
        flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightOption = true;
                trainOption = false;
            }
        });


        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (train.getId() == selectedRadioButtonId) {

            trainOption = true;
            flightOption = false;

        } else {

            flightOption = true;
            trainOption = false;
        }


        predict.setOnClickListener(v -> {
            if (trainOption) {
                Intent intent = new Intent(getActivity(), DisplayTrainPrediction.class);

                intent.putExtra("day", noOfDay.getText().toString());
                intent.putExtra("person", noOfPerson.getText().toString());
                intent.putExtra("star", star[pos]);

                startActivity(intent);
            }

            if(flightOption){
                Intent intent = new Intent(getActivity(), DisplayFlightPrediction.class);

                intent.putExtra("day", noOfDay.getText().toString());
                intent.putExtra("person", noOfPerson.getText().toString());
                intent.putExtra("star", star[pos]);
                startActivity(intent);
            }


        });

        spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                pos = 0;
                break;
            case 1:
                pos = 1;

                break;
            case 2:
                pos = 2;

                break;
            case 3:
                pos = 3;

                break;

            case 4:
                pos = 4;

                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        pos = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            from.setText(Global.fromAddress.getCity());

        }
        if (requestCode == 2) {
            to.setText(Global.toAddress.getCity());

        }


    }
}