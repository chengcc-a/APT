package com.example.apt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.annotation.Arouter;

@Arouter(path="one")
public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }
}
