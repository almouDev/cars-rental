package com.almou.carsrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.almou.carsrental.firebase.Authentication;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Authentication.createCredentials("issoufoos1234@gmail.com","123456",this);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}