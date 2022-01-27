package com.example.smarttourapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.smarttourapp.R;
import com.example.smarttourapp.model.LoginResponse;
import com.example.smarttourapp.model.RegisterBody;
import com.example.smarttourapp.retrofit.RetrofitArrayApi;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener{

    TextView textView;

    @NotEmpty
    @Length(min = 3, max = 30)
    EditText name;

    @NotEmpty
    @Length(min = 4, max = 30)
    EditText username;

    @NotEmpty
    @Password
    @Length(min = 6, max = 40)
    EditText password;
    Button signup;

    @ConfirmPassword
    private EditText ConfirmPassword;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView=findViewById(R.id.signinIntent);
        signup=findViewById(R.id.signup);
        name=findViewById(R.id.editTextName);
        username=findViewById(R.id.editTextUserName);
        password=findViewById(R.id.editTextPassword);
        ConfirmPassword=findViewById(R.id.confirmPassword);
        validator = new Validator(this);
        validator.setValidationListener(this);

        textView.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        });

        signup.setOnClickListener(v -> validator.validate());



    }


    public void loadJson(String name,String username,String password){


        String baseURL = "https://smart-tourist-app.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<LoginResponse> call=service.registerUser(new RegisterBody(username,name,password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {

                if (response.code() ==200) {
                    assert response.body() != null;
                    Toast toast=Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                    JWT parsedJWT = new JWT(response.body().getData());

                    Claim name = parsedJWT.getClaim("name");
                    Claim username = parsedJWT.getClaim("username");
                    Claim id = parsedJWT.getClaim("id");

                    saveUser(id.asString(),username.asString(), name.asString(),response.body().getData(),true);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                }else if(response.code() ==400){
                    Toast toast=Toast.makeText(getApplicationContext(),"Username already in use", Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                }else{
                    assert response.body() != null;
                    Toast toast=Toast.makeText(getApplicationContext(),response.body().getData(),Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        loadJson(name.getText().toString(),username.getText().toString(),password.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void saveUser(String id,String username, String name,String token, Boolean loginStatus) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("username", username);
        editor.putString("name", name);
        editor.putString("token", token);
        editor.putBoolean("loginStatus",loginStatus);
        editor.apply();
    }



    public String getName() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getString("saveString", "user");
    }





}