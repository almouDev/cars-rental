package com.almou.carsrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.almou.carsrental.firebase.Authentication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Authentication.createCredentials("issoufoos1234@gmail.com","1234",this);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}