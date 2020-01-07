package com.example.apt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.annotation.Arouter;

@Arouter(path = "main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpToTwo(View v) {
        startActivity(new Intent(this,TwoActivity$$Arouter.findTarget("two")));
    }

    public void jumpToOne(View v) {
        startActivity(new Intent(this,OneActivity$$Arouter.findTarget("one")));
    }
}
